package cloud.mallne.geokit.geojson.serialization

import cloud.mallne.geokit.geojson.LineStringGeometry

internal object LineStringGeometrySerializer :
    GeoJsonPolymorphicSerializer<LineStringGeometry>(
        baseClass = LineStringGeometry::class,
        allowedTypes = setOf("LineString", "MultiLineString"),
    )
