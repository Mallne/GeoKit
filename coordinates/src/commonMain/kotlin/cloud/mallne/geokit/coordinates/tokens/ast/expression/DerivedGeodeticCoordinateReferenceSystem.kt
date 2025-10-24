package cloud.mallne.geokit.coordinates.tokens.ast.expression

sealed interface DerivedGeodeticCoordinateReferenceSystem :
    SingleCoordinateReferenceSystem,
    StaticCrsCoordinateMetadata,
    DynamicCrsCoordinateMetadata,
    RootNode