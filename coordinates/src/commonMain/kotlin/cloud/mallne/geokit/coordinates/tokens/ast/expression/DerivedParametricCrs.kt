package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class DerivedParametricCrs(
    override val name: String,
    val base: BaseParametricCrs,
    val derivingConversion: DerivingConversion,
    val cs: CoordinateSystem,
    override val usages: List<Usage> = listOf(),
    override val identifiers: List<Identifier> = listOf(),
    override val remark: String? = null,
) : SingleCoordinateReferenceSystem,
    StaticCrsCoordinateMetadata,
    RootNode
