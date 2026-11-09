package cloud.mallne.geokit.coordinates

import cloud.mallne.geokit.coordinates.builtin.BuiltInCoordinateReferenceSystems
import cloud.mallne.geokit.coordinates.model.LocalCoordinate
import cloud.mallne.geokit.coordinates.tokens.WktCrsParser
import cloud.mallne.geokit.coordinates.tokens.ast.expression.*
import kotlin.math.PI
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class EPSGDefinitionRegressionTest {
    @Test
    fun epsgIoWktWithAxisOrderAndPerAxisUnitsParses() {
        // EPSG.io /4326.wkt2: retain the integer/decimal number forms and axis clauses.
        val crs = assertIs<StaticGeographicCrs>(WktCrsParser(EPSG_IO_4326))
        val cs = assertIs<SpatialCS>(crs.coordinateSystem)
        assertEquals(listOf(1, 2), cs.spatialAxis.map { it.order })
        cs.spatialAxis.forEach { assertEquals(PI / 180.0, it.unit!!.conversionFactor, 1e-16) }
        assertEquals(6378137.0, crs.system.ellipsoid.semiMajorAxis)
        assertEquals(298.257223563, crs.system.ellipsoid.inverseFlattening)
    }

    @Test
    fun axisOrderMustRemainAnUnsignedInteger() {
        for (invalid in listOf("1.5", "1e0", "-1", "+1")) {
            assertFailsWith<IllegalArgumentException> {
                WktCrsParser(EPSG_IO_4326.replace("ORDER[1]", "ORDER[$invalid]"))
            }
        }
    }

    @Test
    fun registryNormalizesProjectedPerAxisUnitsIncludingCachedPipelines() {
        val parsed = WktCrsParser(BuiltInCoordinateReferenceSystems.EPSG25832) as ProjectedCrs
        for (factor in listOf(1.0, 0.3048)) {
            val cs = parsed.coordinateSystem as SpatialCS
            // Axis units override a CS-level default. The internal coordinate convention is N,E,
            // even though the EPSG projected CRS lists E,N.
            val source = parsed.copy(coordinateSystem = cs.copy(
                unit = LengthUnit("unused default", 2.0),
                spatialAxis = cs.spatialAxis.map { it.copy(unit = LengthUnit("axis unit", factor)) },
            ))
            val registry = CrsRegistry().apply {
                crs.add(source)
                ingest(BuiltInCoordinateReferenceSystems.EPSG4326)
                ingest(BuiltInCoordinateReferenceSystems.EPSG1149)
            }
            repeat(2) {
                val result = registry.compose(
                    LocalCoordinate(5760437.649 / factor, 668785.856 / factor),
                    "EPSG:25832", "EPSG:4326",
                ).execute().toDeg()
                // Independent PROJ reference used by CRSConverterTest.
                assertEquals(51.96896257665452, result.latitude, 1e-8)
                assertEquals(11.457105780660712, result.longitude, 1e-8)
            }
        }
    }

    @Test
    fun registryDoesNotTreatPerAxisProjectedMetresAsDegrees() {
        val parsed = WktCrsParser(BuiltInCoordinateReferenceSystems.EPSG25832) as ProjectedCrs
        val cs = parsed.coordinateSystem as SpatialCS
        val registry = CrsRegistry().apply {
            crs.add(parsed.copy(coordinateSystem = cs.copy(
                unit = null,
                spatialAxis = cs.spatialAxis.map { it.copy(unit = LengthUnit("metre", 1.0)) },
            )))
            ingest(BuiltInCoordinateReferenceSystems.EPSG4326)
            ingest(BuiltInCoordinateReferenceSystems.EPSG1149)
        }
        repeat(2) {
            val result = registry.compose(LocalCoordinate(5760437.649, 668785.856), "EPSG:25832", "EPSG:4326")
                .execute().toDeg()
            assertEquals(51.96896257665452, result.latitude, 1e-8)
            assertEquals(11.457105780660712, result.longitude, 1e-8)
        }
    }

    @Test
    fun sphericalEllipsoidUsesZeroFlattening() {
        // https://epsg.io/7047-ellipsoid.wkt2: inverse flattening zero denotes a sphere.
        val sphere = Ellipsoid("GRS 1980 Authalic Sphere", 6370997.0, 0.0, LengthUnit("metre", 1.0))
        assertEquals(0.0, sphere.flattening)
        assertEquals(0.0, sphere.eccentricitySquared)
        assertEquals(0.0, sphere.eccentricity)
        assertEquals(sphere.semiMajorAxis, sphere.semiMinorAxis)
    }

    @Test
    fun builtInDefinitionsRetainCurrentEpsgEnsemblesAndUsageMetadata() {
        // EPSG.io dataset 13.102, checked 2026-11-09. ETRS89 has 60 members.
        for (wkt in listOf(BuiltInCoordinateReferenceSystems.EPSG25832, BuiltInCoordinateReferenceSystems.EPSG25833)) {
            val crs = assertIs<ProjectedCrs>(WktCrsParser(wkt))
            val ensemble = assertIs<GeodeticDatumEnsemble>(crs.base.constraints)
            assertCurrentEtrs89(ensemble)
            assertEquals(2, crs.usages.size)
            assertTrue(crs.usages.any { it.scope == "Engineering survey, topographic mapping." })
            crs.usages.forEach { assertNotNull(it.extent.area); assertNotNull(it.extent.bbox) }
        }
        val operation = assertIs<CoordinateOperation>(WktCrsParser(BuiltInCoordinateReferenceSystems.EPSG1149))
        assertCurrentEtrs89(assertIs<GeodeticDatumEnsemble>((operation.source as StaticGeographicCrs).system))
        assertTrue(operation.usages.single().scope.contains("medium and low accuracy"))
        assertNotNull(operation.usages.single().extent.bbox)
        val wgs84 = assertIs<StaticGeographicCrs>(WktCrsParser(BuiltInCoordinateReferenceSystems.EPSG4326))
        assertEquals("Horizontal component of 3D system.", wgs84.usages.single().scope)
        assertEquals(8, (wgs84.system as GeodeticDatumEnsemble).members.size)
    }

    private fun assertCurrentEtrs89(ensemble: GeodeticDatumEnsemble) {
        assertEquals(60, ensemble.members.size)
        val names = ensemble.members.map { it.name }
        for (name in listOf("ETRS89/DREF91 Realization 2025", "ETRS89-LUX [2025]", "Swiss Terrestrial Reference System 1995")) {
            assertTrue(name in names, "Missing EPSG ensemble member: $name")
        }
    }

    private companion object {
        const val EPSG_IO_4326 = """GEOGCRS["WGS 84",ENSEMBLE["World Geodetic System 1984 ensemble",MEMBER["World Geodetic System 1984 (Transit)"],MEMBER["World Geodetic System 1984 (G730)"],MEMBER["World Geodetic System 1984 (G873)"],MEMBER["World Geodetic System 1984 (G1150)"],MEMBER["World Geodetic System 1984 (G1674)"],MEMBER["World Geodetic System 1984 (G1762)"],MEMBER["World Geodetic System 1984 (G2139)"],MEMBER["World Geodetic System 1984 (G2296)"],ELLIPSOID["WGS 84",6378137,298.257223563,LENGTHUNIT["metre",1]],ENSEMBLEACCURACY[2.0]],PRIMEM["Greenwich",0,ANGLEUNIT["degree",0.0174532925199433]],CS[ellipsoidal,2],AXIS["geodetic latitude (Lat)",north,ORDER[1],ANGLEUNIT["degree",0.0174532925199433]],AXIS["geodetic longitude (Lon)",east,ORDER[2],ANGLEUNIT["degree",0.0174532925199433]],USAGE[SCOPE["Horizontal component of 3D system."],AREA["World."],BBOX[-90,-180,90,180]],ID["EPSG",4326]]"""
    }
}
