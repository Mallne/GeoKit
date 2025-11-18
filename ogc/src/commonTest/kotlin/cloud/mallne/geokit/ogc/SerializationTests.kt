package cloud.mallne.geokit.ogc

import cloud.mallne.geokit.ogc.model.gml.geometry.AbstractGeometryType
import cloud.mallne.geokit.ogc.model.gml.geometry.MultiSurface
import cloud.mallne.geokit.ogc.model.wfs.FeatureCollection
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.modules.SerializersModule
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import nl.adaptivity.xmlutil.serialization.XML
import kotlin.test.Test
import kotlin.test.assertTrue

class SerializationTests {
    private fun stripXmlProlog(input: String): String =
        input.trimStart().replaceFirst(Regex("""^<\?xml[^?]*\?>\s*"""), "")

    @OptIn(ExperimentalXmlUtilApi::class)
    @Test
    fun testDataset() {
        val xml = XML {
            recommended()
        }
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
        val xml = XML {
            recommended()
        }
        val objs = xml.decodeFromString<MultiSurface>(
            ParserTest.SURFACE_MEMBER,
        )
        println(objs)
    }

    @OptIn(ExperimentalXmlUtilApi::class)
    @Test
    fun textGeom() {
        val xml = XML {
            recommended()
        }
        val objs = xml.decodeFromString<AbstractGeometryType>(
            ParserTest.GEOM,
        )

        println(objs)
    }

    @OptIn(ExperimentalXmlUtilApi::class)
    @Test
    fun textCurve() {
        val xml = XML(SerializersModule {
        }) {
            recommended()
        }
        val objs = xml.decodeFromString<AbstractGeometryType>(
            ParserTest.CURVE,
        )
        println(objs)
    }
}