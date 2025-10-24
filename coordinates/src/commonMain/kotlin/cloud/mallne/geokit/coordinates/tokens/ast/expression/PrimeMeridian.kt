package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class PrimeMeridian(
    val name: String,
    val irmLongitude: IrmLongitude,
    val identifiers: List<Identifier> = listOf(),
) : WKTCRSExpression
