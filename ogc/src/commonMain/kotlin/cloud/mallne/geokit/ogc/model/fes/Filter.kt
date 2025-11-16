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
) : AbstractSelectionClauseType() {
    companion object {
        fun fromList(operators: List<AbstractOperatorType>): Filter {
            val spatialOps = mutableListOf<SpatialOpsType>()
            val comparisonOps = mutableListOf<ComparisonOpsType>()
            val temporalOps = mutableListOf<TemporalOpsType>()
            val logicOps = mutableListOf<LogicOpsType>()
            val extensionOps = mutableListOf<ExtensionOpsType>()
            for (operator in operators) {
                when (operator) {
                    is SpatialOpsType -> spatialOps.add(operator)
                    is ComparisonOpsType -> comparisonOps.add(operator)
                    is TemporalOpsType -> temporalOps.add(operator)
                    is LogicOpsType -> logicOps.add(operator)
                    is ExtensionOpsType -> extensionOps.add(operator)
                }
            }
            return Filter(
                spatialOp = spatialOps.firstOrNull(),
                comparisonOp = comparisonOps.firstOrNull(),
                temporalOp = temporalOps.firstOrNull(),
                logicOp = logicOps.firstOrNull(),
                extensionOp = extensionOps.firstOrNull(),
            )
        }
    }
}