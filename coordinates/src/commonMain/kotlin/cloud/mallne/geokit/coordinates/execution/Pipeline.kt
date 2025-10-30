package cloud.mallne.geokit.coordinates.execution

interface Pipeline<In, Out> {
    fun execute(input: In): Out
}