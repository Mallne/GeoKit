package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class BaseTemporalCrs(
    val name: String,
    val datum: TemporalDatum,
    val identifiers: List<Identifier> = listOf(),
) : WKTCRSExpression
