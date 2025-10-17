package cloud.mallne.geokit.coordinates

import cloud.mallne.geokit.Vertex

/**
 * Coordinate Reference Systems supported by this module.
 *
 * Use CRS.convert(vertex, from, to) to convert between systems.
 * To get a list of supported systems, use CRS.supported() or CRS.codes().
 */
enum class CRS(val code: String, val description: String) {
    /** Geographic WGS84/ETRS89 latitude/longitude in degrees */
    EPSG4326("EPSG:4326", "WGS84/ETRS89 geographic lat/lon (degrees)"),

    /** ETRS89 / UTM zone 32N, easting/northing in meters (Europe, 6°E..12°E) */
    EPSG25832("EPSG:25832", "ETRS89 / UTM zone 32N (meters)"),

    /** ETRS89 / UTM zone 33N, easting/northing in meters (Europe, 12°E..18°E) */
    EPSG25833("EPSG:25833", "ETRS89 / UTM zone 33N (meters)");

    companion object {
        /** All supported CRS values. */
        fun supported(): List<CRS> = entries.toList()

        /** All supported CRS EPSG codes as strings (e.g., "EPSG:4326"). */
        fun codes(): List<String> = entries.map { it.code }

        /**
         * Convert a coordinate from the [from] CRS to the [to] CRS.
         *
         * Currently implemented:
         * - EPSG:4326 -> EPSG:25832, returning Pair(easting, northing) in meters.
         *
         * For other conversions, this will throw an IllegalArgumentException for now.
         */
        fun convert(vertex: Vertex, from: CRS, to: CRS): Pair<Double, Double> = when {
            from == to -> {
                // Passthrough: keep ordering consistent with target CRS
                if (to == EPSG4326) vertex.latitude to vertex.longitude
                else vertex.longitude to vertex.latitude
            }

            from == EPSG4326 && to == EPSG25832 -> {
                CrsConverter.wgs84ToEtrs89Utm32(vertex.latitude, vertex.longitude)
            }

            from == EPSG4326 && to == EPSG25833 -> {
                CrsConverter.wgs84ToEtrs89Utm33(vertex.latitude, vertex.longitude)
            }

            from == EPSG25832 && to == EPSG4326 -> {
                // Vertex(latitude=Y=northing, longitude=X=easting)
                CrsConverter.etrs89Utm32ToWgs84(vertex.longitude, vertex.latitude)
            }

            from == EPSG25833 && to == EPSG4326 -> {
                CrsConverter.etrs89Utm33ToWgs84(vertex.longitude, vertex.latitude)
            }

            else -> error("Conversion from ${from.code} to ${to.code} is not implemented yet")
        }

        /** Resolve by EPSG code string (case-insensitive), or null if unknown. */
        fun fromCode(code: String): CRS? = entries.firstOrNull { it.code.equals(code, ignoreCase = true) }
    }
}
