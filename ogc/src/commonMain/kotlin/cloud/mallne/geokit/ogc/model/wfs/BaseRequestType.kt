package cloud.mallne.geokit.ogc.model.wfs

import kotlinx.serialization.Serializable

@Serializable
sealed class BaseRequestType {
    abstract val handle: String?
    abstract val service: String?
    abstract val version: String?
    abstract val baseUrl: String?
}