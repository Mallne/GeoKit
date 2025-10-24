package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class ParametricCrs(
    override val name: String,
    val datum: ParametricDatum,
    val coordinateSystem: CoordinateSystem,
    override val usages: List<Usage>,
    override val identifiers: List<Identifier>,
    override val remark: String?
) : SingleCoordinateReferenceSystem,
    StaticCrsCoordinateMetadata,
    RootNode
