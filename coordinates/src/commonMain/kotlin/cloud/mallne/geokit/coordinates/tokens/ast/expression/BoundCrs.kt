package cloud.mallne.geokit.coordinates.tokens.ast.expression

data class BoundCrs(
    val source: CoordinateReferenceSystem,
    val target: CoordinateReferenceSystem,
    val abridgedCoordinateTransformation: AbridgedCoordinateTransformation,
    override val usages: List<Usage> = emptyList(),
    override val identifiers: List<Identifier> = emptyList(),
    override val remark: String? = null,
) : AbstractCoordinateReferenceSystem,
    RootNode
