package cloud.mallne.geokit.gml.model.fes

import cloud.mallne.geokit.gml.Namespaces
import cloud.mallne.geokit.gml.model.Envelope
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@XmlSerialName("BBOX", Namespaces.FES, Namespaces.Prefix.FES)
@Serializable
data class BBOX(
    /** Property path to apply the spatial filter against. Keep generic; default can be "*" or a geometry name. */
    @XmlElement(true)
    val valueReference: ValueReference? = null,
    @XmlElement(true)
    val envelope: Envelope
) {
    constructor(valueReference: String, envelope: Envelope) : this(ValueReference(valueReference), envelope)
}