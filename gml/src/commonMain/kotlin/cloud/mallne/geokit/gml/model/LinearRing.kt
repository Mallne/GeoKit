package cloud.mallne.geokit.gml.model

import cloud.mallne.geokit.gml.SpaceSeparatedDoublesSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

//@XmlSerialName("LinearRing", Namespaces.GML, Namespaces.Prefix.GML)
@SerialName("LinearRing")
@Serializable
data class LinearRing(
    @XmlElement
    @Serializable(with = SpaceSeparatedDoublesSerializer::class)
    override val posList: List<Double>,
    val srsName: String? = null,
    //@XmlSerialName("id", namespace = Namespaces.GML, prefix = Namespaces.Prefix.GML)
    val id: String? = null,
) : DirectPositionList