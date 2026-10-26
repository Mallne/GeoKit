package cloud.mallne.geokit.interop

import cloud.mallne.geokit.geojson.CalculationInterop.toBoundary
import cloud.mallne.geokit.interop.GmlGeometryExtensions.toGeoJson
import cloud.mallne.geokit.ogc.Extensions.toGmlBoundedBy
import cloud.mallne.geokit.ogc.model.wfs.FeatureMember
import cloud.mallne.geokit.ogc.model.wfs.WfsBoundedBy
import kotlinx.serialization.json.JsonPrimitive
import nl.adaptivity.xmlutil.serialization.XML
import org.maplibre.spatialk.geojson.BoundingBox
import org.maplibre.spatialk.geojson.Feature
import org.maplibre.spatialk.geojson.FeatureCollection
import cloud.mallne.geokit.ogc.model.wfs.FeatureCollection as WfsFeatureCollection

object WfsExtensions {
    fun BoundingBox.toGmlBoundedBy(): WfsBoundedBy = this.toBoundary().toGmlBoundedBy()
    fun WfsBoundedBy.toGeoJson(): BoundingBox? = envelope?.let {
        // Keep source coordinates intact until CRS conversion; projected metres are not degrees.
        BoundingBox(southwest = it.lowerCorner.toGeoJson().single(), northeast = it.upperCorner.toGeoJson().single())
    }

    fun FeatureMember.toGeoJson(prefix: String, geometryLocalPart: String, xml: XML = XML.recommended_1_0()) = Feature(
        geometry = this.geometry(prefix, geometryLocalPart, xml)?.toGeoJson(),
        properties = this.properties(prefix).map { it.key to JsonPrimitive(it.value) }.toMap(),
    )

    fun WfsFeatureCollection.toGeoJson(prefix: String, geometryLocalPart: String, xml: XML = XML.recommended_1_0()) =
        FeatureCollection(
            bbox = this.boundedBy?.toGeoJson(),
            features = members.map { it.toGeoJson(prefix, geometryLocalPart, xml) }
        )
}
