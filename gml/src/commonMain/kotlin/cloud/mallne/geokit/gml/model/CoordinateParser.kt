package cloud.mallne.geokit.gml.model

internal object CoordinateParser {
    fun parse(str: String): List<Double> = str.split(Regex("\\s+")).map { it.toDouble() }
}