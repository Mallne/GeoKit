package cloud.mallne.geokit.ogc.model.gml.geometry

import kotlinx.serialization.Serializable

/**
 * A representation of the model object '***Abstract Geometric Primitive Type***'.
 *
 * This is the abstract root type of the geometric primitives. A geometric primitive is a geometric object that is not
 * decomposed further into other primitives in the system. All primitives are oriented in the direction implied by the sequence of their
 * coordinate tuples.
 */
@Serializable
sealed class AbstractGeometricPrimitiveType : AbstractGeometryType()