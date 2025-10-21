package cloud.mallne.geokit.coordinates.ast.expression

sealed interface DatumEnsemble : WKTCRSExpression {
    val name: String
    val members: List<DatumEnsembleMember>
    val accuracy: Double
    val identifiers: List<Identifier>
}
