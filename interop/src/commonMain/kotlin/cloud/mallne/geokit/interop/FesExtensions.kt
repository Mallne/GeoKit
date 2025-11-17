package cloud.mallne.geokit.interop

import cloud.mallne.geokit.geojson.CalculationInterop.toBoundary
import cloud.mallne.geokit.geojson.CalculationInterop.toBoundingBox
import cloud.mallne.geokit.ogc.Extensions.toBoundary
import cloud.mallne.geokit.ogc.Extensions.toGmlBBOX
import cloud.mallne.geokit.ogc.model.fes.BBOX
import org.maplibre.spatialk.geojson.BoundingBox

object FesExtensions {
    fun BBOX.toGeoJson(): BoundingBox = this.toBoundary().toBoundingBox()
    fun BoundingBox.toGmlBBOX(): BBOX = this.toBoundary().toGmlBBOX()
}