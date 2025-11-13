package cloud.mallne.geokit.ogc.model

import cloud.mallne.geokit.ogc.model.geometry.Geometry
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

/**
 * Generic GML Feature base type (whitewall: no domain-specific fields).
 * This is a simplified, library-internal representation that can be mapped to GML 3.2.
 */
//@XmlSerialName("AbstractFeature", Namespaces.GML, Namespaces.Prefix.GML)
@SerialName("AbstractFeature")
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
    val geometry: Geometry? = null,
    /**
     * Arbitrary attributes/properties for unspecialized usage.
     * Keep keys simple (local names); namespace-aware mapping can be added later.
     */
    val properties: Map<String, String> = emptyMap()
)