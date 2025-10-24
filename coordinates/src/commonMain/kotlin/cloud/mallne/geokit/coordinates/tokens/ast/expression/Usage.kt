package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class Usage(
    val scope: String,
    val extent: Extent
) : WKTCRSExpression
