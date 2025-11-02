package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class CoordinateOperation(
    override val name: String,
    override val version: String? = null,
    override val source: CoordinateReferenceSystem,
    override val target: CoordinateReferenceSystem,
    val method: OperationMethod,
    val parameters: List<AbstractOperationParameter> = listOf(),
    val interpolation: CoordinateReferenceSystem? = null,
    override val accuracy: Double? = null,
    override val usages: List<Usage> = listOf(),
    override val identifiers: List<Identifier> = listOf(),
    override val remark: String? = null,
) : SteppedOperation,
    ScopeExtentIdentifierRemark,
    RootNode,
    Operation