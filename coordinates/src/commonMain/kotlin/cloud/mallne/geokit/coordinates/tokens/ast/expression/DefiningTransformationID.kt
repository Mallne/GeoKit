package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class DefiningTransformationID(
    val name: String,
    val identifier: Identifier? = null,
) : WKTCRSExpression
