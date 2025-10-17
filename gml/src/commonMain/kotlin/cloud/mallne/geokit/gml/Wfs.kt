package cloud.mallne.geokit.gml

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

/** Minimal WFS 2.0 request/response models, intentionally generic, with XML mapping. */

@XmlSerialName("GetFeature", GmlNamespaces.WFS, "wfs")
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
        ):
                GetFeature {
            val q = Query(typeNames = typeNames, srsName = srsName, filter = Filter(BBOX(valueReference, envelope)))
            return GetFeature(queries = listOf(q), count = count, startIndex = startIndex)
        }
    }
}

@XmlSerialName("Query", GmlNamespaces.WFS, "wfs")
@Serializable
data class Query(
    @Serializable(with = SpaceSeparatedStringsSerializer::class)
    val typeNames: List<String>,
    val srsName: String? = null,
    @XmlElement(true)
    val filter: Filter
)

@XmlSerialName("Filter", GmlNamespaces.FES, "fes")
@Serializable
data class Filter(
    @XmlElement(true)
    val bbox: BBOX
)

@XmlSerialName("BBOX", GmlNamespaces.FES, "fes")
@Serializable
data class BBOX(
    /** Property path to apply the spatial filter against. Keep generic; default can be "*" or a geometry name. */
    @XmlElement(true)
    val valueReference: ValueReference,
    @XmlElement(true)
    val envelope: Envelope
) {
    constructor(valueReference: String, envelope: Envelope) : this(ValueReference(valueReference), envelope)
}

@XmlSerialName("ValueReference", GmlNamespaces.FES, "fes")
@Serializable
data class ValueReference(
    @XmlValue val value: String
)

@XmlSerialName("member", GmlNamespaces.WFS, "wfs")
@Serializable
data class WfsFeatureMember<T : FeatureType>(
    @XmlElement(true)
    val member: T
)

@XmlSerialName("FeatureCollection", GmlNamespaces.WFS, "wfs")
@Serializable
data class WfsFeatureCollection<T : FeatureType>(
    val timeStamp: String? = null,
    val numberMatched: Long? = null,
    val numberReturned: Long? = null,
    @XmlElement(true)
    val boundedBy: Envelope? = null,
    @XmlElement(true)
    val members: List<WfsFeatureMember<T>> = emptyList()
)
