package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class StaticGeodeticCrs(
    override val name: String,
    val system: GeodeticConstraints,
    val coordinateSystem: CoordinateSystem,
    val definingTransformationIDs: List<DefiningTransformationID> = listOf(),
    override val usages: List<Usage> = listOf(),
    override val identifiers: List<Identifier> = listOf(),
    override val remark: String? = null,
) : GeodeticCoordinateReferenceSystem,
    StaticCoordinateReferenceSystem,
    StaticCrsCoordinateMetadata
