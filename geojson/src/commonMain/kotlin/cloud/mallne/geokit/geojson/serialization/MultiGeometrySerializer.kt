package cloud.mallne.geokit.geojson.serialization

import cloud.mallne.geokit.geojson.MultiGeometry

internal object MultiGeometrySerializer :
    GeoJsonPolymorphicSerializer<MultiGeometry>(
        baseClass = MultiGeometry::class,
        allowedTypes = setOf("MultiPoint", "MultiLineString", "MultiPolygon"),
    )
