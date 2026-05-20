package cloud.mallne.geokit.fwi.model

interface IndiceComponents {
    val ffmc: Double
    val dmc: Double
    val dc: Double
    val isi: Double
    val bui: Double
    val fwi: Double
    val fwiClassification: FWIClassification
        get() = FWIClassification.classify(fwi)
    val dsr: Double
    val gfmc: Double
    val gsi: Double
    val gfwi: Double
    val gfwiClassification: FWIClassification
        get() = FWIClassification.classify(gfwi)
}