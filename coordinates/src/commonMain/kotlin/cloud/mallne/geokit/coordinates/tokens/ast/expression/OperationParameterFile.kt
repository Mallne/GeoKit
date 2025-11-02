package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class OperationParameterFile(
    override val name: String,
    val filename: String,
    override val identifiers: List<Identifier> = listOf(),
) : AbstractOperationParameter