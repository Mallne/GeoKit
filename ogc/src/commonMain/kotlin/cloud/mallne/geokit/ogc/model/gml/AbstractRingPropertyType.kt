package cloud.mallne.geokit.ogc.model.gml

import cloud.mallne.geokit.ogc.model.gml.geometry.AbstractRingType
import kotlinx.serialization.Serializable

@Serializable
data class AbstractRingPropertyType(
    val ring: AbstractRingType
)