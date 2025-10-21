package cloud.mallne.geokit.coordinates.ast.expression

data class Usage(
    val scope: String,
    val extent: Extent
) : WKTCRSExpression
