package cloud.mallne.geokit.ogc.model.fes

import cloud.mallne.geokit.ogc.Namespaces
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@XmlSerialName("Filter", Namespaces.FES, Namespaces.Prefix.FES)
@Serializable
data class Filter(
    @XmlElement
    val spatialOp: SpatialOpsType? = null,
    @XmlElement
    val comparisonOp: ComparisonOpsType? = null,
    @XmlElement
    val temporalOp: TemporalOpsType? = null,
    @XmlElement
    val logicOp: LogicOpsType? = null,
    @XmlElement
    val extensionOp: ExtensionOpsType? = null,
) : AbstractSelectionClauseType()