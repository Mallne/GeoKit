package cloud.mallne.geokit.ogc.model.fes

import cloud.mallne.geokit.ogc.Namespaces
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@XmlSerialName("Filter", Namespaces.FES, Namespaces.Prefix.FES)
@Serializable
data class Filter(
    @XmlElement
    val statialOps: SpatialOpsType? = null,
    @XmlElement
    val comparisonOps: ComparisonOpsType? = null,
    @XmlElement
    val temporalOps: TemporalOpsType? = null,
    @XmlElement
    val logicOps: LogicOpsType? = null,
    @XmlElement
    val extensionOps: ExtensionOpsType? = null,
) : AbstractSelectionClauseType()