package cloud.mallne.geokit.coordinates.execution

import cloud.mallne.geokit.coordinates.model.AbstractCoordinate
import cloud.mallne.geokit.coordinates.tokens.ast.expression.CoordinateOperation
import cloud.mallne.geokit.coordinates.tokens.ast.expression.CoordinateReferenceSystem

data class CoordinateOperationTransformer(
    override val operation: CoordinateOperation,
    override val reversed: Boolean = false,
    private val methods: List<ExecutionDispatchMethod>,
    private val logger: GeokitLogger? = null
) : CoordinateOperationPipeline<CoordinateOperation> {
    override val given: CoordinateReferenceSystem = if (!reversed) operation.source else operation.target
    override val wanted: CoordinateReferenceSystem = if (reversed) operation.source else operation.target

    private val context = GeokitCoordinateConversionContext(
        operation = operation,
        source = given,
        target = wanted,
        logger = logger,
    )
    private val methodDispatcher = MethodDispatcher(methods, context)

    override fun execute(input: AbstractCoordinate): AbstractCoordinate = methodDispatcher.dispatch(
        method = operation.method,
        coordinate = input,
        parameters = operation.parameters,
        reverse = reversed,
    )
}