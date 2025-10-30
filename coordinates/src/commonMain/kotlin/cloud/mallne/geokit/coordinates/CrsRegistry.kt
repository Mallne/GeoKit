package cloud.mallne.geokit.coordinates

import cloud.mallne.geokit.Vertex
import cloud.mallne.geokit.coordinates.execution.CoordinateOperationPipeline
import cloud.mallne.geokit.coordinates.execution.PipelineContainer
import cloud.mallne.geokit.coordinates.model.Coordinate
import cloud.mallne.geokit.coordinates.tokens.WktCrsParser
import cloud.mallne.geokit.coordinates.tokens.ast.expression.CoordinateReferenceSystem
import cloud.mallne.geokit.coordinates.tokens.ast.expression.Operation

/**
 * Central registry for all generated CRS definitions and transformations.
 */
data class CrsRegistry(
    val crs: MutableList<CoordinateReferenceSystem> = mutableListOf(),
    val operations: MutableList<Operation> = mutableListOf(),
) {
    fun ingest(wktCrs: String) {
        when (val node = WktCrsParser(wktCrs)) {
            is CoordinateReferenceSystem -> crs.add(node)
            is Operation -> operations.add(node)
            else -> throw IllegalArgumentException("Not support yet")
        }
    }

    fun compose(
        source: Coordinate,
        to: CoordinateReferenceSystem,
        with: Operation? = null
    ): PipelineContainer<Vertex, Coordinate> {
        val op = with ?: TODO("Find best operation for transform")
        return CoordinateOperationPipeline(
            source = source.system,
            to = to,
            coordinate = source.vertex,
            operation = op
        )
    }
}