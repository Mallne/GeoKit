package cloud.mallne.geokit.coordinates.tokens.ast.expression

sealed interface WKTUnit : WKTCRSExpression {
    val unitName: String
    val conversionFactor: Double
    val identifiers: List<Identifier>
}
