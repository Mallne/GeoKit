package cloud.mallne.geokit.coordinates.ast.expression

data class IrmLongitude(
    val longitude: Double,
    val unit: AngleUnit? = null
) : WKTCRSExpression
