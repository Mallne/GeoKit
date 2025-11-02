package cloud.mallne.geokit.coordinates.builtin

import cloud.mallne.geokit.coordinates.execution.IdentityLocator

enum class EPSGParameters(override val commonNames: List<String>, override val identity: String) : IdentityLocator {
    Tx(listOf("X - axis translation"), "EPSG:8605"),
    Ty(listOf("Y - axis translation"), "EPSG:8606"),
    Tz(listOf("Z - axis translation"), "EPSG:8607"),
    ;
}