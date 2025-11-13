package cloud.mallne.geokit.ogc.model.segment

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.DirectPositionList
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
sealed class AbstractSegment : DirectPositionList {
    @XmlSerialName("posList", Namespaces.GML, Namespaces.Prefix.GML)
    abstract override val posList: List<Double>
}