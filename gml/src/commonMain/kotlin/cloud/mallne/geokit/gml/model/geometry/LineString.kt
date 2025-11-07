package cloud.mallne.geokit.gml.model.geometry

import cloud.mallne.geokit.gml.SpaceSeparatedDoublesSerializer
import cloud.mallne.geokit.gml.model.DirectPositionList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

//@XmlSerialName("LineString", Namespaces.GML, Namespaces.Prefix.GML)
@SerialName("LineString")
@Serializable
data class LineString(
    override val srsName: String? = null,
    @XmlElement
    @Serializable(with = SpaceSeparatedDoublesSerializer::class)
    override val posList: List<Double>,
    override val id: String? = null,
) : DirectPositionList, Geometry()