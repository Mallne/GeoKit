package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class StaticVerticalCrs(
    override val name: String,
    val constraints: VerticalConstraints,
    val coordinateSystem: CoordinateSystem,
    val geoidModelIds: List<GeoidModelId> = emptyList(),
    override val usages: List<Usage> = emptyList(),
    override val identifiers: List<Identifier> = emptyList(),
    override val remark: String? = null,
) : VerticalCoordinateReferenceSystem,
    StaticCrsCoordinateMetadata