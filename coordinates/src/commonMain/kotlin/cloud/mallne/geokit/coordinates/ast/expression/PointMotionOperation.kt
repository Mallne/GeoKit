package cloud.mallne.geokit.coordinates.ast.expression

data class PointMotionOperation(
    val name: String,
    val version: String? = null,
    val source: CoordinateReferenceSystem,
    val method: OperationMethod,
    val parameters: List<AbstractOperationParameter> = listOf(),
    val accuracy: Double? = null,
    override val usages: List<Usage>,
    override val identifiers: List<Identifier>,
    override val remark: String?
) : SteppedOperation, ScopeExtentIdentifierRemark