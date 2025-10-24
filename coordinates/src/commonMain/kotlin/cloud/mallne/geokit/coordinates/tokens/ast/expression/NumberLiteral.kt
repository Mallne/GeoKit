package cloud.mallne.geokit.coordinates.tokens.ast.expression

/** Represents a numeric literal in WKT. */
data class NumberLiteral(val value: Double) : Literal {
    override fun toString(): String = value.toString()
}