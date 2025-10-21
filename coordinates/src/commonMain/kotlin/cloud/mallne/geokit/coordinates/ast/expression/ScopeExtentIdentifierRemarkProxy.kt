package cloud.mallne.geokit.coordinates.ast.expression

data class ScopeExtentIdentifierRemarkProxy(
    override val usages: List<Usage> = listOf(),
    override val identifiers: List<Identifier> = listOf(),
    override val remark: String? = null,
) : ScopeExtentIdentifierRemark