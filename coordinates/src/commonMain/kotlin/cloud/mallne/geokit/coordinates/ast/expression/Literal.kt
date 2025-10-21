package cloud.mallne.geokit.coordinates.ast.expression

sealed interface Literal : WKTCRSExpression {
    override fun toString(): String
}