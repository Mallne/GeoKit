package cloud.mallne.geokit.coordinates

/**
 * Central registry for all generated CRS definitions and transformations.
 */
object CrsRegistry {
    val definitions = mutableMapOf<Int, CrsDefinition>()
    val transformations = mutableMapOf<Int, CrsTransformation>()
    val chains = mutableMapOf<Int, CrsTransformationChain>()

    fun registerDefinition(definition: CrsDefinition) {
        definitions[definition.epsgCode] = definition
    }

    fun registerTransformation(id: Int, transformation: CrsTransformation) {
        transformations[id] = transformation
    }

    fun registerChain(chain: CrsTransformationChain) {
        chains[chain.id] = chain
    }
}