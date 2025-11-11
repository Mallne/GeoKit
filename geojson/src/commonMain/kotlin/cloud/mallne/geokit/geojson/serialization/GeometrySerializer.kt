package cloud.mallne.geokit.geojson.serialization

import cloud.mallne.geokit.geojson.Geometry

internal object GeometrySerializer :
    GeoJsonPolymorphicSerializer<Geometry>(
        baseClass = Geometry::class,
        allowedTypes =
            setOf(
                "Point",
                "MultiPoint",
                "LineString",
                "MultiLineString",
                "Polygon",
                "MultiPolygon",
                "GeometryCollection",
            ),
    )
