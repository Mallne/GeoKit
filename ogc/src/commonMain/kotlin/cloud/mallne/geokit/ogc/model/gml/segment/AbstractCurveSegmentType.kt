package cloud.mallne.geokit.ogc.model.gml.segment

import kotlinx.serialization.Serializable

@Serializable
sealed class AbstractCurveSegmentType {
    abstract val numDerivativeInterior: Int?
    abstract val numDerivativesAtEnd: Int?
    abstract val numDerivativesAtStart: Int?
}