package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class OperationParameterFile(
    val name: String,
    val filename: String,
    val identifiers: List<Identifier> = listOf(),
) : AbstractOperationParameter