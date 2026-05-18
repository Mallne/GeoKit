package cloud.mallne.geokit.fwi

import cloud.mallne.geokit.fwi.calculator.MinMaxCalculator
import cloud.mallne.geokit.fwi.model.WeatherRow
import cloud.mallne.geokit.fwi.model.WeatherRowConstants
import cloud.mallne.units.times
import kotlinx.datetime.LocalDate
import kotlinx.datetime.UtcOffset
import kotlin.test.Test
import kotlin.test.assertEquals

class MinMaxCalculatorTest {

    @Test
    fun `given temp and rh when tempMinMax is called then correct min max temperatures are returned`() {
        val tempNoon = 24.55 * WeatherRowConstants.temp
        val rhNoon = 41.53 * WeatherRowConstants.rh

        val result = MinMaxCalculator(
            listOf(
                WeatherRow.Input(
                    id = "test",
                    date = LocalDate(2007, 5, 12),
                    hr = 12,
                    temp = tempNoon,
                    rh = rhNoon,
                    ws = 10.53 * WeatherRowConstants.ws,
                    prec = 0.0 * WeatherRowConstants.prec,
                    lat = 45.996,
                    long = -77.427,
                    timezone = UtcOffset(hours = -4)
                )
            ),
            silent = true,
            roundOut = 4
        )

        assertEquals(1, result.size)
        val mm = result[0]

        val tempRange =
            17.0 - 0.16 * (rhNoon `in` WeatherRowConstants.rh) + 0.22 * (tempNoon `in` WeatherRowConstants.temp)
        val expectedTempMax = (tempNoon `in` WeatherRowConstants.temp) + 2.0
        val expectedTempMin = expectedTempMax - tempRange

        assertEquals(expectedTempMin, mm.tempMin `in` WeatherRowConstants.temp, 0.001)
        assertEquals(expectedTempMax, mm.tempMax `in` WeatherRowConstants.temp, 0.001)
        assertEquals(0.15 * 10.53, mm.wsMin `in` WeatherRowConstants.ws, 0.001)
        assertEquals(1.25 * 10.53, mm.wsMax `in` WeatherRowConstants.ws, 0.001)
        assertEquals(0.0, mm.prec `in` WeatherRowConstants.prec)
    }

    @Test
    fun `given low temp range when tempMinMax is called then small range is used`() {
        val tempNoon = 3.0 * WeatherRowConstants.temp
        val rhNoon = 99.0 * WeatherRowConstants.rh

        val result = MinMaxCalculator(
            listOf(
                WeatherRow.Input(
                    id = "test",
                    date = LocalDate(2007, 5, 12),
                    hr = 12,
                    temp = tempNoon,
                    rh = rhNoon,
                    ws = 5.0 * WeatherRowConstants.ws,
                    prec = 0.0 * WeatherRowConstants.prec,
                    lat = 45.996,
                    long = -77.427,
                    timezone = UtcOffset(hours = -4)
                )
            ),
            silent = true,
            roundOut = 4
        )

        val tempRange =
            17.0 - 0.16 * (rhNoon `in` WeatherRowConstants.rh) + 0.22 * (tempNoon `in` WeatherRowConstants.temp)
        assertEquals(true, tempRange <= 2.0)

        val mm = result[0]
        assertEquals((tempNoon `in` WeatherRowConstants.temp) - 1.0, mm.tempMin `in` WeatherRowConstants.temp, 0.001)
        assertEquals((tempNoon `in` WeatherRowConstants.temp) + 1.0, mm.tempMax `in` WeatherRowConstants.temp, 0.001)
    }

    @Test
    fun `given high rh when tempMinMax is called then wide temperature range is calculated`() {
        val tempNoon = 15.0 * WeatherRowConstants.temp
        val rhNoon = 90.0 * WeatherRowConstants.rh

        val result = MinMaxCalculator(
            listOf(
                WeatherRow.Input(
                    id = "test",
                    date = LocalDate(2007, 5, 12),
                    hr = 12,
                    temp = tempNoon,
                    rh = rhNoon,
                    ws = 8.0 * WeatherRowConstants.ws,
                    prec = 0.0 * WeatherRowConstants.prec,
                    lat = 45.996,
                    long = -77.427,
                    timezone = UtcOffset(hours = -4)
                )
            ),
            silent = true,
            roundOut = 4
        )

        val tempRange =
            17.0 - 0.16 * (rhNoon `in` WeatherRowConstants.rh) + 0.22 * (tempNoon `in` WeatherRowConstants.temp)
        val expectedTempMax = (tempNoon `in` WeatherRowConstants.temp) + 2.0
        val expectedTempMin = expectedTempMax - tempRange

        val mm = result[0]
        assertEquals(expectedTempMin, mm.tempMin `in` WeatherRowConstants.temp, 0.001)
        assertEquals(expectedTempMax, mm.tempMax `in` WeatherRowConstants.temp, 0.001)
    }

    @Test
    fun `given multiple rows when MinMaxCalculator is called then all rows are processed`() {
        val rows = listOf(
            WeatherRow.Input(
                id = "stn1",
                date = LocalDate(2007, 5, 10),
                hr = 12,
                temp = 20.0 * WeatherRowConstants.temp,
                rh = 50.0 * WeatherRowConstants.rh,
                ws = 10.0 * WeatherRowConstants.ws,
                prec = 0.0 * WeatherRowConstants.prec,
                lat = 45.0,
                long = -75.0,
                timezone = UtcOffset(hours = -5)
            ),
            WeatherRow.Input(
                id = "stn1",
                date = LocalDate(2007, 5, 11),
                hr = 12,
                temp = 22.0 * WeatherRowConstants.temp,
                rh = 45.0 * WeatherRowConstants.rh,
                ws = 8.0 * WeatherRowConstants.ws,
                prec = 0.0 * WeatherRowConstants.prec,
                lat = 45.0,
                long = -75.0,
                timezone = UtcOffset(hours = -5)
            )
        )

        val results = MinMaxCalculator(rows, silent = true, roundOut = 4)

        assertEquals(2, results.size)
        assertEquals(LocalDate(2007, 5, 10), results[0].date)
        assertEquals(LocalDate(2007, 5, 11), results[1].date)
    }

    @Test
    fun `given rounding disabled when MinMaxCalculator is called then values are not rounded`() {
        val row = WeatherRow.Input(
            id = "test",
            date = LocalDate(2007, 5, 12),
            hr = 12,
            temp = 24.555 * WeatherRowConstants.temp,
            rh = 41.535 * WeatherRowConstants.rh,
            ws = 10.533 * WeatherRowConstants.ws,
            prec = 0.0 * WeatherRowConstants.prec,
            lat = 45.996,
            long = -77.427,
            timezone = UtcOffset(hours = -4)
        )

        val result = MinMaxCalculator(listOf(row), silent = true, roundOut = null)

        assertEquals(1, result.size)
        val mm = result[0]

        val tempRange = 17.0 - 0.16 * 41.535 + 0.22 * 24.555
        val expectedTempMax = 24.555 + 2.0
        val expectedTempMin = expectedTempMax - tempRange

        assertEquals(expectedTempMin, mm.tempMin `in` WeatherRowConstants.temp, 0.001)
        assertEquals(expectedTempMax, mm.tempMax `in` WeatherRowConstants.temp, 0.001)
    }
}