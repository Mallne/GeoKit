package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class OperationMethod(
    val name: String,
    val identifiers: List<Identifier> = emptyList(),
) : WKTCRSExpression
