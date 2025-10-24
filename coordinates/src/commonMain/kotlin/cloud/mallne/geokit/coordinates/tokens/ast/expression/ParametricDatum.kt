package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class ParametricDatum(
    val name: String,
    val anchor: String?,
    val identifiers: List<Identifier> = listOf(),
) : WKTCRSExpression
