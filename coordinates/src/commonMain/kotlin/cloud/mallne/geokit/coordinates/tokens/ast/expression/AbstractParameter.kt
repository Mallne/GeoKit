package cloud.mallne.geokit.coordinates.tokens.ast.expression

sealed interface AbstractParameter: WKTCRSExpression {
    val name: String
    val identifiers: List<Identifier>
}
