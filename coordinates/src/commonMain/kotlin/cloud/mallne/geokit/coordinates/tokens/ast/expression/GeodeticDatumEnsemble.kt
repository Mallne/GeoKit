package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class GeodeticDatumEnsemble(
    override val name: String,
    override val members: List<DatumEnsembleMember>,
    val ellipsoid: Ellipsoid,
    override val accuracy: Double,
    override val identifiers: List<Identifier> = listOf(),
    val primeMeridian: PrimeMeridian? = null,
) : GeodeticConstraints,
    DatumEnsemble