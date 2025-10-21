package cloud.mallne.geokit.coordinates.ast.expression

data class DefiningTransformationID(
    val name: String,
    val identifier: Identifier? = null,
) : WKTCRSExpression
