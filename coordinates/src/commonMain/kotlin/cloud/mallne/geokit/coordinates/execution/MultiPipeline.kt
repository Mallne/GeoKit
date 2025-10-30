package cloud.mallne.geokit.coordinates.execution

data class MultiPipeline<In, Out>(
    val pipelines: List<Pipeline<In, Out>>,
    val outInTransform: (Out) -> In,
) : Pipeline<In, Out> {
    override fun execute(input: In): Out {
        var current: Out? = null
        for (pipeline in pipelines) {
            current = pipeline.execute(current?.let { outInTransform(it) } ?: input)
        }
        return current!!
    }
}
