package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class AbridgedTransformationParameter(
    val name: String,
    val value: Double,
    val identifiers: List<Identifier> = listOf(),
) : AbstractOperationParameter