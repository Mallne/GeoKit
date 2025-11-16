package cloud.mallne.geokit.ogc.model.gml.segment

import cloud.mallne.geokit.ogc.model.gml.DirectPositionType
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
sealed class AbstractCurveSegmentType {
    abstract val numDerivativeInterior: Int?
    abstract val numDerivativesAtEnd: Int?
    abstract val numDerivativesAtStart: Int?
    abstract val positions: List<DirectPositionType>
}