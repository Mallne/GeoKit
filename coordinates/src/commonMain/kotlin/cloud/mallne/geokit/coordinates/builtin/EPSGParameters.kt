package cloud.mallne.geokit.coordinates.builtin

import cloud.mallne.geokit.coordinates.execution.IdentityLocator

enum class EPSGParameters(override val commonNames: List<String>, override val identity: String) : IdentityLocator {
    Tx(listOf("X - axis translation"), "EPSG:8605"),
    Ty(listOf("Y - axis translation"), "EPSG:8606"),
    Tz(listOf("Z - axis translation"), "EPSG:8607"),
    Phi_o(listOf("Latitude of natural origin"), "EPSG:8801"),
    Lambda_o(listOf("Longitude of natural origin"), "EPSG:8802"),
    K_o(listOf("Scale factor at natural origin"), "EPSG:8805"),
    FE(listOf("False easting"), "EPSG:8806"),
    FN(listOf("Scale factor at natural origin"), "EPSG:8807"),
    ;
}