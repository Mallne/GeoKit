package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class BaseStaticGeographicCrs(
    val name: String,
    val datumEnsemble: GeodeticConstraints,
    val unit: AngleUnit? = null,
    val identifiers: List<Identifier> = listOf(),
) : BaseGeodeticCoordinateReferenceSystem,
    BaseStaticCoordinateReferenceSystem