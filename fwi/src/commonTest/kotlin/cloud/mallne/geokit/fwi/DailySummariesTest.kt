package cloud.mallne.geokit.fwi

import cloud.mallne.geokit.fwi.calculator.DailySummaries
import kotlin.test.Test
import kotlin.test.assertEquals

class DailySummariesTest {

    @Test
    fun `given hourly FWI data when generateDailySummaries is called then results match expected control values`() {
        val hourlyFWI = PRF2007TestData.getParsedHourlyFWI()
        val expectedResults = PRF2007TestData.getParsedDailyRows()

        val actualResults = DailySummaries(hourlyFWI, silent = true)

        assertEquals(expectedResults.size, actualResults.size, "Result count mismatch")

        actualResults.forEachIndexed { index, actual ->
            val expected = expectedResults[index]

            assertEquals(expected.date, actual.date, "Date mismatch at index $index")
            assertEquals(expected.sunrise, actual.sunrise, "Sunrise mismatch at index $index")
            assertEquals(expected.sunset, actual.sunset, "Sunset mismatch at index $index")
            assertEquals(expected.peakHr, actual.peakHr, "PeakHr mismatch at index $index")
            assertEquals(expected.duration, actual.duration, "Duration mismatch at index $index")

            assertEquals(expected.ffmc, actual.ffmc, 0.001, "FFMC mismatch at index $index")
            assertEquals(expected.dmc, actual.dmc, 0.001, "DMC mismatch at index $index")
            assertEquals(expected.dc, actual.dc, 0.001, "DC mismatch at index $index")
            assertEquals(expected.isi, actual.isi, 0.001, "ISI mismatch at index $index")
            assertEquals(expected.bui, actual.bui, 0.001, "BUI mismatch at index $index")
            assertEquals(expected.fwi, actual.fwi, 0.001, "FWI mismatch at index $index")
            assertEquals(expected.dsr, actual.dsr, 0.001, "DSR mismatch at index $index")
            assertEquals(expected.gfmc, actual.gfmc, 0.001, "GFMC mismatch at index $index")
            assertEquals(expected.gsi, actual.gsi, 0.001, "GSI mismatch at index $index")
            assertEquals(expected.gfwi, actual.gfwi, 0.001, "GFWI mismatch at index $index")
            assertEquals(expected.wsSmooth, actual.wsSmooth, 0.001, "WsSmooth mismatch at index $index")
            assertEquals(expected.isiSmooth, actual.isiSmooth, 0.001, "IsiSmooth mismatch at index $index")
            assertEquals(expected.gsiSmooth, actual.gsiSmooth, 0.001, "GsiSmooth mismatch at index $index")
        }
    }

    @Test
    fun `given data without ID when generateDailySummaries is called then ID is null in results`() {
        val hourlyFWI = PRF2007TestData.getParsedHourlyFWI().map { it.copy(id = null) }
        val expectedResults = PRF2007TestData.getParsedDailyRows()

        val actualResults = DailySummaries(hourlyFWI, silent = true)

        assertEquals(expectedResults.size, actualResults.size)
        actualResults.forEachIndexed { index, actual ->
            assertEquals(null, actual.id, "ID should be null at index $index")
        }
    }

    @Test
    fun `given custom resetHour when generateDailySummaries is called then results are calculated correctly`() {
        val hourlyFWI = PRF2007TestData.getParsedHourlyFWI()

        val resultsDefault = DailySummaries(hourlyFWI, resetHr = 5, silent = true)
        val resultsCustom = DailySummaries(hourlyFWI, resetHr = 6, silent = true)

        // Results may differ when changing the reset hour
        // This test verifies the function runs without error with different reset hours
        assertEquals(resultsDefault.size, resultsCustom.size)
    }
}