package cloud.mallne.geokit.ogc.model.gml.geometry

import kotlinx.serialization.Serializable

/**
 * Intermediate sealed class representing GML Surface objects (Polygons, MultiSurfaces, etc.).
 * This is crucial for strong compile-time type checking for the SurfaceMember.
 */
@Serializable
sealed class AbstractSurfaceType : AbstractGeometricPrimitiveType()