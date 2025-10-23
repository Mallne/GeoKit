package cloud.mallne.geokit.coordinates.ast.expression

data class MapProjectionMethod(
    val name: String,
    val identifiers: List<Identifier> = listOf(),
) : WKTCRSExpression
