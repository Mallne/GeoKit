package cloud.mallne.geokit.interop

import cloud.mallne.geokit.geojson.CalculationInterop.toBoundary
import cloud.mallne.geokit.geojson.CalculationInterop.toBoundingBox
import cloud.mallne.geokit.interop.GmlGeometryExtensions.toGeoJson
import cloud.mallne.geokit.ogc.Extensions.toBoundary
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
    fun WfsBoundedBy.toGeoJson(): BoundingBox? = this.toBoundary()?.toBoundingBox()

    fun FeatureMember.toGeoJson(prefix: String, geometryLocalPart: String, xml: XML = XML()) = Feature(
        geometry = this.geometry(prefix, geometryLocalPart, xml)?.toGeoJson(),
        properties = this.properties(prefix).map { it.key to JsonPrimitive(it.value) }.toMap(),
    )

    fun WfsFeatureCollection.toGeoJson(prefix: String, geometryLocalPart: String, xml: XML = XML()) =
        FeatureCollection(
            bbox = this.boundedBy?.toGeoJson(),
            features = members.map { it.toGeoJson(prefix, geometryLocalPart, xml) }
        )
}