package cloud.mallne.geokit.ogc

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Serializer that encodes/decodes a list of doubles as a single space-separated string,
 * suitable for GML pos/posList textual content.
 */
object SpaceSeparatedDoublesSerializer : KSerializer<List<Double>> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("SpaceSeparatedDoubles", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: List<Double>) {
        val s = value.joinToString(separator = " ") { d ->
            // Avoid trailing .0 for integers, but keep precision otherwise
            val asLong = d.toLong()
            if (asLong.toDouble() == d) asLong.toString() else d.toString()
        }
        encoder.encodeString(s)
    }

    override fun deserialize(decoder: Decoder): List<Double> {
        val s = decoder.decodeString()
        if (s.isBlank()) return emptyList()
        val ss = s.split(Regex("\\s+"))
        val sss = ss.mapNotNull { it.toDoubleOrNull() }
        return sss
    }
}

/**
 * Serializer that encodes/decodes a list of QNames or typenames as a single space-separated string
 * for WFS Query@typeNames attribute. This is kept generic (strings in, strings out).
 */
object SpaceSeparatedStringsSerializer : KSerializer<List<String>> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("SpaceSeparatedStrings", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: List<String>) {
        encoder.encodeString(value.joinToString(" "))
    }

    override fun deserialize(decoder: Decoder): List<String> {
        val s = decoder.decodeString()
        if (s.isBlank()) return emptyList()
        return s.split(Regex("\\s+"))
    }
}
