package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class BaseProjectedCrs(
    val name: String,
    val base: BaseGeodeticCoordinateReferenceSystem,
    val projection: MapProjection,
    val identifier: Identifier? = null,
) : WKTCRSExpression
