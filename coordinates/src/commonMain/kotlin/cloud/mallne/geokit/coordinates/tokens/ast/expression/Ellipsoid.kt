package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class Ellipsoid(
    val name: String,
    val semiMajorAxis: Double,
    val inverseFlattening: Double,
    val unit: LengthUnit? = null,
    val identifiers: List<Identifier> = listOf(),
) : WKTCRSExpression
