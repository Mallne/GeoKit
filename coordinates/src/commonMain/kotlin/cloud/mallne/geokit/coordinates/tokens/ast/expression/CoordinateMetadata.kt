package cloud.mallne.geokit.coordinates.tokens.ast.expression

sealed interface CoordinateMetadata : RootNode {
    data class StaticCoordinateMetadata(
        val crs: StaticCrsCoordinateMetadata
    ) : CoordinateMetadata

    data class DynamicCoordinateMetadata(
        val crs: DynamicCrsCoordinateMetadata,
        val coordinateEpoch: Double
    ) : CoordinateMetadata
}
