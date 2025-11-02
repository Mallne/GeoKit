package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class BaseStaticGeodeticCrs(
    override val name: String,
    override val constraints: GeodeticConstraints,
    override val unit: AngleUnit? = null,
    override val identifiers: List<Identifier> = listOf(),
) : BaseGeodeticCoordinateReferenceSystem,
    BaseStaticCoordinateReferenceSystem