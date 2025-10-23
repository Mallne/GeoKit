package cloud.mallne.geokit.coordinates.ast.expression

data class CoordinateOperation(
    val name: String,
    val version: String? = null,
    val source: CoordinateReferenceSystem,
    val target: CoordinateReferenceSystem,
    val method: OperationMethod,
    val parameters: List<AbstractOperationParameter> = listOf(),
    val interpolation: CoordinateReferenceSystem? = null,
    val accuracy: Double? = null,
    override val usages: List<Usage> = listOf(),
    override val identifiers: List<Identifier> = listOf(),
    override val remark: String? = null,
) : SteppedOperation, ScopeExtentIdentifierRemark