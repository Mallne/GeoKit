package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class ConcatenatedOperation(
    val name: String,
    val version: String? = null,
    val source: CoordinateReferenceSystem,
    val target: CoordinateReferenceSystem,
    val steps: List<SteppedOperation>,
    val accuracy: Double? = null,
    override val usages: List<Usage> = emptyList(),
    override val identifiers: List<Identifier> = emptyList(),
    override val remark: String? = null,
) : WKTCRSExpression,
    ScopeExtentIdentifierRemark,
    RootNode,
    Operation
