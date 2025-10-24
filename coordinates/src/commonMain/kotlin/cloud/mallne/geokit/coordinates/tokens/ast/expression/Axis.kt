package cloud.mallne.geokit.coordinates.tokens.ast.expression

sealed interface Axis : WKTCRSExpression {
    val name: String
    val direction: AxisDirection
    val order: Int?
    val range: AxisRange?
    val identifiers: List<Identifier>
}