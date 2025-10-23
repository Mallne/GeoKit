package cloud.mallne.geokit.coordinates.ast.expression

data class OperationParameter(
    val name: String,
    val value: Double,
    val unit: ParameterUnit,
    val identifiers: List<Identifier> = listOf(),
) : AbstractOperationParameter