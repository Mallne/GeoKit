package cloud.mallne.geokit.ogc.model.fes

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

@Serializable
sealed class BinaryLogicOpType : LogicOpsType() {
    @XmlElement
    abstract val spatialOps: List<SpatialOpsType>

    @XmlElement
    abstract val comparisonOps: List<ComparisonOpsType>

    @XmlElement
    abstract val temporalOps: List<TemporalOpsType>

    @XmlElement
    abstract val logicOps: List<LogicOpsType>

    @XmlElement
    abstract val extensionOps: List<ExtensionOpsType>
}