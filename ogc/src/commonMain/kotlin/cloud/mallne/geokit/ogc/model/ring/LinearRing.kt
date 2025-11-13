package cloud.mallne.geokit.ogc.model.ring

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.SpaceSeparatedDoublesSerializer
import cloud.mallne.geokit.ogc.model.DirectPositionList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@XmlSerialName("LinearRing", Namespaces.GML, Namespaces.Prefix.GML)
@SerialName("LinearRing")
@Serializable
data class LinearRing(
    @XmlElement
    @Serializable(with = SpaceSeparatedDoublesSerializer::class)
    @XmlSerialName("posList", Namespaces.GML, Namespaces.Prefix.GML)
    override val posList: List<Double>,
    val srsName: String? = null,
    @XmlSerialName("id", namespace = Namespaces.GML, prefix = Namespaces.Prefix.GML)
    val id: String? = null,
) : DirectPositionList, AbstractRing()