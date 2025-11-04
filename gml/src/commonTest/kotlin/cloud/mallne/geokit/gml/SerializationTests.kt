import cloud.mallne.geokit.gml.FeatureCollection
import cloud.mallne.geokit.gml.ParserTest
import kotlinx.serialization.decodeFromString
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import nl.adaptivity.xmlutil.serialization.DefaultXmlSerializationPolicy
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlConfig
import kotlin.test.Test
import kotlin.test.assertTrue

class SerializationTests {
    private fun stripXmlProlog(input: String): String =
        input.trimStart().replaceFirst(Regex("""^<\?xml[^?]*\?>\s*"""), "")

    @OptIn(ExperimentalXmlUtilApi::class)
    @Test
    fun testDataset() {
        val xml = XML {
            indent = 2
            autoPolymorphic = true
            policy = DefaultXmlSerializationPolicy {
                unknownChildHandler = XmlConfig.IGNORING_UNKNOWN_CHILD_HANDLER
            }
        }
        val source = stripXmlProlog(ParserTest.GPRXY)
        val objs = xml.decodeFromString<FeatureCollection>(source)
        assertTrue(objs.members.isNotEmpty(), "Expected at least one member")
        val node = objs.members.first().content
        println(node)
    }
}