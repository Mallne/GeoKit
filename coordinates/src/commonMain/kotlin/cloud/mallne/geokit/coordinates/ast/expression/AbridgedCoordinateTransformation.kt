package cloud.mallne.geokit.coordinates.ast.expression

data class AbridgedCoordinateTransformation(
    val name: String,
    val version: String? = null,
    val method: OperationMethod,
    val parameter: List<AbstractOperationParameter> = emptyList(),
    override val usages: List<Usage> = emptyList(),
    override val identifiers: List<Identifier> = emptyList(),
    override val remark: String? = null,
) : ScopeExtentIdentifierRemark {
    init {
        require(parameter.find { it is OperationParameter } == null) {
            "Only abridgedTransformationParameter or operationParameterFile are allowed"
        }
    }
}
