package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class ConcatenatedOperation(
    override val name: String,
    override val version: String? = null,
    override val source: CoordinateReferenceSystem,
    override val target: CoordinateReferenceSystem,
    val steps: List<SteppedOperation>,
    override val accuracy: Double? = null,
    override val usages: List<Usage> = emptyList(),
    override val identifiers: List<Identifier> = emptyList(),
    override val remark: String? = null,
) : WKTCRSExpression,
    ScopeExtentIdentifierRemark,
    RootNode,
    Operation
