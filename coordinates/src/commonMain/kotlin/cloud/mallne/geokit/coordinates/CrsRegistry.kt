package cloud.mallne.geokit.coordinates

import cloud.mallne.geokit.Vertex
import cloud.mallne.geokit.coordinates.builtin.method.CommonExecutionMethods
import cloud.mallne.geokit.coordinates.execution.CoordinateOperationPipeline
import cloud.mallne.geokit.coordinates.execution.ExecutionDispatchMethod
import cloud.mallne.geokit.coordinates.execution.GeokitLogger
import cloud.mallne.geokit.coordinates.execution.PipelineContainer
import cloud.mallne.geokit.coordinates.model.AbstractCoordinate
import cloud.mallne.geokit.coordinates.model.Coordinate
import cloud.mallne.geokit.coordinates.tokens.WktCrsParser
import cloud.mallne.geokit.coordinates.tokens.ast.expression.CoordinateReferenceSystem
import cloud.mallne.geokit.coordinates.tokens.ast.expression.Operation
import cloud.mallne.geokit.coordinates.tokens.ast.expression.OperationMethod

/**
 * Central registry for all generated CRS definitions and transformations.
 */
data class CrsRegistry(
    val crs: MutableList<CoordinateReferenceSystem> = mutableListOf(),
    val operations: MutableList<Operation> = mutableListOf(),
    val methods: MutableList<ExecutionDispatchMethod> = CommonExecutionMethods.entries.toMutableList(),
    val logger: GeokitLogger? = null,
) {
    fun ingest(wktCrs: String) {
        when (val node = WktCrsParser(wktCrs)) {
            is CoordinateReferenceSystem -> crs.add(node)
            is Operation -> operations.add(node)
            else -> throw IllegalArgumentException("Not support yet")
        }
    }

    //fun compose(
    //    source: AbstractCoordinate,
    //    to: CoordinateReferenceSystem,
    //    with: Operation? = null
    //): PipelineContainer<AbstractCoordinate, Coordinate> {
    //    val op = with ?: if (source is Coordinate) {operations.find { it.target. }}
    //    return CoordinateOperationPipeline(
    //        source = source.system,
    //        to = to,
    //        coordinate = source.vertex,
    //        operation = op
    //    )
    //}
}