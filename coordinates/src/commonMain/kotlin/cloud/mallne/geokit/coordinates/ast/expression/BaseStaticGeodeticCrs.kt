package cloud.mallne.geokit.coordinates.ast.expression

data class BaseStaticGeodeticCrs(
    val name: String,
    val datumEnsemble: GeodeticSystem,
    val unit: AngleUnit? = null,
    val identifiers: List<Identifier> = listOf(),
) : BaseGeodeticCoordinateReferenceSystem, BaseStaticCoordinateReferenceSystem