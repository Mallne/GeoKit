package cloud.mallne.geokit.coordinates.tokens.ast.expression

/** Represents a numeric literal in WKT. */
data class NumberLiteral(val value: Double) : Literal {
    override fun toString(): String = value.toShortString()
}

private fun Double.toShortString(): String {
    val isInt = this == toInt().toDouble()
    return if (isInt) toInt().toString() else toString()
}
