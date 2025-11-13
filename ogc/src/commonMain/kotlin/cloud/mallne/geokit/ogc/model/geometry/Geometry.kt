package cloud.mallne.geokit.ogc.model.geometry

import kotlinx.serialization.Serializable

/**
 * Generic GML geometries (GML 3.2 aligned) kept intentionally simple and unspecialized.
 * XML mapping is provided via xmlutil annotations.
 */
@Serializable
sealed class Geometry {
    abstract val srsName: String?

    //@XmlSerialName("id", namespace = Namespaces.GML, prefix = Namespaces.Prefix.GML)
    abstract val id: String?
}