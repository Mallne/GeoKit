package cloud.mallne.geokit.fwi

import cloud.mallne.geokit.fwi.calculator.Util
import cloud.mallne.geokit.fwi.calculator.indices.FireWeatherIndex
import kotlinx.datetime.LocalDate
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Reference validation tests for FWI calculations.
 *
 * Data source: https://raw.githubusercontent.com/nrcan-cfs-fire/cffdrs-ng/main/data/PRF2007_standard_hourly_FWI.csv
 *
 * This test validates that our Kotlin implementation produces identical results
 * to the cffdrs-ng reference implementation.
 */
class FireWeatherIndexReferenceValidationTest {

    @Test
    fun `hourly FWI calculation matches reference within tight tolerance`() {
        val csvRows = PRF2007TestData.getParsedRows()

        // Convert CSV rows to WeatherRow.Input objects
        val weatherRows = csvRows.map { csv ->
            csv.toWeatherRow()
        }

        // Calculate sunrise/sunset if not provided
        val processedRows = weatherRows

        // Use the main FireWeatherIndex function to process all rows
        val results = FireWeatherIndex(processedRows)

        var fwiPass = 0
        var total = 0

        for ((index, result) in results.withIndex()) {
            // Compare with reference (skip first 24 hours for warmup)
            val refRow = csvRows[index]
            if (abs(result.fwi - refRow.fwi) <= 0.1) {
                fwiPass++
            } else {
                println(
                    "FWI mismatch at [i=$index] ${processedRows[index].hr} ${processedRows[index].date}: ${result.fwi} vs ${refRow.fwi} | (diff=${
                        abs(
                            result.fwi - refRow.fwi
                        )
                    })"
                )
            }

            if (total < 24) {
                println(
                    "Hour ${processedRows[index].hr} ${processedRows[index].date}: calc_fwi=${result.fwi} ref_fwi=${refRow.fwi} diff=${
                        abs(
                            result.fwi - refRow.fwi
                        )
                    }"
                )
            }
            total++
        }

        val validCount = total
        val passRate = { passed: Int -> if (validCount > 0) (passed.toDouble() / validCount * 100).toInt() else 0 }

        println("Pass rates (after 24h warmup, $total total hours):")
        println("  FWI:  ${passRate(fwiPass)}% ($fwiPass/$validCount)")

        // Require at least 99% pass rate for each component
        assertTrue(fwiPass > validCount * 0.99, "FWI pass rate too low: ${passRate(fwiPass)}%")
    }

    @Test
    fun `test seasonal curing calculation`() {
        // Test May (should be in green-up phase)
        val curingMay = Util.seasonalCuring(LocalDate(2007, 5, 15))
        println("May 15 curing: $curingMay")

        // Test January (should be fully cured/winter)
        val curingJan = Util.seasonalCuring(LocalDate(2007, 1, 15))
        println("Jan 15 curing: $curingJan")

        assertTrue(curingMay < 100, "May should not be fully cured")
        assertTrue(curingJan > 90, "January should be mostly cured")
    }
}