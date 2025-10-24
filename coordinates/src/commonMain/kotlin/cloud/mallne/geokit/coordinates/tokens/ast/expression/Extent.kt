package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class Extent(
    val area: Area? = null,
    val bbox: Bbox? = null,
    val verticalExtent: VerticalExtent? = null,
    val temporalExtent: TemporalExtent? = null,
) : WKTCRSExpression
