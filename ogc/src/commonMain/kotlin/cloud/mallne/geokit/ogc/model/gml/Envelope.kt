package cloud.mallne.geokit.ogc.model.gml

import cloud.mallne.geokit.ogc.Namespaces
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

/** Bounding box envelope using 2 corner positions. */
@XmlSerialName("Envelope", Namespaces.GML, Namespaces.Prefix.GML)
@SerialName("Envelope")
@Serializable
data class Envelope(
    @XmlSerialName(value = "lowerCorner", namespace = Namespaces.GML, prefix = Namespaces.Prefix.GML)
    @SerialName("lowerCorner")
    val lowerCorner: DirectPositionType,
    @XmlSerialName("upperCorner", Namespaces.GML, Namespaces.Prefix.GML)
    @SerialName("upperCorner")
    val upperCorner: DirectPositionType,
    val axisLabels: List<String> = listOf(),
    val srsDimension: Int? = null,
    val srsName: String? = null,
    val uomLabels: List<String> = listOf(),
    val id: String? = null,
)