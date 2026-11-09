package cloud.mallne.geokit.coordinates

import cloud.mallne.geokit.coordinates.builtin.BuiltInCoordinateReferenceSystems
import cloud.mallne.geokit.coordinates.model.LocalCoordinate
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import kotlin.test.Test

class EPSG9807ConcurrencyTest {
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
                    val expectedLatitude = if (useZone33) 51.065 else 51.972311
                    val expectedLongitude = if (useZone33) 13.74 else 11.457106
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
