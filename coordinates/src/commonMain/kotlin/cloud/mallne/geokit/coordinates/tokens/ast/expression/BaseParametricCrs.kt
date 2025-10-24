package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class BaseParametricCrs(
    val name: String,
    val datum: ParametricDatum,
    val identifiers: List<Identifier> = listOf(),
) : WKTCRSExpression
