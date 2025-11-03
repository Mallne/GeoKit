package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class TemporalCrs(
    override val name: String,
    val datum: TemporalDatum,
    val coordinateSystem: CoordinateSystem,
    override val usages: List<Usage>,
    override val identifiers: List<Identifier>,
    override val remark: String?
) : SingleCoordinateReferenceSystem,
    StaticCrsCoordinateMetadata,
    RootNode {
    override fun getDatumUnit(): WKTUnit? = (coordinateSystem as? SpatialCS)?.unit
}
