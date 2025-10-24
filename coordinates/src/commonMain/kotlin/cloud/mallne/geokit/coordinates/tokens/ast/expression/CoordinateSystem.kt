package cloud.mallne.geokit.coordinates.tokens.ast.expression

sealed interface CoordinateSystem : WKTCRSExpression {
    val dimension: Dimension
    val identifiers: List<Identifier>
}