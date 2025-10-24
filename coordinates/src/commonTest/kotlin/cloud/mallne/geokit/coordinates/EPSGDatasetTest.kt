package cloud.mallne.geokit.coordinates

import cloud.mallne.geokit.coordinates.builtin.BuiltInCoordinateReferenceSystems
import cloud.mallne.geokit.coordinates.tokens.WktCrsParser
import kotlin.test.Test

class EPSGDatasetTest {
    @Test
    fun parserTest() {
        val crs = WktCrsParser(BuiltInCoordinateReferenceSystems.EPSG4326)
        println(crs)
    }
}