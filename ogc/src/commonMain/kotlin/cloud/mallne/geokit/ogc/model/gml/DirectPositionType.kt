package cloud.mallne.geokit.ogc.model.gml

import cloud.mallne.geokit.Vertex
import cloud.mallne.geokit.ogc.Extensions.toVertex
import cloud.mallne.geokit.ogc.Extensions.toVertices
import cloud.mallne.geokit.ogc.SpaceSeparatedDoublesSerializer
import cloud.mallne.geokit.ogc.SpaceSeparatedStringsSerializer
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
data class DirectPositionType(
    @XmlValue
    @Serializable(with = SpaceSeparatedDoublesSerializer::class)
    val value: List<Double>,
    @Serializable(with = SpaceSeparatedStringsSerializer::class)
    val axisLabels: List<String> = listOf(),
    val srsDimension: Int? = null,
    val srsName: String? = null,
    @Serializable(with = SpaceSeparatedStringsSerializer::class)
    val uomLabels: List<String> = listOf(),
) {
    fun toVertices(): List<Vertex> = value.toVertices()
    fun toVertex(): Vertex? = value.toVertex()
}