package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class DatumEnsembleMember(
    val name: String,
    val identifiers: List<Identifier> = listOf(),
) : WKTCRSExpression
