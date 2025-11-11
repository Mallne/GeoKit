package cloud.mallne.geokit.interop

import cloud.mallne.geokit.geojson.BoundingBox
import cloud.mallne.geokit.geojson.CalculationInterop.toBoundary
import cloud.mallne.geokit.geojson.CalculationInterop.toBoundingBox
import cloud.mallne.geokit.gml.Extensions.toBoundary
import cloud.mallne.geokit.gml.Extensions.toGmlBBOX
import cloud.mallne.geokit.gml.model.fes.BBOX

object FesExtensions {
    fun BBOX.toGeoJson(): BoundingBox = this.toBoundary().toBoundingBox()
    fun BoundingBox.toGmlBBOX(): BBOX = this.toBoundary().toGmlBBOX()
}