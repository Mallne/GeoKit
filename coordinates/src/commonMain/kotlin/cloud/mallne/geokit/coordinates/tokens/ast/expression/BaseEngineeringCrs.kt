package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class BaseEngineeringCrs(
    val name: String,
    val datum: EngineeringDatum,
    val identifiers: List<Identifier> = listOf(),
) : WKTCRSExpression
