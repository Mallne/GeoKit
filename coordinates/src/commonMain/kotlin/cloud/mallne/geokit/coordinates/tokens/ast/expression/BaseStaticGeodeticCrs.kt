package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class BaseStaticGeodeticCrs(
    val name: String,
    val constraints: GeodeticConstraints,
    val unit: AngleUnit? = null,
    val identifiers: List<Identifier> = listOf(),
) : BaseGeodeticCoordinateReferenceSystem,
    BaseStaticCoordinateReferenceSystem