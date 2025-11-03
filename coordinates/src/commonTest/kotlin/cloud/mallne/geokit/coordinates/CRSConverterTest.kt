package cloud.mallne.geokit.coordinates

import cloud.mallne.geokit.coordinates.builtin.BuiltInCoordinateReferenceSystems
import cloud.mallne.geokit.coordinates.model.LocalCoordinate
import kotlin.test.Test
import kotlin.test.assertTrue

class CRSConverterTest {
    @Test
    fun convert4326To25832() {
        val coord = LocalCoordinate(50.963841, 10.998273)
        val registry = CrsRegistry()
        registry.ingest(BuiltInCoordinateReferenceSystems.EPSG4326)
        registry.ingest(BuiltInCoordinateReferenceSystems.EPSG25832)
        registry.ingest(BuiltInCoordinateReferenceSystems.EPSG1149)
        val pipeline = registry.compose(
            source = coord,
            fromEPSG = "EPSG:4326",
            toEPSG = "EPSG:25832",
        )
        println(pipeline)
        val result = pipeline.execute()
        println(result)
        //since the Accuracy is 1 Meter we could round down the result
        val northing = result.northing.toInt()
        val easting = result.easting.toInt()

        assertTrue { 5647704 == northing }
        assertTrue { 640321 == easting }
    }
}