package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class EngineeringCrs(
    override val name: String,
    val datum: EngineeringDatum,
    val coordinateSystem: CoordinateSystem,
    override val usages: List<Usage>,
    override val identifiers: List<Identifier>,
    override val remark: String?
) : SingleCoordinateReferenceSystem,
    StaticCrsCoordinateMetadata,
    RootNode {
    override fun getDatumUnit(): WKTUnit? = (coordinateSystem as? SpatialCS)?.unit
}
