package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class OrdinalDateTimeAxis(
    override val name: String,
    override val direction: AxisDirection,
    override val order: Int? = null,
    override val range: AxisRange? = null,
    override val identifiers: List<Identifier> = listOf(),
) : Axis
