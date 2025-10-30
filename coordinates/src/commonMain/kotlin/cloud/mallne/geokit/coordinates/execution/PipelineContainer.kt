package cloud.mallne.geokit.coordinates.execution

data class PipelineContainer<In, Out>(
    val data: In,
    val pipeline: Pipeline<In, Out>
) : Pipeline<In, Out> by pipeline {
    fun execute(): Out = execute(data)
}
