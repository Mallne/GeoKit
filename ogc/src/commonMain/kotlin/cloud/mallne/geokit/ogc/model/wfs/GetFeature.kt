package cloud.mallne.geokit.ogc.model.wfs

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.fes.BBOX
import cloud.mallne.geokit.ogc.model.fes.Filter
import cloud.mallne.geokit.ogc.model.gml.Envelope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

/** Minimal WFS 2.0 request/response models, intentionally generic, with XML mapping. */
@XmlSerialName("GetFeature", Namespaces.WFS, Namespaces.Prefix.WFS)
@SerialName("GetFeature")
@Serializable
data class GetFeature(
    override val service: String = "WFS",
    override val version: String = "2.0.0",
    @XmlElement(true)
    val queries: List<AbstractQueryExpressionType> = emptyList(),
    val count: Int? = null,
    val outputFormat: String? = null,
    val resolve: ResolveValueType? = null,
    val resolveTimeout: Int? = null,
    val resultType: ResultTypeType? = null,
    val startIndex: Int? = null,
    override val handle: String? = null,
    override val baseUrl: String? = null
) : BaseRequestType() {
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