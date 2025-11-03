package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class CompoundCrs(
    override val name: String,
    val systems: List<SingleCoordinateReferenceSystem>,
    override val usages: List<Usage> = emptyList(),
    override val identifiers: List<Identifier> = emptyList(),
    override val remark: String? = null
) : CoordinateReferenceSystem,
    StaticCrsCoordinateMetadata,
    DynamicCrsCoordinateMetadata,
    RootNode {
    override fun getDatumUnit(): WKTUnit? = null

}
