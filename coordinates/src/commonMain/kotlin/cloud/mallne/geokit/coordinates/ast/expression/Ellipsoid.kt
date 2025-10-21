package cloud.mallne.geokit.coordinates.ast.expression

data class Ellipsoid(
    val name: String,
    val semiMajorAxis: Double,
    val inverseFlattening: Double,
    val unit: LengthUnit? = null,
    val identifiers: List<Identifier> = listOf(),
) : WKTCRSExpression
