package cloud.mallne.geokit.coordinates.ast.expression

data class Extent(
    val structure: List<ExtendStructure>,
    val identifier: Identifier? = null
)
