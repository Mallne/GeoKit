package cloud.mallne.geokit.coordinates.ast.expression

data class MapProjectionParameter(
    val name: String,
    val value: Double,
    val unit: MapProjectionParameterUnit? = null,
    val identifiers: List<Identifier> = listOf(),
) : WKTCRSExpression
