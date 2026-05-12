package cloud.mallne.geokit.fwi

import cloud.mallne.geokit.fwi.calculator.MinMaxCalculator
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class MinMaxCalculatorTest {

    @Test
    fun `given temp and rh when tempMinMax is called then correct min max temperatures are returned`() {
        val tempNoon = 24.55
        val rhNoon = 41.53

        val result = MinMaxCalculator(
            listOf(
                cloud.mallne.geokit.fwi.model.WeatherRow.Input(
                    id = "test",
                    date = LocalDate(2007, 5, 12),
                    hr = 12,
                    temp = tempNoon,
                    rh = rhNoon,
                    ws = 10.53,
                    prec = 0.0,
                    lat = 45.996,
                    long = -77.427,
                    timezone = -4.0
                )
            ),
            silent = true,
            roundOut = 4
        )

        assertEquals(1, result.size)
        val mm = result[0]

        val tempRange = 17.0 - 0.16 * rhNoon + 0.22 * tempNoon
        val expectedTempMax = tempNoon + 2.0
        val expectedTempMin = expectedTempMax - tempRange

        assertEquals(expectedTempMin, mm.tempMin, 0.001)
        assertEquals(expectedTempMax, mm.tempMax, 0.001)
        assertEquals(0.15 * 10.53, mm.wsMin, 0.001)
        assertEquals(1.25 * 10.53, mm.wsMax, 0.001)
        assertEquals(0.0, mm.prec)
    }

    @Test
    fun `given low temp range when tempMinMax is called then small range is used`() {
        val tempNoon = 3.0
        val rhNoon = 99.0

        val result = MinMaxCalculator(
            listOf(
                cloud.mallne.geokit.fwi.model.WeatherRow.Input(
                    id = "test",
                    date = LocalDate(2007, 5, 12),
                    hr = 12,
                    temp = tempNoon,
                    rh = rhNoon,
                    ws = 5.0,
                    prec = 0.0,
                    lat = 45.996,
                    long = -77.427,
                    timezone = -4.0
                )
            ),
            silent = true,
            roundOut = 4
        )

        val tempRange = 17.0 - 0.16 * rhNoon + 0.22 * tempNoon
        assertEquals(true, tempRange <= 2.0)

        val mm = result[0]
        assertEquals(tempNoon - 1.0, mm.tempMin, 0.001)
        assertEquals(tempNoon + 1.0, mm.tempMax, 0.001)
    }

    @Test
    fun `given high rh when tempMinMax is called then wide temperature range is calculated`() {
        val tempNoon = 15.0
        val rhNoon = 90.0

        val result = MinMaxCalculator(
            listOf(
                cloud.mallne.geokit.fwi.model.WeatherRow.Input(
                    id = "test",
                    date = LocalDate(2007, 5, 12),
                    hr = 12,
                    temp = tempNoon,
                    rh = rhNoon,
                    ws = 8.0,
                    prec = 0.0,
                    lat = 45.996,
                    long = -77.427,
                    timezone = -4.0
                )
            ),
            silent = true,
            roundOut = 4
        )

        val tempRange = 17.0 - 0.16 * rhNoon + 0.22 * tempNoon
        val expectedTempMax = tempNoon + 2.0
        val expectedTempMin = expectedTempMax - tempRange

        val mm = result[0]
        assertEquals(expectedTempMin, mm.tempMin, 0.001)
        assertEquals(expectedTempMax, mm.tempMax, 0.001)
    }

    @Test
    fun `given multiple rows when MinMaxCalculator is called then all rows are processed`() {
        val rows = listOf(
            cloud.mallne.geokit.fwi.model.WeatherRow.Input(
                id = "stn1",
                date = LocalDate(2007, 5, 10),
                hr = 12,
                temp = 20.0,
                rh = 50.0,
                ws = 10.0,
                prec = 0.0,
                lat = 45.0,
                long = -75.0,
                timezone = -5.0
            ),
            cloud.mallne.geokit.fwi.model.WeatherRow.Input(
                id = "stn1",
                date = LocalDate(2007, 5, 11),
                hr = 12,
                temp = 22.0,
                rh = 45.0,
                ws = 8.0,
                prec = 0.0,
                lat = 45.0,
                long = -75.0,
                timezone = -5.0
            )
        )

        val results = MinMaxCalculator(rows, silent = true, roundOut = 4)

        assertEquals(2, results.size)
        assertEquals(LocalDate(2007, 5, 10), results[0].date)
        assertEquals(LocalDate(2007, 5, 11), results[1].date)
    }

    @Test
    fun `given rounding disabled when MinMaxCalculator is called then values are not rounded`() {
        val row = cloud.mallne.geokit.fwi.model.WeatherRow.Input(
            id = "test",
            date = LocalDate(2007, 5, 12),
            hr = 12,
            temp = 24.555,
            rh = 41.535,
            ws = 10.533,
            prec = 0.0,
            lat = 45.996,
            long = -77.427,
            timezone = -4.0
        )

        val result = MinMaxCalculator(listOf(row), silent = true, roundOut = null)

        assertEquals(1, result.size)
        val mm = result[0]

        val tempRange = 17.0 - 0.16 * 41.535 + 0.22 * 24.555
        val expectedTempMax = 24.555 + 2.0
        val expectedTempMin = expectedTempMax - tempRange

        assertEquals(expectedTempMin, mm.tempMin, 0.001)
        assertEquals(expectedTempMax, mm.tempMax, 0.001)
    }
}