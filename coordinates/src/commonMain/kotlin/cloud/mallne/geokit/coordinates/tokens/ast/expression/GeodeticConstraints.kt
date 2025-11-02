package cloud.mallne.geokit.coordinates.tokens.ast.expression

sealed interface GeodeticConstraints : WKTCRSExpression {
    val name: String
    val ellipsoid: Ellipsoid
    val primeMeridian: PrimeMeridian?
    val identifiers: List<Identifier>
}
