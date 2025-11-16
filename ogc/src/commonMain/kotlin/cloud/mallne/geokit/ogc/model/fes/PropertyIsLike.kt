package cloud.mallne.geokit.ogc.model.fes

import cloud.mallne.geokit.ogc.Namespaces
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@SerialName("PropertyIsBetween")
@XmlSerialName("PropertyIsBetween", Namespaces.FES, Namespaces.Prefix.FES)
data class PropertyIsLike(
    val valueReference: ValueReference,
    val literal: Literal,
    val wildCard: String = "*",
    val singleChar: String = ".",
    val escapeChar: String = "\\",
) : ComparisonOpsType()
