package cloud.mallne.geokit.ogc.model.wfs

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.geometry.Geometry
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import nl.adaptivity.xmlutil.dom2.Element
import nl.adaptivity.xmlutil.dom2.localName
import nl.adaptivity.xmlutil.dom2.prefix
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

/** A lightweight member wrapper that may appear in collections. */
@XmlSerialName("member", Namespaces.WFS, Namespaces.Prefix.WFS)
@SerialName("member")
@Serializable
data class FeatureMember(
    /** Raw XML content of the feature element (unknown domain). */
    @XmlValue(true)
    val content: Element? = null
) {
    fun properties(prefix: String): Map<String, String> {
        val elements =
            content?.getChildNodes()?.filter { it is Element }?.filter { (it as Element).prefix == prefix }
        return elements?.associate { (it as Element).localName to (it.getTextContent()?.trim() ?: "") } ?: emptyMap()
    }

    fun geometry(prefix: String?, localPart: String, xml: XML = XML()): Geometry? {
        val geomElement = content?.getElementsByTagName(if (prefix != null) "$prefix:$localPart" else localPart)
        val geomNode = geomElement?.firstOrNull() as? Element
        val node = geomNode?.getChildNodes()?.firstOrNull { it is Element } as? Element
        val serialized = node?.let { xml.encodeToString(it) }
        return serialized?.let { xml.decodeFromString<Geometry>(it) }
    }

    fun geometry(localPart: String, xml: XML = XML()): Geometry? {
        return geometry(null, localPart, xml)
    }
}