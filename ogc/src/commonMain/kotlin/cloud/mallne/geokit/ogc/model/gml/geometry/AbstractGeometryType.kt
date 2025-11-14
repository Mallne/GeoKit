package cloud.mallne.geokit.ogc.model.gml.geometry

import kotlinx.serialization.Serializable

/**
 * A representation of the model object '***Abstract Geometry Type***'.
 *
 * All geometry elements are derived directly or indirectly from this abstract supertype. A geometry element may
 * have an identifying attribute ("gml:id"), a name (attribute "name") and a description (attribute "description"). It may be associated
 * with a spatial reference system (attribute "srsName"). The following rules shall be adhered: - Every geometry type shall derive
 * from this abstract type. - Every geometry element (i.e. an element of a geometry type) shall be directly or indirectly in the
 * substitution group of _Geometry.
 *
 */
@Serializable
sealed class AbstractGeometryType : AbstractGMLType() {
    abstract val srsDimension: Int?
    abstract val srsName: String?
    abstract val axisLabels: List<String>
    abstract val gid: String?
    abstract val uomLabels: List<String>
}