package cloud.mallne.geokit.geojson.serialization

import cloud.mallne.geokit.geojson.*
import kotlinx.serialization.*
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.JsonEncoder

internal class FeatureSerializer<T : Geometry?, P : @Serializable Any?>(
    private val geometrySerializer: KSerializer<T>,
    private val propertiesSerializer: KSerializer<P>,
) : KSerializer<Feature<T, P>> {
    private val serialName: String = "Feature"
    private val typeSerializer = String.serializer()
    private val bboxSerializer = BoundingBox.serializer().nullable
    private val idSerializer = FeatureIdSerializer.nullable

    // special sentinel for nullable values
    private val uninitialized = Any()

    init {
        if (propertiesSerializer.descriptor.kind !is StructureKind)
            throw SerializationException(
                "Expected Feature.properties to serialize to a structure, got ${propertiesSerializer.descriptor.kind}"
            )
    }

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor(serialName) {
            element("type", typeSerializer.descriptor)
            element("geometry", geometrySerializer.descriptor)
            element("properties", propertiesSerializer.descriptor, isOptional = !GeoJson.STRICT)
            element("id", idSerializer.descriptor, isOptional = true)
            element("bbox", bboxSerializer.descriptor, isOptional = true)
        }

    override fun serialize(encoder: Encoder, value: Feature<T, P>) {
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(descriptor, 0, typeSerializer, serialName)
            encodeSerializableElement(descriptor, 1, geometrySerializer, value.geometry)
            encodeSerializableElement(descriptor, 2, propertiesSerializer, value.properties)
            if (value.id != null || encoder !is JsonEncoder)
                encodeSerializableElement(descriptor, 3, idSerializer, value.id)
            if (value.bbox != null || encoder !is JsonEncoder)
                encodeSerializableElement(descriptor, 4, bboxSerializer, value.bbox)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): Feature<T, P> {
        return decoder.decodeStructure(descriptor) {
            var type: String? = null
            var bbox: BoundingBox? = null
            var geometry: Any? = uninitialized
            var properties: Any? = uninitialized
            var id: FeatureId? = null

            if (decodeSequentially()) {
                type = decodeSerializableElement(descriptor, 0, typeSerializer)
                geometry = decodeSerializableElement(descriptor, 1, geometrySerializer)
                properties = decodeSerializableElement(descriptor, 2, propertiesSerializer)
                id = decodeSerializableElement(descriptor, 3, idSerializer)
                bbox = decodeSerializableElement(descriptor, 4, bboxSerializer)
            } else {
                while (true) when (val index = decodeElementIndex(descriptor)) {
                    CompositeDecoder.DECODE_DONE -> break
                    0 -> type = decodeSerializableElement(descriptor, 0, typeSerializer)
                    1 -> geometry = decodeSerializableElement(descriptor, 1, geometrySerializer)
                    2 -> properties = decodeSerializableElement(descriptor, 2, propertiesSerializer)
                    3 -> id = decodeSerializableElement(descriptor, 3, idSerializer)
                    4 -> bbox = decodeSerializableElement(descriptor, 4, bboxSerializer)
                    else -> throw SerializationException("Unknown index $index")
                }
            }

            if (type == null) throw MissingFieldException("type", serialName)
            if (geometry == uninitialized) throw MissingFieldException("geometry", serialName)
            if (properties == uninitialized && GeoJson.STRICT)
                throw MissingFieldException("properties", serialName)

            if (type != serialName)
                throw SerializationException("Expected type $serialName but found $type")

            @Suppress("UNCHECKED_CAST")
            Feature(
                geometry as T,
                if (properties == uninitialized) null as P else properties as P,
                id,
                bbox,
            )
        }
    }
}
