package cloud.mallne.geokit.coordinates.ast.expression

data class TemporalCountMeasureAxis(
    override val name: String,
    override val direction: AxisDirection,
    override val order: Int? = null,
    val unit: TimeUnit? = null,
    override val range: AxisRange? = null,
    override val identifiers: List<Identifier> = listOf(),
) : Axis
