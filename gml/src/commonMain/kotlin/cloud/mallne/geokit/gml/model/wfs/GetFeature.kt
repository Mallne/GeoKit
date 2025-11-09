package cloud.mallne.geokit.gml.model.wfs

import cloud.mallne.geokit.gml.model.Envelope
import cloud.mallne.geokit.gml.model.fes.BBOX
import cloud.mallne.geokit.gml.model.fes.Filter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

/** Minimal WFS 2.0 request/response models, intentionally generic, with XML mapping. */
//@XmlSerialName("GetFeature", Namespaces.WFS, Namespaces.Prefix.WFS)
@SerialName("GetFeature")
@Serializable
data class GetFeature(
    val service: String = "WFS",
    val version: String = "2.0.0",
    @XmlElement(true)
    val queries: List<Query> = emptyList(),
    val count: Int? = null,
    val startIndex: Int? = null
) {
    companion object {
        /** Convenience helper: create a minimal GetFeature with a single Query constrained by an Envelope boundary. */
        fun withBbox(
            typeNames: List<String>,
            envelope: Envelope,
            srsName: String? = envelope.srsName,
            count: Int? = null,
            startIndex: Int? = null,
            valueReference: String = "*"
        ): GetFeature {
            val q = Query(typeNames = typeNames, srsName = srsName, filter = Filter(BBOX(valueReference, envelope)))
            return GetFeature(queries = listOf(q), count = count, startIndex = startIndex)
        }
    }
}