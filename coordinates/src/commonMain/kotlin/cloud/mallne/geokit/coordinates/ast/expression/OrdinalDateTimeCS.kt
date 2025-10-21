package cloud.mallne.geokit.coordinates.ast.expression

data class OrdinalDateTimeCS(
    val type: OrdinalDateTimeCSType,
    override val dimension: Dimension,
    override val identifiers: List<Identifier> = listOf(),
    val axis: List<OrdinalDateTimeAxis>
) : CoordinateSystem