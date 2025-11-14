package cloud.mallne.geokit.ogc.model.gml.geometry

import kotlinx.serialization.Serializable

@Serializable
sealed class AbstractGMLType {
    abstract val id: String?
}