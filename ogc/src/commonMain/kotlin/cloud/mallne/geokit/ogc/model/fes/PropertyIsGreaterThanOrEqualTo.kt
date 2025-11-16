package cloud.mallne.geokit.ogc.model.fes

import cloud.mallne.geokit.ogc.Namespaces
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@SerialName("PropertyIsGreaterThanOrEqualTo")
@XmlSerialName("PropertyIsGreaterThanOrEqualTo", Namespaces.FES, Namespaces.Prefix.FES)
data class PropertyIsGreaterThanOrEqualTo(
    override val valueReference: ValueReference,
    override val literal: Literal,
) : ComparisonOpsType(), BasicCompOp
