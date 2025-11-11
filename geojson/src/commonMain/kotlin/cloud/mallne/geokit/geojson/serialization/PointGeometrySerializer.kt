package cloud.mallne.geokit.geojson.serialization

import cloud.mallne.geokit.geojson.PointGeometry

internal object PointGeometrySerializer :
    GeoJsonPolymorphicSerializer<PointGeometry>(
        baseClass = PointGeometry::class,
        allowedTypes = setOf("Point", "MultiPoint"),
    )
