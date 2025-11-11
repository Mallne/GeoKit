package cloud.mallne.geokit.interop

import cloud.mallne.geokit.geojson.BoundingBox
import cloud.mallne.geokit.geojson.CalculationInterop.toBoundary
import cloud.mallne.geokit.geojson.CalculationInterop.toBoundingBox
import cloud.mallne.geokit.geojson.Feature
import cloud.mallne.geokit.geojson.FeatureCollection
import cloud.mallne.geokit.gml.Extensions.toBoundary
import cloud.mallne.geokit.gml.Extensions.toGmlBoundedBy
import cloud.mallne.geokit.gml.model.wfs.FeatureMember
import cloud.mallne.geokit.gml.model.wfs.WfsBoundedBy
import cloud.mallne.geokit.interop.GmlGeometryExtensions.toGeoJson
import kotlinx.serialization.json.JsonPrimitive
import nl.adaptivity.xmlutil.serialization.XML
import cloud.mallne.geokit.gml.model.wfs.FeatureCollection as WfsFeatureCollection

object WfsExtensions {
    fun BoundingBox.toGmlBoundedBy(): WfsBoundedBy = this.toBoundary().toGmlBoundedBy()
    fun WfsBoundedBy.toGeoJson(): BoundingBox? = this.toBoundary()?.toBoundingBox()

    fun FeatureMember.toGeoJson(prefix: String, geometryLocalPart: String, xml: XML = XML()): Feature = Feature(
        geometry = this.geometry(prefix, geometryLocalPart, xml)?.toGeoJson(),
        properties = this.properties(prefix).map { it.key to JsonPrimitive(it.value) }.toMap(),
    )

    fun WfsFeatureCollection.toGeoJson(prefix: String, geometryLocalPart: String, xml: XML = XML()): FeatureCollection =
        FeatureCollection(
            bbox = this.boundedBy?.toGeoJson(),
            features = members.map { it.toGeoJson(prefix, geometryLocalPart, xml) }
        )
}