package cloud.mallne.geokit.ogc.model.fes

import cloud.mallne.geokit.ogc.Namespaces
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@SerialName("PropertyIsLessThan")
@XmlSerialName("PropertyIsLessThan", Namespaces.FES, Namespaces.Prefix.FES)
data class PropertyIsLessThan(
    override val valueReference: ValueReference,
    override val literal: Literal,
) : ComparisonOpsType(), BasicCompOp
