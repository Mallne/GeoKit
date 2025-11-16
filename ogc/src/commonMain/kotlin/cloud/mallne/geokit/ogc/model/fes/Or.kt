package cloud.mallne.geokit.ogc.model.fes

import cloud.mallne.geokit.ogc.Namespaces
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@SerialName("Or")
@XmlSerialName("Or", Namespaces.FES, Namespaces.Prefix.FES)
data class Or(
    @XmlElement
    override val spatialOps: List<SpatialOpsType> = listOf(),
    @XmlElement
    override val comparisonOps: List<ComparisonOpsType> = listOf(),
    @XmlElement
    override val temporalOps: List<TemporalOpsType> = listOf(),
    @XmlElement
    override val logicOps: List<LogicOpsType> = listOf(),
    @XmlElement
    override val extensionOps: List<ExtensionOpsType> = listOf(),
) : BinaryLogicOpType()