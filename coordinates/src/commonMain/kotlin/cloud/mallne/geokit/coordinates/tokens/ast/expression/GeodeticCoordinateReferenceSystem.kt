package cloud.mallne.geokit.coordinates.tokens.ast.expression

sealed interface GeodeticCoordinateReferenceSystem :
    SingleCoordinateReferenceSystem,
    RootNode {
    val system: GeodeticConstraints
    val coordinateSystem: CoordinateSystem
    val definingTransformationIDs: List<DefiningTransformationID>
}