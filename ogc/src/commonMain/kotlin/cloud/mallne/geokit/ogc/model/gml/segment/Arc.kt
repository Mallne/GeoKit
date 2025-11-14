package cloud.mallne.geokit.ogc.model.gml.segment

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.gml.DirectPositionType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("Arc", Namespaces.GML, Namespaces.Prefix.GML)
@SerialName("Arc")
sealed class Arc(
    @XmlSerialName("pos", Namespaces.GML, Namespaces.Prefix.GML)
    @SerialName("pos")
    val pos: List<DirectPositionType> = listOf(),
    @XmlSerialName("posList", Namespaces.GML, Namespaces.Prefix.GML)
    @SerialName("posList")
    val posList: List<DirectPositionType> = listOf(),
    override val numDerivativeInterior: Int? = null,
    override val numDerivativesAtEnd: Int? = null,
    override val numDerivativesAtStart: Int? = null
) : AbstractCurveSegmentType()