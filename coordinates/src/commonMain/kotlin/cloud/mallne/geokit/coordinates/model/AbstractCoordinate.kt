package cloud.mallne.geokit.coordinates.model

import cloud.mallne.geokit.Vertex
import cloud.mallne.geokit.coordinates.tokens.ast.expression.WKTUnit

sealed interface AbstractCoordinate {
    val latitude: Double
    val longitude: Double
    val altitude: Double?

    fun is3D(): Boolean = altitude != null

    val x: Double get() = latitude
    val y: Double get() = longitude
    val z: Double? get() = altitude

    val phi: Double get() = latitude
    val lambda: Double get() = longitude
    val h: Double? get() = altitude

    val northing: Double get() = latitude
    val easting: Double get() = longitude

    fun toDeg(): AbstractCoordinate
    fun toRad(): AbstractCoordinate
    fun autoConvert(thisUnit: WKTUnit): AbstractCoordinate


    fun toVertex() = Vertex(latitude, longitude)
    fun Vertex.toCoordinate() = LocalCoordinate(latitude, longitude)
}
