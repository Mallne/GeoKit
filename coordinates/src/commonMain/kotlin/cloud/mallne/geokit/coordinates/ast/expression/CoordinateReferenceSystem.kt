package cloud.mallne.geokit.coordinates.ast.expression

sealed interface CoordinateReferenceSystem : WKTCRSExpression {
    val name: String
}