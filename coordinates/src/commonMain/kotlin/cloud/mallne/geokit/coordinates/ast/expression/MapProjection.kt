package cloud.mallne.geokit.coordinates.ast.expression

data class MapProjection(
    val name: String,
    val projectionMethod: MapProjectionMethod,
    val parameters: List<MapProjectionParameter> = listOf(),
    val identifiers: List<Identifier> = listOf(),
) : SteppedOperation