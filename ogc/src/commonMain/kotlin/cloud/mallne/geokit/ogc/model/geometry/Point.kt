package cloud.mallne.geokit.ogc.model.geometry

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.SpaceSeparatedDoublesSerializer
import cloud.mallne.geokit.ogc.model.DirectPosition
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@XmlSerialName("Point", Namespaces.GML, Namespaces.Prefix.GML)
@SerialName("Point")
@Serializable
data class Point(
    override val srsName: String? = null,
    //@XmlSerialName("id", namespace = Namespaces.GML, prefix = Namespaces.Prefix.GML)
    override val id: String? = null,
    @XmlElement
    @Serializable(with = SpaceSeparatedDoublesSerializer::class)
    override val pos: List<Double>,
) : DirectPosition, Geometry()