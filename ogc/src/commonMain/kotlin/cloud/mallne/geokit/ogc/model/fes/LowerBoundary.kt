package cloud.mallne.geokit.ogc.model.fes

import cloud.mallne.geokit.ogc.Namespaces
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@SerialName("LowerBoundary")
@XmlSerialName("LowerBoundary", Namespaces.FES, Namespaces.Prefix.FES)
data class LowerBoundary(
    val literal: Literal,
)
