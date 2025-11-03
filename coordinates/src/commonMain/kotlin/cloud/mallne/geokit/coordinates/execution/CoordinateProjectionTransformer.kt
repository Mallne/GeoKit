package cloud.mallne.geokit.coordinates.execution

import cloud.mallne.geokit.coordinates.model.AbstractCoordinate
import cloud.mallne.geokit.coordinates.tokens.ast.expression.MapProjection
import cloud.mallne.geokit.coordinates.tokens.ast.expression.ProjectedCrs
import cloud.mallne.geokit.coordinates.tokens.ast.expression.WKTCRSExpression

data class CoordinateProjectionTransformer(
    val source: ProjectedCrs,
    val projection: MapProjection = source.projection,
    override val reversed: Boolean = false,
    private val methods: List<ExecutionDispatchMethod>,
    private val logger: GeokitLogger? = null
) : CoordinatePipeline {

    override val given: WKTCRSExpression = if (!reversed) source.base else source
    override val wanted: WKTCRSExpression = if (reversed) source.base else source

    private val context = GeokitCoordinateConversionContext(
        operation = projection,
        source = given,
        target = wanted,
        logger = logger,
    )
    private val methodDispatcher = MethodDispatcher(methods, context)

    override fun execute(input: AbstractCoordinate): AbstractCoordinate = methodDispatcher.dispatch(
        method = projection.projectionMethod,
        coordinate = input,
        parameters = projection.parameters,
        reverse = reversed,
    )
}