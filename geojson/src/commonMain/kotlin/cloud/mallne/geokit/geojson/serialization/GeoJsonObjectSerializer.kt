package cloud.mallne.geokit.geojson.serialization

import cloud.mallne.geokit.geojson.GeoJsonObject

internal object GeoJsonObjectSerializer :
    GeoJsonPolymorphicSerializer<GeoJsonObject>(
        GeoJsonObject::class,
        setOf(
            "Point",
            "MultiPoint",
            "LineString",
            "MultiLineString",
            "Polygon",
            "MultiPolygon",
            "GeometryCollection",
            "Feature",
            "FeatureCollection",
        ),
    )
