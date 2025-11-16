package cloud.mallne.geokit.ogc.model.fes

import cloud.mallne.geokit.ogc.Namespaces
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@SerialName("PropertyIsGreaterThan")
@XmlSerialName("PropertyIsGreaterThan", Namespaces.FES, Namespaces.Prefix.FES)
data class PropertyIsGreaterThan(
    override val valueReference: ValueReference,
    override val literal: Literal,
) : ComparisonOpsType(), BasicCompOp
