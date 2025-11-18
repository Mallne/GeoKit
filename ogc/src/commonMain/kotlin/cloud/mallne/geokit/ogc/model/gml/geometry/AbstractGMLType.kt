package cloud.mallne.geokit.ogc.model.gml.geometry

import cloud.mallne.geokit.ogc.Namespaces
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
sealed class AbstractGMLType {
    @XmlSerialName("id", namespace = Namespaces.GML, prefix = Namespaces.Prefix.GML)
    abstract val id: String?
}