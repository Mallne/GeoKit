package cloud.mallne.geokit.gml.model.fes

import cloud.mallne.geokit.gml.model.Envelope
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

//@XmlSerialName("BBOX", Namespaces.FES, Namespaces.Prefix.FES)
@Serializable
data class BBOX(
    /** Property path to apply the spatial filter against. Keep generic; default can be "*" or a geometry name. */
    @XmlElement(true)
    val valueReference: ValueReference,
    @XmlElement(true)
    val envelope: Envelope
) {
    constructor(valueReference: String, envelope: Envelope) : this(ValueReference(valueReference), envelope)
}