package cloud.mallne.geokit.ogc.model.wfs

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.gml.geometry.AbstractGeometryType
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
        return propertyElements(prefix).associate { (it.localName ?: "") to (it.getTextContent()?.trim() ?: "") }
    }

    fun geometry(prefix: String?, localPart: String, xml: XML = XML.recommended_1_0()): AbstractGeometryType? {
        val geomNode = propertyElements(prefix).firstOrNull { it.localName == localPart }
        val node = geomNode?.getChildNodes()?.firstOrNull { it is Element } as? Element
        val serialized = node?.let { xml.encodeToString(it) }
        return serialized?.let { xml.decodeFromString<AbstractGeometryType>(it) }
    }

    fun geometry(localPart: String, xml: XML = XML.recommended_1_0()): AbstractGeometryType? {
        return geometry(null, localPart, xml)
    }

    private fun propertyElements(prefix: String?): List<Element> {
        val feature = content ?: return emptyList()
        val namespace = feature.getNamespaceURI()
        return feature.getChildNodes().filterIsInstance<Element>().filter {
            if (namespace.isNullOrEmpty()) prefix == null || it.prefix == prefix else it.getNamespaceURI() == namespace
        }
    }
}
