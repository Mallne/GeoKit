package cloud.mallne.geokit.coordinates.tokens.ast.expression

sealed interface Literal : WKTCRSExpression {
    override fun toString(): String
}