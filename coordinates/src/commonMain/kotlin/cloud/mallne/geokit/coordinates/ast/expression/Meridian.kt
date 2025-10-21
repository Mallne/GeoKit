package cloud.mallne.geokit.coordinates.ast.expression

data class Meridian(
    val number: Double,
    val unit: AngleUnit
) : WKTCRSExpression
