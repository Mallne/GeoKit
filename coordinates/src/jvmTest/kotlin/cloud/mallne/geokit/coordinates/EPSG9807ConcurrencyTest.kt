package cloud.mallne.geokit.coordinates

import cloud.mallne.geokit.coordinates.builtin.BuiltInCoordinateReferenceSystems
import cloud.mallne.geokit.coordinates.model.LocalCoordinate
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import kotlin.test.Test
import kotlin.test.assertEquals

class EPSG9807ConcurrencyTest {
    @Test
    fun configuredGermanUtmZonesMatchProjReferences() {
        val references = listOf(
            Triple(
                "EPSG:25832",
                LocalCoordinate(latitude = 5_760_437.649, longitude = 668_785.856),
                LocalCoordinate(latitude = 51.96896257665452, longitude = 11.457105780660712),
            ),
            // Brandenburg WFS parcel 12050102501696______, returned in EPSG:25832;
            // the same feature's EPSG:4326 response supplies the independent reference.
            Triple(
                "EPSG:25832",
                LocalCoordinate(latitude = 5_812_929.32225837, longitude = 775_818.825587298),
                LocalCoordinate(latitude = 52.3969704761363, longitude = 13.0545402852741),
            ),
            // Frankfurt parcel 060464125000010002__ from the Hessen WFS.
            Triple(
                "EPSG:25832",
                LocalCoordinate(latitude = 5_552_187.184, longitude = 476_930.215),
                LocalCoordinate(latitude = 50.1214780107256, longitude = 8.67728366901777),
            ),
            Triple(
                "EPSG:25833",
                LocalCoordinate(latitude = 5_657_267.070, longitude = 412_289.892),
                LocalCoordinate(latitude = 51.06022233402966, longitude = 13.748385225841215),
            ),
        )
        val registry = CrsRegistry().apply {
            ingest(BuiltInCoordinateReferenceSystems.EPSG4326)
            ingest(BuiltInCoordinateReferenceSystems.EPSG25832)
            ingest(BuiltInCoordinateReferenceSystems.EPSG25833)
            ingest(BuiltInCoordinateReferenceSystems.EPSG1149)
        }

        references.forEach { (zone, projected, expected) ->
            val actual = registry.compose(
                source = projected,
                fromEPSG = zone,
                toEPSG = "EPSG:4326",
            ).execute().toDeg()

            assertEquals(expected.latitude, actual.latitude, absoluteTolerance = 1e-8, "$zone latitude")
            assertEquals(expected.longitude, actual.longitude, absoluteTolerance = 1e-8, "$zone longitude")
        }
    }

    @Test
    fun concurrentUtmZoneTransformsKeepTheirOwnProjectionParameters() {
        val executor = Executors.newFixedThreadPool(8)
        try {
            val jobs = (0 until 8).map { worker ->
                Callable {
                    val useZone33 = worker % 2 == 0
                    val zone = if (useZone33) "EPSG:25833" else "EPSG:25832"
                    val input = if (useZone33) {
                        LocalCoordinate(latitude = 5_657_267.070, longitude = 412_289.892)
                    } else {
                        LocalCoordinate(latitude = 5_760_437.649, longitude = 668_785.856)
                    }
                    // Independent EPSG -> WGS84 references from PROJ 9.8.1.
                    val expectedLatitude = if (useZone33) 51.06022233402966 else 51.96896257665452
                    val expectedLongitude = if (useZone33) 13.748385225841215 else 11.457105780660712
                    val registry = CrsRegistry().apply {
                        ingest(BuiltInCoordinateReferenceSystems.EPSG4326)
                        ingest(BuiltInCoordinateReferenceSystems.EPSG25832)
                        ingest(BuiltInCoordinateReferenceSystems.EPSG25833)
                        ingest(BuiltInCoordinateReferenceSystems.EPSG1149)
                    }

                    repeat(1_000) {
                        val geographic = registry.compose(
                            source = input,
                            fromEPSG = zone,
                            toEPSG = "EPSG:4326",
                        ).execute().toDeg()
                        check(kotlin.math.abs(geographic.latitude - expectedLatitude) < 0.001) {
                            "$zone latitude displaced: $geographic"
                        }
                        check(kotlin.math.abs(geographic.longitude - expectedLongitude) < 0.01) {
                            "$zone longitude displaced: $geographic"
                        }
                    }
                }
            }
            executor.invokeAll(jobs).forEach { it.get() }
        } finally {
            executor.shutdownNow()
        }
    }
}
