package cloud.mallne.geokit.geojson

import kotlinx.serialization.json.JsonObject
import org.maplibre.spatialk.geojson.Feature
import org.maplibre.spatialk.geojson.FeatureCollection
import org.maplibre.spatialk.geojson.Geometry

typealias JsonFeatureCollection = FeatureCollection<Geometry?, JsonObject?>
typealias JsonFeature = Feature<Geometry?, JsonObject?>