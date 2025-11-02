package cloud.mallne.geokit.coordinates.model

import cloud.mallne.geokit.coordinates.tokens.ast.expression.CoordinateReferenceSystem

data class Coordinate(
    override val latitude: Double,
    override val longitude: Double,
    override val altitude: Double? = null,
    val crs: CoordinateReferenceSystem,
): AbstractCoordinate
