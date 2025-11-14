package cloud.mallne.geokit.ogc.model.gml.geometry

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.gml.CurveSegments
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@XmlSerialName("Curve", Namespaces.GML, Namespaces.Prefix.GML)
@Serializable
data class Curve(
    @XmlSerialName("segments", Namespaces.GML, Namespaces.Prefix.GML)
    val segments: CurveSegments,
    override val srsName: String? = null,
    override val id: String? = null,
    override val srsDimension: Int? = null,
    override val axisLabels: List<String> = listOf(),
    override val gid: String? = null,
    override val uomLabels: List<String> = listOf(),
) : AbstractCurveType()