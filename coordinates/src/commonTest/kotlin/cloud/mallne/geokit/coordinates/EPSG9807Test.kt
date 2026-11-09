package cloud.mallne.geokit.coordinates

import cloud.mallne.geokit.coordinates.execution.CoordinateProjectionTransformer
import cloud.mallne.geokit.coordinates.model.AbstractCoordinate
import cloud.mallne.geokit.coordinates.model.LocalCoordinate
import cloud.mallne.geokit.coordinates.tokens.ast.expression.ProjectedCrs
import kotlin.math.PI
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class EPSG9807Test {
    @Test
    fun airyBritishNationalGridMatchesEpsgExample() {
        // https://epsg.io/9807-method: Airy 1830, not a WGS84 datum transformation.
        val registry = registry()
        val projected = transform(registry, LocalCoordinate(50.5, 0.5).toRad())
        assertEquals(577274.99, projected.easting, 0.005)
        assertEquals(69740.50, projected.northing, 0.005)

        val geographic = transform(registry, LocalCoordinate(69740.50, 577274.99), reverse = true).toDeg()
        // The published projected input is rounded to centimetres.
        assertEquals(50.5, geographic.latitude, 1e-7)
        assertEquals(0.5, geographic.longitude, 1e-7)
    }

    @Test
    fun unroundedCoordinatesRoundTripPrecisely() {
        val registry = registry()
        for (point in listOf(LocalCoordinate(50.5, 0.5), LocalCoordinate(-35.123456789, -1.25), LocalCoordinate(0.1, -2.0))) {
            val input = point.toRad()
            val output = transform(registry, transform(registry, input), reverse = true)
            assertEquals(input.latitude, output.latitude, 1e-12)
            assertEquals(input.longitude, output.longitude, 1e-12)
        }
    }

    @Test
    fun smallNonzeroOriginsAreNotSnappedToEquator() {
        for (origin in listOf(0.0, 0.00001, -0.00001)) {
            val projected = transform(registry(origin), LocalCoordinate(origin, -2.0).toRad())
            assertEquals(400000.0, projected.easting, 1e-8)
            assertEquals(-100000.0, projected.northing, 1e-7)
        }
    }

    @Test
    fun nearPoleOriginsRetainTheirMeridionalDistanceFromBothPoles() {
        val f = 1.0 / 299.32496
        val e2 = 2 * f - f * f
        val polarRadius = 6377563.396 / sqrt(1 - e2)
        for (sign in listOf(-1.0, 1.0)) {
            val point = LocalCoordinate(sign * 80.0, -2.0).toRad()
            val atPole = transform(registry(sign * 90.0), point)
            // Includes origins within the old 1e-6 rad snapping range and within
            // EPSG's two-arcsecond warning range, down to about 0.1 millimetres.
            for (distanceDegrees in listOf(0.0001, 0.00001, 0.000000001)) {
                val nearPole = transform(registry(sign * (90.0 - distanceDegrees)), point)
                val expectedDifference = sign * 0.9996013 * polarRadius * distanceDegrees * PI / 180.0
                // Over this tiny interval the polar meridional radius is constant
                // to well below this tolerance; no tangent-based oracle is used.
                assertEquals(expectedDifference, nearPole.northing - atPole.northing, 1e-7)
            }
        }
    }

    @Test
    fun ellipsoidLengthUnitsAreNormalizedToMetres() {
        val input = LocalCoordinate(50.5, 0.5).toRad()
        val metres = transform(registry(), input)
        val feet = transform(registry(axis = 6377563.396 / 0.3048, axisUnit = "LENGTHUNIT[\"foot\",0.3048]"), input)
        assertEquals(metres.easting, feet.easting, 1e-8)
        assertEquals(metres.northing, feet.northing, 1e-8)
    }

    @Test
    fun exactPolesProjectIndependentlyOfLongitudeAndInvertToCentralMeridian() {
        for (origin in listOf(49.0, -90.0, 90.0)) {
            val registry = registry(origin)
            for (sign in listOf(-1.0, 1.0)) {
                val pole = transform(registry, LocalCoordinate(sign * PI / 2.0, -2.0 * PI / 180.0))
                assertEquals(400000.0, pole.easting, 0.0)
                assertTrue(pole.northing.isFinite())
                for (longitude in listOf(-720.0, -92.0, 88.0, 178.0, 720.0)) {
                    val other = transform(registry, LocalCoordinate(sign * 90.0, longitude).toRad())
                    assertEquals(pole.easting, other.easting, 0.0)
                    assertEquals(pole.northing, other.northing, 0.0)
                }
                val inverse = transform(registry, pole, reverse = true)
                assertEquals(sign * PI / 2.0, inverse.latitude, 0.0)
                assertEquals(-2.0 * PI / 180.0, inverse.longitude, 1e-16)
            }
        }
    }

    @Test
    fun nearPolesRoundTripWithoutAsinRoundingToPole() {
        val registry = registry()
        for (latitude in listOf(-89.999999999, -89.9999, 89.9999, 89.999999999)) {
            val input = LocalCoordinate(latitude, 10.0).toRad()
            val output = transform(registry, transform(registry, input), reverse = true)
            assertEquals(input.latitude, output.latitude, 1e-12)
            // Longitude becomes ill-conditioned extremely close to a pole.
            assertEquals(input.longitude, output.longitude, 1e-4)
        }
    }

    @Test
    fun wrappedLongitudesUseTheSamePrincipalBranchAcrossTheAntimeridian() {
        val registry = registry(centralMeridian = 179.0)
        val expected = transform(registry, LocalCoordinate(50.0, -179.0).toRad())
        for (longitude in listOf(181.0, -539.0, 541.0)) {
            val actual = transform(registry, LocalCoordinate(50.0, longitude).toRad())
            assertEquals(expected.easting, actual.easting, 1e-7)
            assertEquals(expected.northing, actual.northing, 1e-7)
        }
        val inverse = transform(registry, expected, reverse = true).toDeg()
        assertEquals(50.0, inverse.latitude, 1e-9)
        assertEquals(-179.0, inverse.longitude, 1e-9)
    }

    @Test
    fun backsideLongitudesAreRejectedRatherThanFoldedOntoPrincipalBranch() {
        for (longitude in listOf(-180.0, -90.001, 90.001, 180.0, 540.0)) {
            val failure = assertFailsWith<IllegalArgumentException> {
                transform(registry(centralMeridian = 0.0), LocalCoordinate(45.0, longitude).toRad())
            }
            assertTrue(failure.message.orEmpty().contains("principal"))
        }
    }

    @Test
    fun principalBoundaryIsFiniteAwayFromEquatorButEquatorIsSingular() {
        val registry = registry(centralMeridian = 0.0)
        for (longitude in listOf(-90.0, 90.0)) {
            val projected = transform(registry, LocalCoordinate(80.0, longitude).toRad())
            assertTrue(projected.easting.isFinite() && projected.northing.isFinite())
            val inverse = transform(registry, projected, reverse = true).toDeg()
            assertEquals(80.0, inverse.latitude, 1e-8)
            assertEquals(longitude, inverse.longitude, 1e-8)
            val failure = assertFailsWith<IllegalArgumentException> {
                transform(registry, LocalCoordinate(0.0, longitude).toRad())
            }
            assertTrue(failure.message.orEmpty().contains("singular"))
        }
        // No arbitrary central-meridian accuracy cutoff is imposed on the EPSG series.
        val wide = transform(registry, LocalCoordinate(60.0, 30.0).toRad())
        assertTrue(wide.easting.isFinite() && wide.northing.isFinite())
    }

    @Test
    fun exactLongitudeBoundariesAllowUnitConversionAndWrappingRoundoff() {
        for (centralMeridian in listOf(-170.0, -1.0, 179.0)) {
            val registry = registry(centralMeridian = centralMeridian)
            for (difference in listOf(-90.0, 90.0)) {
                for (turn in listOf(-360.0, 0.0, 360.0)) {
                    val point = LocalCoordinate(80.0, centralMeridian + difference + turn).toRad()
                    val projected = transform(registry, point)
                    assertTrue(projected.easting.isFinite() && projected.northing.isFinite())
                }
            }
        }
    }

    @Test
    fun nonfiniteForwardOutputIsNeverReturned() {
        val failure = assertFailsWith<IllegalStateException> {
            transform(registry(axis = Double.MAX_VALUE), LocalCoordinate(90.0, -2.0).toRad())
        }
        assertTrue(failure.message.orEmpty().contains("EPSG:9807 forward produced nonfinite"))
    }

    @Test
    fun invalidGeographicInputsAreRejected() {
        for (latitude in listOf(-PI, PI, Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)) {
            val failure = assertFailsWith<IllegalArgumentException> {
                transform(registry(), LocalCoordinate(latitude, 0.0))
            }
            assertTrue(failure.message.orEmpty().contains("latitude"))
        }
        for (longitude in listOf(Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)) {
            assertFailsWith<IllegalArgumentException> {
                transform(registry(), LocalCoordinate(0.0, longitude))
            }
        }
    }

    @Test
    fun nonfiniteInverseReportsProjectionAndCoordinateBeforeIteration() {
        val failure = assertFailsWith<IllegalStateException> {
            transform(registry(), LocalCoordinate(Double.NaN, 400000.0), reverse = true)
        }
        // Preserve the exception type and coordinate diagnostic, but reject NaN immediately.
        assertTrue(failure.message.orEmpty().contains("EPSG:9807 inverse requires finite"))
        assertTrue(failure.message.orEmpty().contains("northing=NaN"))
        for (input in listOf(
            LocalCoordinate(Double.POSITIVE_INFINITY, 400000.0),
            LocalCoordinate(0.0, Double.NEGATIVE_INFINITY),
            LocalCoordinate(0.0, Double.NaN),
            LocalCoordinate(0.0, Double.MAX_VALUE),
            LocalCoordinate(Double.MAX_VALUE, 400000.0),
        )) {
            val invalid = assertFailsWith<IllegalStateException> { transform(registry(), input, reverse = true) }
            assertTrue(invalid.message.orEmpty().contains("EPSG:9807 inverse"))
        }
        val northPole = transform(registry(), LocalCoordinate(90.0, -2.0).toRad())
        assertFailsWith<IllegalStateException> {
            transform(registry(), LocalCoordinate(northPole.northing + 1.0, 400000.0), reverse = true)
        }
    }

    private fun transform(registry: CrsRegistry, input: AbstractCoordinate, reverse: Boolean = false): AbstractCoordinate =
        CoordinateProjectionTransformer(
            source = registry.crs.single() as ProjectedCrs,
            reversed = reverse,
            methods = registry.methods,
        ).execute(input)

    private fun registry(
        origin: Double = 49.0,
        axis: Double = 6377563.396,
        axisUnit: String = "LENGTHUNIT[\"metre\",1]",
        centralMeridian: Double = -2.0,
    ): CrsRegistry = CrsRegistry().apply {
        // Parameter names deliberately have no identifiers, testing EPSG name lookup.
        ingest(
            """PROJCRS["Airy Transverse Mercator",
                BASEGEOGCRS["OSGB36",DATUM["Ordnance Survey of Great Britain 1936",
                    ELLIPSOID["Airy 1830",$axis,299.32496,$axisUnit]],ID["EPSG",4277]],
                CONVERSION["British National Grid",METHOD["Transverse Mercator",ID["EPSG",9807]],
                    PARAMETER["Latitude of natural origin",$origin,ANGLEUNIT["degree",0.017453292519943295]],
                    PARAMETER["Longitude of natural origin",$centralMeridian,ANGLEUNIT["degree",0.017453292519943295]],
                    PARAMETER["Scale factor at natural origin",0.9996013,SCALEUNIT["unity",1]],
                    PARAMETER["False easting",400000,LENGTHUNIT["metre",1]],
                    PARAMETER["False northing",-100000,LENGTHUNIT["metre",1]]],
                CS[Cartesian,2],AXIS["Easting (E)",east],AXIS["Northing (N)",north],
                LENGTHUNIT["metre",1],ID["EPSG",27700]]""".trimIndent()
        )
    }
}
