package cloud.mallne.geokit.coordinates.tokens.ast.expression

sealed interface Operation : ScopeExtentIdentifierRemark {
    val name: String
    val version: String?
    val source: CoordinateReferenceSystem
    val target: CoordinateReferenceSystem
    val accuracy: Double?
}