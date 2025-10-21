package cloud.mallne.geokit.coordinates.ast.expression

data class StaticGeodeticCrs(
    override val name: String,
    val system: GeodeticSystem,
    val coordinateSystem: CoordinateSystem,
    val definingTransformationIDs: List<DefiningTransformationID> = listOf(),
    override val usages: List<Usage> = listOf(),
    override val identifiers: List<Identifier> = listOf(),
    override val remark: String? = null,
) : GeodeticCoordinateReferenceSystem, StaticCoordinateReferenceSystem, ScopeExtentIdentifierRemark
