package cloud.mallne.geokit.ogc.model

internal object CoordinateParser {
    fun parse(str: String): List<Double> = str.split(Regex("\\s+")).map { it.toDouble() }
}