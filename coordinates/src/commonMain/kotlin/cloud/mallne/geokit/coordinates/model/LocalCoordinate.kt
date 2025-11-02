package cloud.mallne.geokit.coordinates.model

import cloud.mallne.geokit.coordinates.execution.CalculationExtensions.toDegrees
import cloud.mallne.geokit.coordinates.execution.CalculationExtensions.toRadians
import cloud.mallne.geokit.coordinates.tokens.ast.expression.CoordinateReferenceSystem
import cloud.mallne.geokit.coordinates.tokens.ast.expression.WKTUnit

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

    override fun toDeg() = copy(
        latitude = latitude.toDegrees(),
        longitude = longitude.toDegrees(),
        altitude = altitude?.toDegrees(),
    )

    override fun toRad() = copy(
        latitude = latitude.toRadians(),
        longitude = longitude.toRadians(),
        altitude = altitude?.toRadians()
    )

    override fun autoConvert(thisUnit: WKTUnit) = copy(
        latitude = latitude * thisUnit.conversionFactor,
        longitude = longitude * thisUnit.conversionFactor,
        altitude = altitude?.let { it * thisUnit.conversionFactor },
    )
}