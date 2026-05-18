package cloud.mallne.geokit.fwi.model

enum class FWIClassification {
    VeryLow, Low, Moderate, High, VeryHigh, Extreme;

    companion object {
        fun classify(value: Double): FWIClassification = when {
            value < 5.2 -> VeryLow
            value < 11.2 -> Low
            value < 21.3 -> Moderate
            value < 38.0 -> High
            value < 50.0 -> VeryHigh
            else -> Extreme
        }
    }
}