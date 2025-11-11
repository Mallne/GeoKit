package cloud.mallne.geokit.gml.model.wfs

import cloud.mallne.geokit.gml.Namespaces
import cloud.mallne.geokit.gml.SpaceSeparatedStringsSerializer
import cloud.mallne.geokit.gml.model.fes.Filter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@XmlSerialName("Query", Namespaces.WFS, Namespaces.Prefix.WFS)
@SerialName("Query")
@Serializable
data class Query(
    @Serializable(with = SpaceSeparatedStringsSerializer::class)
    val typeNames: List<String>,
    val srsName: String? = null,
    @XmlElement(true)
    val filter: Filter
)