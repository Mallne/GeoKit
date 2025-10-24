package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class VerticalReferenceFrame(
    val name: String,
    val anchor: String?,
    val epoch: Double?,
    val identifiers: List<Identifier> = listOf(),
) : VerticalConstraints