package cloud.mallne.geokit.coordinates.ast.expression

sealed interface CoordinateReferenceSystem : AbstractCoordinateReferenceSystem {
    val name: String
}