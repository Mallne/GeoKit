package cloud.mallne.geokit.coordinates.ast.expression

import cloud.mallne.geokit.Boundary

data class Bbox(
    val boundary: Boundary,
) : ExtendStructure {
    constructor(
        lowerLeftLatitude: Double,
        lowerLeftLongitude: Double,
        upperRightLatitude: Double,
        upperRightLongitude: Double
    ) : this(Boundary(upperRightLatitude, upperRightLongitude, lowerLeftLatitude, lowerLeftLongitude))
}