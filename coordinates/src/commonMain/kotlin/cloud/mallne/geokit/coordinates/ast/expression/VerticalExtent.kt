package cloud.mallne.geokit.coordinates.ast.expression

data class VerticalExtent(
    val minHeight: Double,
    val maxHeight: Double,
    val unit: LengthUnit? = null
) : ExtendStructure