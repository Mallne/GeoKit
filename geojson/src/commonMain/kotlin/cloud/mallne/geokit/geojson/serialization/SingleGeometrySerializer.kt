package cloud.mallne.geokit.geojson.serialization

import cloud.mallne.geokit.geojson.SingleGeometry

internal object SingleGeometrySerializer :
    GeoJsonPolymorphicSerializer<SingleGeometry>(
        baseClass = SingleGeometry::class,
        allowedTypes = setOf("Point", "LineString", "Polygon"),
    )
