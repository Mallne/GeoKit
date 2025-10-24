package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class OrdinalDateTimeCS(
    val type: OrdinalDateTimeCSType,
    override val dimension: Dimension,
    override val identifiers: List<Identifier> = listOf(),
    val axis: List<OrdinalDateTimeAxis>
) : CoordinateSystem