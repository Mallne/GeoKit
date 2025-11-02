package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class GeodeticDatumEnsemble(
    override val name: String,
    override val members: List<DatumEnsembleMember>,
    override val ellipsoid: Ellipsoid,
    override val accuracy: Double,
    override val identifiers: List<Identifier> = listOf(),
    override val primeMeridian: PrimeMeridian? = null,
) : GeodeticConstraints,
    DatumEnsemble