package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class StaticBaseVerticalCrs(
    val name: String,
    val constraints: VerticalConstraints,
    val identifiers: List<Identifier> = listOf(),
) : BaseVerticalCrs