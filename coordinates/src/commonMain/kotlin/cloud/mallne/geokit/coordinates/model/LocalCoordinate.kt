package cloud.mallne.geokit.coordinates.model

import cloud.mallne.geokit.coordinates.tokens.ast.expression.CoordinateReferenceSystem

data class LocalCoordinate(
    override val latitude: Double,
    override val longitude: Double,
    override val altitude: Double? = null
) : AbstractCoordinate {
    fun toCoordinate(crs: CoordinateReferenceSystem) = Coordinate(
        latitude = latitude,
        longitude = longitude,
        altitude = altitude,
        crs = crs,
    )
}