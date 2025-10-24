package cloud.mallne.geokit.coordinates.tokens.ast.expression

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

    constructor(vararg positions: Double) : this(positions[0], positions[1], positions[2], positions[3])
}