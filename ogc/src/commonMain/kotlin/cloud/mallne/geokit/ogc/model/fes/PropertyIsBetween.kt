package cloud.mallne.geokit.ogc.model.fes

import cloud.mallne.geokit.ogc.Namespaces
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@SerialName("PropertyIsBetween")
@XmlSerialName("PropertyIsBetween", Namespaces.FES, Namespaces.Prefix.FES)
data class PropertyIsBetween(
    val valueReference: ValueReference,
    val lowerBoundary: LowerBoundary,
    val upperBoundary: UpperBoundary,

    ) : ComparisonOpsType()
