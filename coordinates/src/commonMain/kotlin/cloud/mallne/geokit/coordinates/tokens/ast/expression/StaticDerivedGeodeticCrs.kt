package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class StaticDerivedGeodeticCrs(
    override val name: String,
    val system: BaseStaticCoordinateReferenceSystem,
    val derivingConversion: DerivingConversion,
    val coordinateSystem: CoordinateSystem,
    override val usages: List<Usage> = listOf(),
    override val identifiers: List<Identifier> = listOf(),
    override val remark: String? = null,
) : DerivedGeodeticCoordinateReferenceSystem,
    StaticCoordinateReferenceSystem
