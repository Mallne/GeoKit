package cloud.mallne.geokit.coordinates.ast.expression

sealed interface WKTUnit : WKTCRSExpression {
    val unitName: String
    val conversionFactor: Double
    val identifiers: List<Identifier>
}
