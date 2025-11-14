package cloud.mallne.geokit.ogc.model.wfs

import kotlinx.serialization.Serializable

@Serializable
sealed class AbstractQueryExpressionType {
    abstract val handle: String?
}