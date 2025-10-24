package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class TemporalCountMeasureCS(
    val type: TemporalCountMeasureCSType,
    override val dimension: Dimension,
    override val identifiers: List<Identifier> = listOf(),
    val axis: TemporalCountMeasureAxis
) : CoordinateSystem