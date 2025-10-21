package cloud.mallne.geokit.coordinates.ast.expression

data class AxisRange(
    val min: Double? = null,
    val max: Double? = null,
    val meaning: AxisRangeMeaning? = null,
) : WKTCRSExpression
