package cloud.mallne.geokit.coordinates.ast.expression

sealed interface CoordinateMetadata : WKTCRSExpression {
    data class StaticCoordinateMetadata(
        val crs: StaticCrsCoordinateMetadata
    ) : CoordinateMetadata

    data class DynamicCoordinateMetadata(
        val crs: DynamicCrsCoordinateMetadata,
        val coordinateEpoch: Double
    ) : CoordinateMetadata
}
