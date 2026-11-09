package cloud.mallne.geokit.coordinates

import cloud.mallne.geokit.coordinates.builtin.method.CommonExecutionMethods
import cloud.mallne.geokit.coordinates.execution.CoordinateOperationTransformer
import cloud.mallne.geokit.coordinates.model.AbstractCoordinate
import cloud.mallne.geokit.coordinates.model.LocalCoordinate
import cloud.mallne.geokit.coordinates.tokens.ast.expression.*
import kotlin.math.PI
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class EPSGMethodsTest {
    private val metre = LengthUnit("metre", 1.0)
    private val degree = AngleUnit("degree", PI / 180.0)
    private val wgs84 = Ellipsoid("WGS 84", 6378137.0, 298.257223563, metre)
    private val international = Ellipsoid("International 1924", 6378388.0, 297.0, metre)

    @Test
    fun geographicGeocentricConversionMatchesPublishedExampleInBothDirections() {
        val operation = conversion(geographic(), geocentric(), "Geographic/geocentric conversions")
        val geographic = LocalCoordinate(53.80939444444444, 2.12955, 73.0).toRad()
        val cartesian = transform(operation, geographic)
        assertCoordinate(LocalCoordinate(3771793.97, 140253.34, 5124304.35), cartesian, 0.01)
        val restored = transform(operation, cartesian, reverse = true)
        assertEquals(geographic.phi, restored.phi, 1e-12)
        assertEquals(geographic.lambda, restored.lambda, 1e-12)
        assertEquals(73.0, restored.h!!, 1e-5)
    }

    @Test
    fun geographicGeocentricConversionHandlesEquatorBothHemispheresAndPoles() {
        val operation = conversion(geographic(), geocentric(), "Geographic/geocentric conversions")
        for (latitude in listOf(-90.0, -89.999999, -45.0, 0.0, 45.0, 89.999999, 90.0)) {
            val input = LocalCoordinate(latitude, 0.0, 125.0).toRad()
            val restored = transform(operation, transform(operation, input), reverse = true)
            assertEquals(input.phi, restored.phi, 1e-12)
            assertEquals(input.lambda, restored.lambda, 1e-12)
            assertEquals(input.h!!, restored.h!!, 1e-5)
        }
    }

    @Test
    fun ellipsoidAxesAreConvertedToMetres() {
        val input = LocalCoordinate(53.80939444444444, 2.12955, 73.0).toRad()
        val feet = wgs84.copy(semiMajorAxis = wgs84.semiMajorAxis / 0.3048, unit = LengthUnit("foot", 0.3048))
        val metricOperation = conversion(geographic(), geocentric(), "Geographic/geocentric conversions")
        val feetOperation = conversion(geographic(ellipsoid = feet), geocentric(ellipsoid = feet), metricOperation.method.name)
        val expected = transform(metricOperation, input)
        assertCoordinate(expected, transform(feetOperation, input), 1e-8)
        assertCoordinate(input, transform(feetOperation, expected, reverse = true), 1e-5)
    }

    @Test
    fun nonGreenwichPrimeMeridianIsAppliedInBothDirections() {
        // Paris: 2.5969213 grads east of Greenwich.
        val paris = PrimeMeridian("Paris", IrmLongitude(2.5969213, AngleUnit("grad", PI / 200.0)))
        val operation = conversion(geographic(primeMeridian = paris), geocentric(), "Geographic/geocentric conversions")
        val local = LocalCoordinate(0.0, -2.5969213 * PI / 200.0, 0.0)
        val cartesian = transform(operation, local)
        assertCoordinate(LocalCoordinate(wgs84.semiMajorAxis, 0.0, 0.0), cartesian, 1e-8)
        assertCoordinate(local, transform(operation, cartesian, reverse = true), 1e-8)
    }

    @Test
    fun primeMeridianWithoutUnitInheritsGeographicAngularUnit() {
        val paris = PrimeMeridian("Paris", IrmLongitude(2.33722917))
        val operation = conversion(geographic(primeMeridian = paris), geocentric(), "Geographic/geocentric conversions")
        val input = LocalCoordinate(0.0, -2.33722917, 0.0).toRad()
        assertCoordinate(LocalCoordinate(wgs84.semiMajorAxis, 0.0, 0.0), transform(operation, input), 1e-8)
    }

    @Test
    fun geocentricTranslationsUseOfficialMethodAndParameterNamesWithoutIds() {
        val operation = conversion(geocentric(), geocentric(international), "Geocentric translations (geocentric domain)", translations())
        val source = LocalCoordinate(3771793.97, 140253.34, 5124304.35)
        val target = LocalCoordinate(3771878.84, 140349.83, 5124421.30)
        assertCoordinate(target, transform(operation, source), 1e-8)
        assertCoordinate(source, transform(operation, target, reverse = true), 1e-8)
    }

    @Test
    fun translationParametersAreConvertedToMetres() {
        val parameters = translations().map { it.copy(value = it.value / 0.3048, unit = LengthUnit("foot", 0.3048)) }
        val operation = conversion(geocentric(), geocentric(international), "Geocentric translations (geocentric domain)", parameters)
        assertCoordinate(LocalCoordinate(84.87, 96.49, 116.95), transform(operation, LocalCoordinate(0.0, 0.0, 0.0)), 1e-10)
    }

    @Test
    fun geographic2DTranslationMatchesPublishedExampleAndReverses() {
        val operation = conversion(geographic(dimension = Dimension.D2), geographic(international, Dimension.D2),
            "Geocentric translations (geog2D domain)", translations())
        val source = LocalCoordinate(53.80939444444444, 2.12955).toRad()
        val target = transform(operation, source).toDeg()
        // EPSG publishes the example with h=73m; 9603 assumes h=0. The horizontal difference is sub-millimetric.
        assertEquals(53.0 + 48.0 / 60.0 + 36.565 / 3600.0, target.phi, 2e-7)
        assertEquals(2.0 + 7.0 / 60.0 + 51.477 / 3600.0, target.lambda, 2e-7)
        assertNull(target.h)
        val restored = transform(operation, target.toRad(), reverse = true)
        // A 2D operation discards the transformed height, so reversal is not an exact 3D round trip.
        assertEquals(source.phi, restored.phi, 1e-9)
        assertEquals(source.lambda, restored.lambda, 1e-9)
        assertNull(restored.h)
    }

    @Test
    fun geographic2DTranslationUsesSourceAndTargetPrimeMeridiansInBothDirections() {
        val paris = PrimeMeridian("Paris", IrmLongitude(2.33722917, degree))
        val source = geographic(dimension = Dimension.D2)
        val target = geographic(dimension = Dimension.D2, primeMeridian = paris)
        val parameters = translations().map { it.copy(value = 0.0) }
        val operation = conversion(source, target, "Geocentric translations (geog2D domain)", parameters)
        val input = LocalCoordinate(40.0, 0.0).toRad()
        val projected = transform(operation, input)
        assertEquals(40.0, projected.toDeg().latitude, 1e-10)
        assertEquals(-2.33722917, projected.toDeg().longitude, 1e-10)
        val restored = transform(operation, projected, reverse = true)
        assertEquals(input.latitude, restored.latitude, 1e-12)
        assertEquals(input.longitude, restored.longitude, 1e-12)
    }

    @Test
    fun idsTakePrecedenceOverParameterNamesAndLegacyAliasesStillResolve() {
        val method = OperationMethod("Ignored name", listOf(Identifier("EPSG", TextLiteral("1031"))))
        val parameters = translations().mapIndexed { index, parameter ->
            parameter.copy(name = "Ignored parameter", identifiers = listOf(Identifier("EPSG", TextLiteral("${8605 + index}"))))
        }
        val operation = conversion(geocentric(), geocentric(international), method.name, parameters).copy(method = method)
        val expected = LocalCoordinate(84.87, 96.49, 116.95)
        assertCoordinate(expected, transform(operation, LocalCoordinate(0.0, 0.0, 0.0)), 1e-10)

        val legacy = translations().map { it.copy(name = it.name.replace("-axis", " - axis")) }
        val legacyOperation = operation.copy(method = OperationMethod("Geocentric Translations (geocentric domain)"), parameters = legacy)
        assertCoordinate(expected, transform(legacyOperation, LocalCoordinate(0.0, 0.0, 0.0)), 1e-10)
    }

    @Test
    fun geographic3DTo2DUsesOfficialNameAndPreservesHorizontalCoordinates() {
        val operation = conversion(geographic(), geographic(dimension = Dimension.D2), "Geographic3D to 2D conversion")
        val input = LocalCoordinate(0.5, 0.25, 123.0)
        val flattened = transform(operation, input)
        assertEquals(input.phi, flattened.phi)
        assertEquals(input.lambda, flattened.lambda)
        assertNull(flattened.h)
        assertCoordinate(LocalCoordinate(0.5, 0.25, 0.0), transform(operation, flattened, reverse = true), 0.0)
    }

    @Test
    fun angularConversionsNeverScaleEllipsoidalHeight() {
        val input = LocalCoordinate(45.0, 90.0, 123.0)
        assertCoordinate(LocalCoordinate(PI / 4.0, PI / 2.0, 123.0), input.toRad(), 1e-15)
        assertCoordinate(input, input.toRad().toDeg(), 1e-12)
        assertCoordinate(input.toRad(), input.autoConvert(degree), 1e-15)
        assertCoordinate(LocalCoordinate(13.716, 27.432, 37.4904), input.autoConvert(LengthUnit("foot", 0.3048)), 1e-12)
    }

    @Test
    fun registryPreservesHeightDuringAngularNormalizationIncludingCachedPipelines() {
        val source = geographic()
        val target = geocentric()
        val operation = conversion(source, target, "Geographic/geocentric conversions")
        val registry = CrsRegistry().apply {
            crs.addAll(listOf(source, target))
            operations.add(operation)
        }
        repeat(2) {
            val output = registry.compose(LocalCoordinate(53.80939444444444, 2.12955, 73.0), "TEST:1", "TEST:2").execute()
            assertCoordinate(LocalCoordinate(3771793.97, 140253.34, 5124304.35), output, 0.01)
        }
    }

    @Test
    fun registryNormalizesPerAxisAnglesAndHeightIndependently() {
        for (angularUnit in listOf(AngleUnit("radian", 1.0), AngleUnit("grad", PI / 200.0))) {
            val source = geographic().copy(coordinateSystem = SpatialCS(
                SpatialCSType.ELLIPSOIDAL, Dimension.D3,
                spatialAxis = listOf(
                    SpatialAxis("latitude", AxisDirection.NORTH(), unit = angularUnit),
                    SpatialAxis("longitude", AxisDirection.EAST, unit = angularUnit),
                    SpatialAxis("height", AxisDirection.UP, unit = LengthUnit("foot", 0.3048)),
                ),
            ))
            val target = geocentric()
            val registry = CrsRegistry().apply {
                crs.addAll(listOf(source, target))
                operations.add(conversion(source, target, "Geographic/geocentric conversions"))
            }
            val input = LocalCoordinate(53.80939444444444, 2.12955, 73.0).toRad()
            repeat(2) {
                val result = registry.compose(LocalCoordinate(
                    input.phi / angularUnit.conversionFactor,
                    input.lambda / angularUnit.conversionFactor,
                    input.h!! / 0.3048,
                ), "TEST:1", "TEST:2").execute()
                assertCoordinate(LocalCoordinate(3771793.97, 140253.34, 5124304.35), result, 0.01)
            }
        }
    }

    @Test
    fun sphericalGeographicGeocentricConversionMatchesAnalyticCoordinates() {
        val sphere = Ellipsoid("GRS 1980 Authalic Sphere", 6370997.0, 0.0, metre)
        val operation = conversion(geographic(sphere), geocentric(sphere), "Geographic/geocentric conversions")
        val input = LocalCoordinate(30.0, 60.0, 123.0).toRad()
        val radius = sphere.semiMajorAxis + input.h!!
        val expected = LocalCoordinate(
            radius * kotlin.math.sqrt(3.0) / 4.0,
            radius * 3.0 / 4.0,
            radius / 2.0,
        )
        val projected = transform(operation, input)
        assertCoordinate(expected, projected, 1e-8)
        assertCoordinate(input, transform(operation, projected, reverse = true), 1e-8)
    }

    private fun geographic(
        ellipsoid: Ellipsoid = wgs84,
        dimension: Dimension = Dimension.D3,
        primeMeridian: PrimeMeridian? = null,
    ) = StaticGeographicCrs(
        name = "Geographic",
        system = GeodeticReferenceFrame("Datum", ellipsoid, primeMeridian = primeMeridian),
        coordinateSystem = SpatialCS(SpatialCSType.ELLIPSOIDAL, dimension, spatialAxis = emptyList(), unit = degree),
        identifiers = listOf(Identifier("TEST", TextLiteral("1"))),
    )

    private fun geocentric(ellipsoid: Ellipsoid = wgs84) = StaticGeodeticCrs(
        name = "Geocentric",
        system = GeodeticReferenceFrame("Datum", ellipsoid),
        coordinateSystem = SpatialCS(SpatialCSType.CARTESIAN, Dimension.D3, spatialAxis = emptyList(), unit = metre),
        identifiers = listOf(Identifier("TEST", TextLiteral("2"))),
    )

    private fun conversion(
        source: CoordinateReferenceSystem,
        target: CoordinateReferenceSystem,
        method: String,
        parameters: List<OperationParameter> = emptyList(),
    ) = CoordinateOperation("Test operation", source = source, target = target, method = OperationMethod(method), parameters = parameters)

    private fun translations() = listOf(
        OperationParameter("X-axis translation", 84.87, metre),
        OperationParameter("Y-axis translation", 96.49, metre),
        OperationParameter("Z-axis translation", 116.95, metre),
    )

    private fun transform(operation: CoordinateOperation, coordinate: AbstractCoordinate, reverse: Boolean = false): AbstractCoordinate =
        CoordinateOperationTransformer(operation, reversed = reverse, methods = CommonExecutionMethods.entries).execute(coordinate)

    private fun assertCoordinate(expected: AbstractCoordinate, actual: AbstractCoordinate, tolerance: Double) {
        assertEquals(expected.latitude, actual.latitude, tolerance)
        assertEquals(expected.longitude, actual.longitude, tolerance)
        if (expected.altitude == null) assertNull(actual.altitude) else assertEquals(expected.altitude!!, actual.altitude!!, tolerance)
    }
}
