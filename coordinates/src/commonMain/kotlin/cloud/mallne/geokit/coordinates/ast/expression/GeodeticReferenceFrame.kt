package cloud.mallne.geokit.coordinates.ast.expression

data class GeodeticReferenceFrame(
    val name: String,
    val ellipsoid: Ellipsoid,
    val anchor: String? = null,
    val anchorEpoch: Double? = null,
    val identifiers: List<Identifier> = listOf(),
    val primeMeridian: PrimeMeridian? = null,
) : GeodeticSystem