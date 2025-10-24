package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class ProjectedCrs(
    override val name: String,
    val base: BaseGeodeticCoordinateReferenceSystem,
    val projection: MapProjection,
    val coordinateSystem: CoordinateSystem,
    override val usages: List<Usage>,
    override val identifiers: List<Identifier>,
    override val remark: String?
) : SingleCoordinateReferenceSystem,
    StaticCrsCoordinateMetadata,
    DynamicCrsCoordinateMetadata,
    RootNode
