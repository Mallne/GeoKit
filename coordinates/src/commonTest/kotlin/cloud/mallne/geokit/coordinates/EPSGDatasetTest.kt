package cloud.mallne.geokit.coordinates

import cloud.mallne.geokit.coordinates.builtin.BuiltInCoordinateReferenceSystems
import cloud.mallne.geokit.coordinates.tokens.WktCrsParser
import kotlin.test.Test

class EPSGDatasetTest {
    @Test
    fun parser4326() {
        val crs = WktCrsParser(BuiltInCoordinateReferenceSystems.EPSG4326)
        println(crs)
    }

    @Test
    fun parser25832() {
        val crs = WktCrsParser(BuiltInCoordinateReferenceSystems.EPSG25832)
        println(crs)
    }

    @Test
    fun parser1149() {
        val crs = WktCrsParser(BuiltInCoordinateReferenceSystems.EPSG1149)
        println(crs)
    }

}