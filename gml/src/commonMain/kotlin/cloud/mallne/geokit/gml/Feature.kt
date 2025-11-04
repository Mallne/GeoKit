package cloud.mallne.geokit.gml

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import nl.adaptivity.xmlutil.dom2.Element
import nl.adaptivity.xmlutil.dom2.localName
import nl.adaptivity.xmlutil.dom2.prefix
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

/**
 * Generic GML Feature base type (whitewall: no domain-specific fields).
 * This is a simplified, library-internal representation that can be mapped to GML 3.2.
 */
@XmlSerialName("AbstractFeature", GmlNamespaces.GML, "gml")
@Serializable
open class FeatureType(
    /** gml:id if available */
    val id: String? = null,
    /** Optional human-readable name/description */
    val name: String? = null,
    val description: String? = null,
    /** BoundedBy as Envelope if present */
    @XmlElement(true)
    val boundedBy: Envelope? = null,
    /** Primary geometry of this feature, if known */
    @XmlElement(true)
    val geometry: Geometry? = null,
    /**
     * Arbitrary attributes/properties for unspecialized usage.
     * Keep keys simple (local names); namespace-aware mapping can be added later.
     */
    val properties: Map<String, String> = emptyMap()
)

/** A lightweight member wrapper that may appear in collections. */
@XmlSerialName("member", GmlNamespaces.WFS, "wfs")
@Serializable
data class FeatureMember(
    /** Raw XML content of the feature element (unknown domain). */
    @XmlValue(true)
    val content: Element? = null
) {
    fun properties(nameSpace: String): Map<String, String> {
        val elements =
            content?.getChildNodes()?.filter { it is Element }?.filter { (it as Element).prefix == nameSpace }
        return elements?.associate { (it as Element).localName to (it.getTextContent() ?: "") } ?: emptyMap()
    }

    fun geometry(prefix: String, localPart: String): Geometry? {
        val geomElement = content?.getElementsByTagName("$prefix:$localPart")
        val geomNode = geomElement?.firstOrNull() as? Element
        return geomNode?.getTextContent()?.let { XML().decodeFromString<Geometry>(it) }
    }
}

/** Wrapper for wfs:boundedBy that contains a gml:Envelope. */
@XmlSerialName("boundedBy", GmlNamespaces.WFS, "wfs")
@Serializable
data class WfsBoundedBy(
    @XmlSerialName("Envelope", GmlNamespaces.GML, "gml")
    @XmlElement(true)
    val envelope: Envelope? = null
)

/** WFS 2.0 FeatureCollection focusing on collection metadata and bounds. */
@XmlSerialName("FeatureCollection", GmlNamespaces.WFS, "wfs")
@Serializable
data class FeatureCollection(
    val id: String? = null,
    val timeStamp: String? = null,
    val numberMatched: Long? = null,
    val numberReturned: Long? = null,
    @XmlElement(true)
    val boundedBy: WfsBoundedBy? = null,
    @XmlSerialName("member", GmlNamespaces.WFS, "wfs")
    @XmlElement(true)
    val members: List<FeatureMember> = emptyList()
)
