package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class StaticGeographicCrs(
    override val name: String,
    val system: GeodeticConstraints,
    val coordinateSystem: CoordinateSystem,
    val definingTransformationIDs: List<DefiningTransformationID> = listOf(),
    override val usages: List<Usage> = listOf(),
    override val identifiers: List<Identifier> = listOf(),
    override val remark: String? = null,
) : GeographicCoordinateReferenceSystem,
    StaticCoordinateReferenceSystem,
    StaticCrsCoordinateMetadata
