package cloud.mallne.geokit.coordinates.tokens.ast.expression

sealed interface BaseGeodeticCoordinateReferenceSystem :
    BaseCoordinateReferenceSystem {
    val name: String
    val constraints: GeodeticConstraints
    val unit: AngleUnit?
    val identifiers: List<Identifier>
}