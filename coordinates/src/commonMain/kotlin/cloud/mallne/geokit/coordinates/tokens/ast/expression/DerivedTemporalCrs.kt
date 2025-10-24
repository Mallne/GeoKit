package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class DerivedTemporalCrs(
    override val name: String,
    val base: BaseTemporalCrs,
    val derivingConversion: DerivingConversion,
    val cs: CoordinateSystem,
    override val usages: List<Usage> = listOf(),
    override val identifiers: List<Identifier> = listOf(),
    override val remark: String? = null,
) : SingleCoordinateReferenceSystem,
    StaticCrsCoordinateMetadata,
    RootNode
