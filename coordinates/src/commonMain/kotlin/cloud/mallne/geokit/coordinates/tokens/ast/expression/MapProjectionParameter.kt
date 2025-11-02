package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class MapProjectionParameter(
    override val name: String,
    val value: Double,
    val unit: MapProjectionParameterUnit? = null,
    override val identifiers: List<Identifier> = listOf(),
) : AbstractParameter
