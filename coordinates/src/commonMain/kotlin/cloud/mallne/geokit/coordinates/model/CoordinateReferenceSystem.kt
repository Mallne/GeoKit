package cloud.mallne.geokit.coordinates.model

interface CoordinateReferenceSystem {
    val name: String
    val code: String
    val converter: CRSConverter
}