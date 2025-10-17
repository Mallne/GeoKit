package cloud.mallne.geokit.gml

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

/**
 * Generic GML Feature base type (whitewall: no domain-specific fields).
 * This is a simplified, library-internal representation that can be mapped to GML 3.2.
 */
@XmlSerialName("AbstractFeature", GmlNamespaces.GML, "gml")
@Serializable
open class FeatureType(
    /** gml:id if available */
    val id: String? = null,
    /** Optional human-readable name/description */
    val name: String? = null,
    val description: String? = null,
    /** BoundedBy as Envelope if present */
    @XmlElement(true)
    val boundedBy: Envelope? = null,
    /** Primary geometry of this feature, if known */
    @XmlElement(true)
    val geometry: Geometry? = null,
    /**
     * Arbitrary attributes/properties for unspecialized usage.
     * Keep keys simple (local names); namespace-aware mapping can be added later.
     */
    val properties: Map<String, String> = emptyMap()
)

/** A lightweight member wrapper that may appear in collections. */
@XmlSerialName("featureMember", GmlNamespaces.GML, "gml")
@Serializable
data class FeatureMember<T : FeatureType>(
    @XmlElement(true)
    val feature: T
)

/** GML 3.2 FeatureCollection equivalent kept generic. */
@XmlSerialName("FeatureCollection", GmlNamespaces.GML, "gml")
@Serializable
data class FeatureCollection<T : FeatureType>(
    val id: String? = null,
    val timeStamp: String? = null,
    val numberMatched: Long? = null,
    val numberReturned: Long? = null,
    @XmlElement(true)
    val boundedBy: Envelope? = null,
    @XmlElement(true)
    val members: List<FeatureMember<T>> = emptyList()
)
