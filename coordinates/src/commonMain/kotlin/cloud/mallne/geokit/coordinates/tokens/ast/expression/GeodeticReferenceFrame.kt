package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class GeodeticReferenceFrame(
    override val name: String,
    override val ellipsoid: Ellipsoid,
    val anchor: String? = null,
    val anchorEpoch: Double? = null,
    override val identifiers: List<Identifier> = listOf(),
    override val primeMeridian: PrimeMeridian? = null,
) : GeodeticConstraints