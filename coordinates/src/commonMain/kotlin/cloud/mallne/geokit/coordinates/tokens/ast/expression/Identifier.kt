package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class Identifier(
    val authorityName: String,
    val uid: Literal,
    val version: Literal? = null,
    val citation: String? = null,
    val uri: String? = null,
) : WKTCRSExpression
