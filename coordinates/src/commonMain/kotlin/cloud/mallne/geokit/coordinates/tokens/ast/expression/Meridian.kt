package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class Meridian(
    val number: Double,
    val unit: AngleUnit
) : WKTCRSExpression
