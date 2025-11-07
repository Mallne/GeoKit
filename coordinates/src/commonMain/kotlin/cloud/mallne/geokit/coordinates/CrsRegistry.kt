package cloud.mallne.geokit.coordinates

import cloud.mallne.geokit.coordinates.builtin.method.CommonExecutionMethods
import cloud.mallne.geokit.coordinates.execution.*
import cloud.mallne.geokit.coordinates.model.AbstractCoordinate
import cloud.mallne.geokit.coordinates.tokens.WktCrsParser
import cloud.mallne.geokit.coordinates.tokens.ast.expression.*
import cloud.mallne.geokit.coordinates.tokens.ast.expression.Identifier.Companion.epsgId

/**
 * Central registry for all generated CRS definitions and transformations.
 */
data class CrsRegistry(
    val crs: MutableList<CoordinateReferenceSystem> = mutableListOf(),
    val operations: MutableList<Operation> = mutableListOf(),
    val methods: MutableList<ExecutionDispatchMethod> = CommonExecutionMethods.entries.toMutableList(),
    val logger: GeokitLogger? = null,
    private val cachedPipelines: MutableMap<SearchContainer, Pipeline<AbstractCoordinate, AbstractCoordinate>>
) {
    fun ingest(wktCrs: String) {
        when (val node = WktCrsParser(wktCrs)) {
            is CoordinateReferenceSystem -> crs.add(node)
            is Operation -> operations.add(node)
            else -> throw IllegalArgumentException("Not support yet")
        }
    }

    fun clearCaches() {
        cachedPipelines.clear()
    }

    fun compose(
        source: AbstractCoordinate,
        fromEPSG: String,
        toEPSG: String,
    ): PipelineContainer<AbstractCoordinate, AbstractCoordinate> {
        val cached = cachedPipelines.firstNotNullOfOrNull { (pair, pipeline) ->
            if (pair.from.samePointer(fromEPSG) && pair.to.samePointer(toEPSG)) {
                pair to pipeline
            } else null
        }
        if (cached != null) {
            //Premature exit condition for cached Pipelines
            return PipelineContainer(cached.first.from.getDatumUnit()?.let { source.autoConvert(it) } ?: source.toRad(),
                cached.second)
        }

        val search = search(fromEPSG, toEPSG)
        val pipelines = mutableListOf<Pipeline<AbstractCoordinate, AbstractCoordinate>>()

        //non ProjectedCRS to ProjectedCRS
        if (search.projectionFirst) {
            val to = search.operation?.let { if (!search.reverse) it.source else it.target } ?: search.to
            if (to is ProjectedCrs) {
                pipelines.add(
                    CoordinateProjectionTransformer(
                        source = to,
                        methods = methods,
                        logger = logger
                    )
                )
            }
        }

        //ProjectedCRS to non ProjectedCRS
        if (search.deProjectionFirst) {
            val from = search.from
            if (from is ProjectedCrs) {
                pipelines.add(
                    CoordinateProjectionTransformer(
                        source = from,
                        reversed = true,
                        methods = methods,
                        logger = logger
                    )
                )
            }
        }

        when (search.operation) {
            is ConcatenatedOperation -> TODO()
            is CoordinateOperation -> {
                pipelines.add(
                    CoordinateOperationTransformer(
                        operation = search.operation,
                        reversed = search.reverse,
                        methods = methods,
                        logger = logger
                    )
                )
            }

            null -> {}
        }

        //ProjectedCRS to non ProjectedCRS
        if (search.deProjectionAfter) {
            val from = search.operation?.let { if (!search.reverse) it.target else it.source } ?: search.from
            if (from is ProjectedCrs) {
                pipelines.add(
                    CoordinateProjectionTransformer(
                        source = from,
                        reversed = true,
                        methods = methods,
                        logger = logger
                    )
                )
            }
        }

        if (search.projectionAfter) {
            val to = search.to
            if (to is ProjectedCrs) {
                pipelines.add(
                    CoordinateProjectionTransformer(
                        source = to,
                        methods = methods,
                        logger = logger
                    )
                )
            }
        }

        val mulPipeline = MultiPipeline(pipelines) { it }
        cachedPipelines[search] = mulPipeline
        return PipelineContainer(
            data = search.from.getDatumUnit()?.let { source.autoConvert(it) } ?: source.toRad(),
            pipeline = mulPipeline
        )
    }

    private fun search(fromEPSG: String, toEPSG: String): SearchContainer {
        val goodForwardOperations = operations.firstOrNull {
            it.source.identifiers.epsgId?.contains(fromEPSG) == true && it.target.identifiers.epsgId?.contains(toEPSG) == true
        }
        if (goodForwardOperations != null) {
            return SearchContainer(
                operation = goodForwardOperations,
                from = goodForwardOperations.source,
                to = goodForwardOperations.target,
            )
        }
        val goodBackwardsOperations = operations.firstOrNull {
            it.target.identifiers.epsgId?.contains(fromEPSG) == true && it.source.identifiers.epsgId?.contains(toEPSG) == true
        }
        if (goodBackwardsOperations != null) {
            return SearchContainer(
                operation = goodBackwardsOperations,
                reverse = true,
                from = goodBackwardsOperations.target,
                to = goodBackwardsOperations.source,
            )
        }

        val from = findInRepo(fromEPSG)
            ?: throw IllegalArgumentException("The EPSG Code $fromEPSG is not found in any of the Repositories")
        val to = findInRepo(toEPSG)
            ?: throw IllegalArgumentException("The EPSG Code $toEPSG is not found in any of the Repositories")
        if (from == to) {
            //Edgecase the two CRS are the same
            return SearchContainer(
                null,
                from = from,
                to = to,
            )
        }
        val fromPointer = from.epsgPointer()
        val toPointer = to.epsgPointer()

        for (operation in operations) {
            val sourcePointer = operation.source.epsgPointer()
            val targetPointer = operation.target.epsgPointer()

            val foundFromInSource = sourcePointer.any { srcId -> fromPointer.any { it.id == srcId.id } }
            val foundToInTarget = targetPointer.any { srcId -> toPointer.any { it.id == srcId.id } }
            val foundFromInTarget = targetPointer.any { srcId -> fromPointer.any { it.id == srcId.id } }
            val foundToInSource = sourcePointer.any { srcId -> toPointer.any { it.id == srcId.id } }

            if (foundFromInSource && foundToInTarget) {
                //forward
                val source = operation.source
                val target = operation.target
                return SearchContainer(
                    operation = operation,
                    projectionFirst = (from !is ProjectedCrs) && (source is ProjectedCrs),
                    deProjectionFirst = (from is ProjectedCrs) && (source !is ProjectedCrs),
                    projectionAfter = (target !is ProjectedCrs) && (to is ProjectedCrs),
                    deProjectionAfter = (target is ProjectedCrs) && (to !is ProjectedCrs),
                    from = from,
                    to = to,
                )
            } else if (foundFromInTarget && foundToInSource) {
                //backwards
                val source = operation.target
                val target = operation.source
                return SearchContainer(
                    operation = operation,
                    reverse = true,
                    projectionFirst = (from !is ProjectedCrs) && (source is ProjectedCrs),
                    deProjectionFirst = (from is ProjectedCrs) && (source !is ProjectedCrs),
                    projectionAfter = (target !is ProjectedCrs) && (to is ProjectedCrs),
                    deProjectionAfter = (target is ProjectedCrs) && (to !is ProjectedCrs),
                    from = from,
                    to = to,
                )
            } else continue
        }
        throw IllegalArgumentException("Cannot determine a clear Operation path for your Request.")
    }


    private fun CoordinateReferenceSystem.epsgPointer(): List<CRSPointer> {
        val maybeProj = this as? ProjectedCrs
        val add = if (maybeProj != null) {
            maybeProj.base.identifiers.map { CRSPointer(it.epsgId, maybeProj.identifiers.epsgId ?: "") }
        } else listOf()
        return this.identifiers.map { CRSPointer(it.epsgId) } + add
    }

    private fun CoordinateReferenceSystem.samePointer(epsgCode: String): Boolean {
        return this.identifiers.any { it.epsgId.contains(epsgCode, true) }
    }

    data class CRSPointer(
        val id: String,
        val isBaseOf: String? = null,
    )

    private fun findInRepo(epsgCode: String): CoordinateReferenceSystem? =
        (crs.find { it.samePointer(epsgCode) }
            ?: operations.find { it.source.samePointer(epsgCode) }?.source
            ?: operations.find { it.target.samePointer(epsgCode) }?.target)

    data class SearchContainer(
        val operation: Operation?,
        val reverse: Boolean = false,
        val projectionFirst: Boolean = false,
        val deProjectionFirst: Boolean = false,
        val projectionAfter: Boolean = false,
        val deProjectionAfter: Boolean = false,
        val from: CoordinateReferenceSystem,
        val to: CoordinateReferenceSystem,
    )
}