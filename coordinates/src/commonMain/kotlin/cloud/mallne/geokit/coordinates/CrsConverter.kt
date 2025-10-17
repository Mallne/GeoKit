package cloud.mallne.geokit.coordinates

/**
 * Minimal, KMP-friendly Coordinate Reference System (CRS) converter.
 *
 * Currently supports:
 * - EPSG:4326 (WGS84/ETRS89 geographic lat,lon in degrees)
 *   → EPSG:25832 (ETRS89 / UTM zone 32N) returning easting/northing in meters.
 *   → EPSG:25833 (ETRS89 / UTM zone 33N) returning easting/northing in meters.
 *
 * Notes:
 * - ETRS89 uses the GRS80 ellipsoid. For typical applications, WGS84 and ETRS89
 *   are considered equivalent at meter-level precision in Europe. We therefore
 *   accept EPSG:4326 inputs as ETRS89 for conversion to EPSG:25832.
 * - Northern hemisphere only (zone 32N covers 6°E..12°E). No southern false
 *   northing is applied here.
 */
object CrsConverter {
    /**
     * Convert a coordinate from one CRS to another.
     *
     * @param fromEPSG CRS code of source coordinate (e.g., "EPSG:4326").
     * @param toEPSG CRS code of target coordinate (e.g., "EPSG:25832").
     * @param latitude For geographic CRS: latitude in degrees. For projected CRS: northing (Y) in meters.
     * @param longitude For geographic CRS: longitude in degrees. For projected CRS: easting (X) in meters.
     */
    fun convert(
        fromEPSG: String,
        toEPSG: String,
        latitude: Double,
        longitude: Double
    ): Pair<Double, Double> {
        val from = CRS.fromCode(fromEPSG)
            ?: error("Unsupported source CRS: $fromEPSG")
        val to = CRS.fromCode(toEPSG)
            ?: error("Unsupported target CRS: $toEPSG")
        return CRS.convert(cloud.mallne.geokit.Vertex(latitude, longitude), from, to)
    }

    /**
     * Forward projection from geographic lat/lon (degrees) to ETRS89 / UTM zone 32N (meters).
     *
     * Implemented using Transverse Mercator formulas with the GRS80 ellipsoid.
     *
     * @param latDeg Latitude in degrees
     * @param lonDeg Longitude in degrees
     * @return Pair(easting, northing) in meters
     */
    fun wgs84ToEtrs89Utm32(latDeg: Double, lonDeg: Double): Pair<Double, Double> {
        return wgs84ToEtrs89Utm(latDeg, lonDeg, 9.0, 6.0..12.0)
    }

    /**
     * Forward projection from geographic lat/lon (degrees) to ETRS89 / UTM zone 33N (meters).
     */
    fun wgs84ToEtrs89Utm33(latDeg: Double, lonDeg: Double): Pair<Double, Double> {
        return wgs84ToEtrs89Utm(latDeg, lonDeg, 15.0, 12.0..18.0)
    }

    private fun wgs84ToEtrs89Utm(
        latDeg: Double,
        lonDeg: Double,
        lambda0Deg: Double,
        validLon: ClosedFloatingPointRange<Double>
    ): Pair<Double, Double> {
        require(lonDeg in -180.0..180.0) { "Longitude must be between -180 and 180 degrees" }
        require(latDeg in -80.0..84.0) { "Latitude must be between -80 and 84 degrees for UTM" }
        require(lonDeg in validLon) { "Longitude $lonDeg° is outside the recommended UTM zone bounds $validLon" }

        // Constants for ETRS89 / GRS80
        val a = 6378137.0                    // semi-major axis [m]
        val f = 1.0 / 298.257222101          // flattening
        val e2 = f * (2.0 - f)               // first eccentricity squared
        val ep2 = e2 / (1.0 - e2)            // second eccentricity squared

        // UTM parameters
        val k0 = 0.9996
        val falseEasting = 500_000.0
        val falseNorthing = 0.0              // Northern hemisphere

        val degToRad = kotlin.math.PI / 180.0
        val phi = latDeg * degToRad
        val lambda = lonDeg * degToRad
        val lambda0 = lambda0Deg * degToRad

        val sinPhi = kotlin.math.sin(phi)
        val cosPhi = kotlin.math.cos(phi)
        val tanPhi = kotlin.math.tan(phi)

        val N = a / kotlin.math.sqrt(1.0 - e2 * sinPhi * sinPhi)
        val T = tanPhi * tanPhi
        val C = ep2 * cosPhi * cosPhi
        val A = cosPhi * (lambda - lambda0)

        // Meridional arc length (series expansion)
        val e4 = e2 * e2
        val e6 = e4 * e2

        val M = a * (
                (1 - e2 / 4.0 - 3.0 * e4 / 64.0 - 5.0 * e6 / 256.0) * phi
                        - (3.0 * e2 / 8.0 + 3.0 * e4 / 32.0 + 45.0 * e6 / 1024.0) * kotlin.math.sin(2.0 * phi)
                        + (15.0 * e4 / 256.0 + 45.0 * e6 / 1024.0) * kotlin.math.sin(4.0 * phi)
                        - (35.0 * e6 / 3072.0) * kotlin.math.sin(6.0 * phi)
                )

        val A2 = A * A
        val A3 = A2 * A
        val A4 = A2 * A2
        val A5 = A4 * A
        val A6 = A3 * A3

        val x = k0 * N * (
                A + (1 - T + C) * A3 / 6.0 + (5 - 18 * T + T * T + 72 * C - 58 * ep2) * A5 / 120.0
                ) + falseEasting

        val y = k0 * (
                M + N * tanPhi * (
                        A2 / 2.0 + (5 - T + 9 * C + 4 * C * C) * A4 / 24.0 + (61 - 58 * T + T * T + 600 * C - 330 * ep2) * A6 / 720.0
                        )
                ) + falseNorthing

        return x to y
    }

    /**
     * Inverse projection from ETRS89 / UTM (meters) to geographic lat/lon in degrees for a given zone.
     * Inputs are easting (X) and northing (Y) in meters.
     */
    private fun etrs89UtmToWgs84(easting: Double, northing: Double, lambda0Deg: Double): Pair<Double, Double> {
        // Constants for ETRS89 / GRS80
        val a = 6378137.0
        val f = 1.0 / 298.257222101
        val e2 = f * (2.0 - f)
        val ep2 = e2 / (1.0 - e2)

        val k0 = 0.9996
        val falseEasting = 500_000.0
        val falseNorthing = 0.0 // Northern hemisphere only

        val x = easting - falseEasting
        val y = northing - falseNorthing

        val M = y / k0
        val mu = M / (a * (1 - e2 / 4.0 - 3.0 * e2 * e2 / 64.0 - 5.0 * e2 * e2 * e2 / 256.0))

        val e1 = (1.0 - kotlin.math.sqrt(1.0 - e2)) / (1.0 + kotlin.math.sqrt(1.0 - e2))

        val J1 = (3.0 * e1 / 2.0 - 27.0 * e1 * e1 * e1 / 32.0)
        val J2 = (21.0 * e1 * e1 / 16.0 - 55.0 * e1 * e1 * e1 * e1 / 32.0)
        val J3 = (151.0 * e1 * e1 * e1 / 96.0)
        val J4 = (1097.0 * e1 * e1 * e1 * e1 / 512.0)

        val phi1 = mu +
                J1 * kotlin.math.sin(2.0 * mu) +
                J2 * kotlin.math.sin(4.0 * mu) +
                J3 * kotlin.math.sin(6.0 * mu) +
                J4 * kotlin.math.sin(8.0 * mu)

        val sinPhi1 = kotlin.math.sin(phi1)
        val cosPhi1 = kotlin.math.cos(phi1)
        val tanPhi1 = kotlin.math.tan(phi1)

        val N1 = a / kotlin.math.sqrt(1.0 - e2 * sinPhi1 * sinPhi1)
        val tmp = 1.0 - e2 * sinPhi1 * sinPhi1
        val denom = kotlin.math.sqrt(tmp)
        val R1 = a * (1.0 - e2) / (denom * denom * denom)
        val T1 = tanPhi1 * tanPhi1
        val C1 = ep2 * cosPhi1 * cosPhi1
        val D = x / (N1 * k0)

        val D2 = D * D
        val D3 = D2 * D
        val D4 = D2 * D2
        val D5 = D4 * D
        val D6 = D3 * D3

        val phi = phi1 - (N1 * tanPhi1 / R1) * (
                D2 / 2.0 - (5.0 + 3.0 * T1 + 10.0 * C1 - 4.0 * C1 * C1 - 9.0 * ep2) * D4 / 24.0 +
                        (61.0 + 90.0 * T1 + 298.0 * C1 + 45.0 * T1 * T1 - 252.0 * ep2 - 3.0 * C1 * C1) * D6 / 720.0
                )

        val lambda0 = lambda0Deg * kotlin.math.PI / 180.0
        val lambda = lambda0 + (
                D - (1.0 + 2.0 * T1 + C1) * D3 / 6.0 +
                        (5.0 - 2.0 * C1 + 28.0 * T1 - 3.0 * C1 * C1 + 8.0 * ep2 + 24.0 * T1 * T1) * D5 / 120.0
                ) / cosPhi1

        val radToDeg = 180.0 / kotlin.math.PI
        val lat = phi * radToDeg
        val lon = lambda * radToDeg
        return lat to lon
    }

    fun etrs89Utm32ToWgs84(easting: Double, northing: Double): Pair<Double, Double> =
        etrs89UtmToWgs84(easting, northing, 9.0)

    fun etrs89Utm33ToWgs84(easting: Double, northing: Double): Pair<Double, Double> =
        etrs89UtmToWgs84(easting, northing, 15.0)
}
