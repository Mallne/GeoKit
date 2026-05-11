package cloud.mallne.geokit.fwi.calculator

import kotlin.math.pow

object BuildUpIndex {
    /**
     * Calculate Build-up Index (BUI)
     *
     * @param dmc [Double]            Duff Moisture Code
     * @param dc  [Double]            Drought Code
     * @return    [Double]            Build-up Index
     */
    operator fun invoke(dmc: Double, dc: Double): Double {
        // Initial BUI calculation with a zero-check to avoid division by zero
        var bui = if (dmc == 0.0 && dc == 0.0) {
            0.0
        } else {
            (0.8 * dc * dmc) / (dmc + 0.4 * dc)
        }

        // Adjustment if BUI is less than DMC
        if (bui < dmc) {
            val p = (dmc - bui) / dmc
            val cc = 0.92 + (0.0114 * dmc).pow(1.7)

            bui = dmc - cc * p

            // Ensure BUI does not drop below zero
            if (bui < 0.0) {
                bui = 0.0
            }
        }

        return bui
    }
}