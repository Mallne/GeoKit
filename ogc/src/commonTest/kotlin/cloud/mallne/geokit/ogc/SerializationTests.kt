package cloud.mallne.geokit.ogc

import cloud.mallne.geokit.ogc.model.gml.geometry.AbstractGeometryType
import cloud.mallne.geokit.ogc.model.gml.geometry.MultiSurface
import cloud.mallne.geokit.ogc.model.wfs.FeatureCollection
import kotlinx.serialization.decodeFromString
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import nl.adaptivity.xmlutil.serialization.XML
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class SerializationTests {
    private fun stripXmlProlog(input: String): String =
        input.trimStart().replaceFirst(Regex("""^<\?xml[^?]*\?>\s*"""), "")

    @OptIn(ExperimentalXmlUtilApi::class)
    @Test
    fun featureCollectionAcceptsWfsResponseMetadataAttributes() {
        val xml = XML.recommended_1_0()
        val source = """<wfs:FeatureCollection xmlns:wfs="${Namespaces.WFS}" timeStamp="2026-10-13T00:00:00Z" numberMatched="unknown" numberReturned="10" next="https://example.test/features?startIndex=10" previous="https://example.test/features?startIndex=0" lockId="lock-1"/>"""

        val collection = xml.decodeFromString<FeatureCollection>(source)

        assertEquals("https://example.test/features?startIndex=10", collection.next)
        assertEquals("https://example.test/features?startIndex=0", collection.previous)
        assertEquals("lock-1", collection.lockId)
    }

    @OptIn(ExperimentalXmlUtilApi::class)
    @Test
    fun testDataset() {
        val xml = XML.recommended_1_0()
        val source = stripXmlProlog(ParserTest.GPRXY)
        val objs = xml.decodeFromString<FeatureCollection>(source)
        assertTrue(objs.members.isNotEmpty(), "Expected at least one member")
        val node = objs.members.first()
        println(node)
        val properties = node.properties("ave")
        println(properties)
        val geometry = node.geometry("ave", "geometrie", xml)
        println(geometry)
    }

    @OptIn(ExperimentalXmlUtilApi::class)
    @Test
    fun textMultiSurface() {
        val xml = XML.recommended_1_0()
        val objs = xml.decodeFromString<MultiSurface>(
            ParserTest.SURFACE_MEMBER,
        )
        println(objs)
    }

    @OptIn(ExperimentalXmlUtilApi::class)
    @Test
    fun textGeom() {
        val xml = XML.recommended_1_0()
        val objs = xml.decodeFromString<AbstractGeometryType>(
            ParserTest.GEOM,
        )

        println(objs)
    }

    @OptIn(ExperimentalXmlUtilApi::class)
    @Test
    fun textCurve() {
        val xml = XML.recommended_1_0()
        val objs = xml.decodeFromString<AbstractGeometryType>(
            ParserTest.CURVE,
        )
        println(objs)
    }
}
