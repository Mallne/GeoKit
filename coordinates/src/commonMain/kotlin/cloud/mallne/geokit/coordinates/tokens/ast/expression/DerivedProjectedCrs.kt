package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class DerivedProjectedCrs(
    override val name: String,
    val base: BaseProjectedCrs,
    val derivingConversion: DerivingConversion,
    val coordinateSystem: CoordinateSystem,
    override val usages: List<Usage> = emptyList(),
    override val identifiers: List<Identifier> = listOf(),
    override val remark: String? = null,
) : SingleCoordinateReferenceSystem,
    StaticCrsCoordinateMetadata,
    DynamicCrsCoordinateMetadata,
    RootNode
