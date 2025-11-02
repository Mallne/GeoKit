package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class OperationParameter(
    override val name: String,
    val value: Double,
    val unit: ParameterUnit,
    override val identifiers: List<Identifier> = listOf(),
) : AbstractOperationParameter