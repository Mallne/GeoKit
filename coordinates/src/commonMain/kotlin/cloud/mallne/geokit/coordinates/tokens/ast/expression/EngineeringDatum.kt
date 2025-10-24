package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class EngineeringDatum(
    val name: String,
    val anchor: String?,
    val identifiers: List<Identifier> = listOf(),
) : WKTCRSExpression
