package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class AbridgedTransformationParameter(
    override val name: String,
    val value: Double,
    override val identifiers: List<Identifier> = listOf(),
) : AbstractOperationParameter