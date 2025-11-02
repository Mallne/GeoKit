package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class StaticGeodeticCrs(
    override val name: String,
    override val system: GeodeticConstraints,
    override val coordinateSystem: CoordinateSystem,
    override val definingTransformationIDs: List<DefiningTransformationID> = listOf(),
    override val usages: List<Usage> = listOf(),
    override val identifiers: List<Identifier> = listOf(),
    override val remark: String? = null,
) : GeodeticCoordinateReferenceSystem,
    StaticCoordinateReferenceSystem,
    StaticCrsCoordinateMetadata
