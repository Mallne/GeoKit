package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class ParametricUnit(
    override val unitName: String,
    override val conversionFactor: Double,
    override val identifiers: List<Identifier> = listOf(),
) : SpatialUnit,
    ParameterUnit