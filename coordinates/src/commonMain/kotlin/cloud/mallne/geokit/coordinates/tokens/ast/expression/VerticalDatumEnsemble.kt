package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class VerticalDatumEnsemble(
    override val name: String,
    override val members: List<DatumEnsembleMember>,
    override val accuracy: Double,
    override val identifiers: List<Identifier> = listOf(),
) : DatumEnsemble,
    VerticalConstraints