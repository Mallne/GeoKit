package cloud.mallne.geokit.coordinates.tokens.ast.expression

/** Represents a quoted text literal in WKT. */
data class TextLiteral(val text: String) : Literal {
    override fun toString(): String = text
}