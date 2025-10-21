package cloud.mallne.geokit.coordinates.ast.expression

sealed interface ScopeExtentIdentifierRemark : WKTCRSExpression {
    val usages: List<Usage>
    val identifiers: List<Identifier>
    val remark: String?
}