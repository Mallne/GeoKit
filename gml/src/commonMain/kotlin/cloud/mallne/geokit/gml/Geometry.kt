package cloud.mallne.geokit.gml

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

/**
 * Generic GML geometries (GML 3.2 aligned) kept intentionally simple and unspecialized.
 * XML mapping is provided via xmlutil annotations.
 */
@Serializable
sealed interface Geometry {
    val srsName: String?
}

/** A coordinate position with arbitrary dimensionality (2D, 3D, …). */
@XmlSerialName("pos", GmlNamespaces.GML, "gml")
@Serializable
data class Pos(
    @XmlValue
    @Serializable(with = SpaceSeparatedDoublesSerializer::class)
    val values: List<Double>
)

/** A list of positions (flattened as space-separated doubles). */
@XmlSerialName("posList", GmlNamespaces.GML, "gml")
@Serializable
data class PosList(
    @XmlValue
    @Serializable(with = SpaceSeparatedDoublesSerializer::class)
    val values: List<Double>,
    /** Number of ordinates per position (e.g., 2 for 2D, 3 for 3D). */
    val srsDimension: Int = 2
)

@XmlSerialName("Point", GmlNamespaces.GML, "gml")
@Serializable
data class Point(
    @XmlElement(true)
    val pos: Pos,
    override val srsName: String? = null
) : Geometry

@XmlSerialName("LineString", GmlNamespaces.GML, "gml")
@Serializable
data class LineString(
    @XmlElement(true)
    val posList: PosList,
    override val srsName: String? = null
) : Geometry

@XmlSerialName("LinearRing", GmlNamespaces.GML, "gml")
@Serializable
data class LinearRing(
    @XmlElement(true)
    val posList: PosList,
    override val srsName: String? = null
) : Geometry

@XmlSerialName("Polygon", GmlNamespaces.GML, "gml")
@Serializable
data class Polygon(
    @XmlElement(true)
    val exterior: LinearRing,
    @XmlElement(true)
    val interior: List<LinearRing> = emptyList(),
    override val srsName: String? = null
) : Geometry

@XmlSerialName("MultiPoint", GmlNamespaces.GML, "gml")
@Serializable
data class MultiPoint(
    @XmlElement(true)
    val pointMember: List<Point>,
    override val srsName: String? = null
) : Geometry

@XmlSerialName("MultiLineString", GmlNamespaces.GML, "gml")
@Serializable
data class MultiLineString(
    @XmlElement(true)
    val lineStringMember: List<LineString>,
    override val srsName: String? = null
) : Geometry

@XmlSerialName("MultiPolygon", GmlNamespaces.GML, "gml")
@Serializable
data class MultiPolygon(
    @XmlElement(true)
    val polygonMember: List<Polygon>,
    override val srsName: String? = null
) : Geometry

/** Text-only corner elements and Envelope wrapper. */
@XmlSerialName("lowerCorner", GmlNamespaces.GML, "gml")
@Serializable
data class LowerCorner(
    @XmlValue
    @Serializable(with = SpaceSeparatedDoublesSerializer::class)
    val values: List<Double>
)

@XmlSerialName("upperCorner", GmlNamespaces.GML, "gml")
@Serializable
data class UpperCorner(
    @XmlValue
    @Serializable(with = SpaceSeparatedDoublesSerializer::class)
    val values: List<Double>
)

/** Bounding box envelope using 2 corner positions. */
@XmlSerialName("Envelope", GmlNamespaces.GML, "gml")
@Serializable
data class Envelope(
    @XmlElement(true)
    val lowerCorner: LowerCorner,
    @XmlElement(true)
    val upperCorner: UpperCorner,
    override val srsName: String? = null
) : Geometry
