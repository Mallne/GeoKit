package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class GeoidModelId(
    val name: String,
    val identifier: Identifier? = null,
) : WKTCRSExpression
