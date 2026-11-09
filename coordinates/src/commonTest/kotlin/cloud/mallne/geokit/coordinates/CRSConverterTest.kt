package cloud.mallne.geokit.coordinates

import cloud.mallne.geokit.coordinates.builtin.BuiltInCoordinateReferenceSystems
import cloud.mallne.geokit.coordinates.model.LocalCoordinate
import kotlin.math.abs
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

    @Test
    fun convertCoordinatesNearReportedMissingParcelBoundsTo25832() {
        val registry = CrsRegistry()
        registry.ingest(BuiltInCoordinateReferenceSystems.EPSG4326)
        registry.ingest(BuiltInCoordinateReferenceSystems.EPSG25832)
        registry.ingest(BuiltInCoordinateReferenceSystems.EPSG1149)

        val projected = registry.compose(
            source = LocalCoordinate(51.205, 11.437),
            fromEPSG = "EPSG:4326",
            toEPSG = "EPSG:25832",
        ).execute()

        assertTrue(projected.northing in 5_670_000.0..5_680_000.0, "Unexpected northing: $projected")
        assertTrue(projected.easting in 665_000.0..680_000.0, "Unexpected easting: $projected")
    }

    @Test
    fun convertSaxonyAnhaltProjectedParcelCoordinateBackToGeographic() {
        val registry = CrsRegistry()
        registry.ingest(BuiltInCoordinateReferenceSystems.EPSG4326)
        registry.ingest(BuiltInCoordinateReferenceSystems.EPSG25832)
        registry.ingest(BuiltInCoordinateReferenceSystems.EPSG1149)

        val geographic = registry.compose(
            source = LocalCoordinate(latitude = 5_760_437.649, longitude = 668_785.856),
            fromEPSG = "EPSG:25832",
            toEPSG = "EPSG:4326",
        ).execute().toDeg()

        assertTrue(abs(geographic.latitude - 51.972311) < 0.00001, "Unexpected latitude: $geographic")
        assertTrue(abs(geographic.longitude - 11.457106) < 0.00001, "Unexpected longitude: $geographic")
    }

    @Test
    fun convertRepresentativeEPSG25833ParcelCoordinatesBackToGeographic() {
        val registry = CrsRegistry()
        registry.ingest(BuiltInCoordinateReferenceSystems.EPSG4326)
        registry.ingest(BuiltInCoordinateReferenceSystems.EPSG25833)
        registry.ingest(BuiltInCoordinateReferenceSystems.EPSG1149)

        fun assertParcelCoordinate(
            state: String,
            projected: LocalCoordinate,
            latitudeBounds: ClosedFloatingPointRange<Double>,
            longitudeBounds: ClosedFloatingPointRange<Double>,
        ) {
            val geographic = registry.compose(
                source = projected,
                fromEPSG = "EPSG:25833",
                toEPSG = "EPSG:4326",
            ).execute().toDeg()

            assertTrue(geographic.latitude in latitudeBounds, "$state latitude out of bounds: $geographic")
            assertTrue(geographic.longitude in longitudeBounds, "$state longitude out of bounds: $geographic")
        }

        assertParcelCoordinate(
            "Saxony",
            LocalCoordinate(latitude = 5_657_267.070, longitude = 412_289.892),
            51.05..51.08,
            13.72..13.76,
        )
        assertParcelCoordinate(
            "Brandenburg",
            LocalCoordinate(latitude = 5_735_891.500, longitude = 453_650.313),
            51.74..51.78,
            14.30..14.36,
        )
    }
}
