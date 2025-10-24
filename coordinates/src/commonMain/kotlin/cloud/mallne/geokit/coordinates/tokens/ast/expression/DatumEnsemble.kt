package cloud.mallne.geokit.coordinates.tokens.ast.expression

sealed interface DatumEnsemble : RootNode {
    val name: String
    val members: List<DatumEnsembleMember>
    val accuracy: Double
    val identifiers: List<Identifier>
}
