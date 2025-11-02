package cloud.mallne.geokit.coordinates.model

import cloud.mallne.geokit.coordinates.tokens.ast.expression.WKTUnit

sealed interface AbstractCoordinate {
    val latitude: Double
    val longitude: Double
    val altitude: Double?

    fun is3D(): Boolean = altitude != null

    val x: Double get() = latitude
    val y: Double get() = longitude
    val z: Double? get() = altitude

    fun toDeg(): AbstractCoordinate
    fun toRad(): AbstractCoordinate
    fun autoConvert(thisUnit: WKTUnit): AbstractCoordinate
}
