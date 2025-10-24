package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class SpatialCS(
    val type: SpatialCSType,
    override val dimension: Dimension,
    override val identifiers: List<Identifier> = listOf(),
    val spatialAxis: List<SpatialAxis>,
    val unit: WKTUnit? = null,
) : CoordinateSystem