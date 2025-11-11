package cloud.mallne.geokit.geojson.serialization

import cloud.mallne.geokit.geojson.PolygonGeometry

internal object PolygonGeometrySerializer :
    GeoJsonPolymorphicSerializer<PolygonGeometry>(
        baseClass = PolygonGeometry::class,
        allowedTypes = setOf("Polygon", "MultiPolygon"),
    )
