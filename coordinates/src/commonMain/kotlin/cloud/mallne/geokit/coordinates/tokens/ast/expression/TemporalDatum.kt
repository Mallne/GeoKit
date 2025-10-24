package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class TemporalDatum(
    val name: String,
    val calendar: String? = null,
    val origin: TemporalOrigin? = null,
    val identifiers: List<Identifier> = listOf(),
) : WKTCRSExpression
