package cloud.mallne.geokit.ogc.model.geometry

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.SpaceSeparatedDoublesSerializer
import cloud.mallne.geokit.ogc.model.DirectPositionList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@XmlSerialName("LineString", Namespaces.GML, Namespaces.Prefix.GML)
@SerialName("LineString")
@Serializable
data class LineString(
    override val srsName: String? = null,
    @XmlElement
    @Serializable(with = SpaceSeparatedDoublesSerializer::class)
    @XmlSerialName("posList", Namespaces.GML, Namespaces.Prefix.GML)
    override val posList: List<Double>,
    override val id: String? = null,
) : DirectPositionList, Geometry()