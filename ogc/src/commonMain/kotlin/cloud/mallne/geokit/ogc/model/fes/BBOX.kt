package cloud.mallne.geokit.ogc.model.fes

import cloud.mallne.geokit.ogc.Namespaces
import cloud.mallne.geokit.ogc.model.Envelope
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