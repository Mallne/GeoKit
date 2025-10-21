package cloud.mallne.geokit.coordinates.ast.expression

data class TemporalCountMeasureCS(
    val type: TemporalCountMeasureCSType,
    override val dimension: Dimension,
    override val identifiers: List<Identifier> = listOf(),

    ) : CoordinateSystem