package cloud.mallne.geokit.coordinates.ast.expression

data class DerivingConversion(
    val name: String,
    val method: OperationMethod,
    val parameters: List<AbstractOperationParameter> = emptyList(),
    val identifiers: List<Identifier> = emptyList(),
) : SteppedOperation
