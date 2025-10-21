package cloud.mallne.geokit.coordinates.ast.expression

sealed interface AxisDirection : WKTCRSExpression {
    data class NORTH(val meridian: Meridian? = null) : AxisDirection
    data object NORTHNORTHEAST : AxisDirection
    data object NORTHEAST : AxisDirection
    data object EASTNORTHEAST : AxisDirection
    data object EAST : AxisDirection
    data object EASTSOUTHEAST : AxisDirection
    data object SOUTHEAST : AxisDirection
    data object SOUTHSOUTHEAST : AxisDirection
    data class SOUTH(val meridian: Meridian? = null) : AxisDirection
    data object SOUTHSOUTHWEST : AxisDirection
    data object SOUTHWEST : AxisDirection
    data object WESTSOUTHWEST : AxisDirection
    data object WEST : AxisDirection
    data object WESTNORTHWEST : AxisDirection
    data object NORTHWEST : AxisDirection
    data object NORTHNORTHWEST : AxisDirection
    data object GEOCENTRICX : AxisDirection
    data object GEOCENTRICY : AxisDirection
    data object GEOCENTRICZ : AxisDirection
    data object UP : AxisDirection
    data object DOWN : AxisDirection
    data object FORWARD : AxisDirection
    data object AFT : AxisDirection
    data object PORT : AxisDirection
    data object STARBOARD : AxisDirection
    data class CLOCKWISE(val bearing: Double) : AxisDirection
    data class COUNTERCLOCKWISE(val bearing: Double) : AxisDirection
    data object COLUMNPOSITIVE : AxisDirection
    data object COLUMNNEGATIVE : AxisDirection
    data object ROWPOSITIVE : AxisDirection
    data object ROWNEGATIVE : AxisDirection
    data object DISPLAYRIGHT : AxisDirection
    data object DISPLAYLEFT : AxisDirection
    data object DISPLAYUP : AxisDirection
    data object DISPLAYDOWN : AxisDirection
    data object FUTURE : AxisDirection
    data object PAST : AxisDirection
    data object TOWARDS : AxisDirection
    data object AWAYFROM : AxisDirection
    data object UNSPECIFIED : AxisDirection
}
