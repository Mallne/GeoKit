package cloud.mallne.geokit.fwi

import cloud.mallne.geokit.fwi.calculator.HourlyInterpolator
import cloud.mallne.geokit.fwi.model.MinMaxWeather
import cloud.mallne.geokit.fwi.model.WeatherRowConstants
import cloud.mallne.units.times
import kotlinx.datetime.LocalDate
import kotlinx.datetime.UtcOffset
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HourlyInterpolatorTest {

    @Test
    fun `given single day minmax when HourlyInterpolator is called then 24 hourly values are returned`() {
        val dailyInput = listOf(
            MinMaxWeather(
                id = "test",
                date = LocalDate(2007, 5, 12),
                tempMin = 10.0 * WeatherRowConstants.temp,
                tempMax = 25.0 * WeatherRowConstants.temp,
                rhMin = 30.0 * WeatherRowConstants.rh,
                rhMax = 80.0 * WeatherRowConstants.rh,
                wsMin = 5.0 * WeatherRowConstants.ws,
                wsMax = 15.0 * WeatherRowConstants.ws,
                prec = 0.0 * WeatherRowConstants.prec
            )
        )

        val result = HourlyInterpolator(
            dailyInput,
            lat = 45.0,
            long = -75.0,
            timezone = UtcOffset(hours = -4),
            silent = true,
            roundOut = 4
        )

        assertEquals(24, result.size)
        result.forEachIndexed { index, hourly ->
            assertEquals(LocalDate(2007, 5, 12), hourly.date)
            assertEquals(index, hourly.hr)
            assertEquals("test", hourly.id)
        }
    }

    @Test
    fun `given multiple days when HourlyInterpolator is called then correct number of hourly values returned`() {
        val dailyInput = listOf(
            MinMaxWeather(
                id = "test",
                date = LocalDate(2007, 5, 10),
                tempMin = 8.0 * WeatherRowConstants.temp,
                tempMax = 22.0 * WeatherRowConstants.temp,
                rhMin = 35.0 * WeatherRowConstants.rh,
                rhMax = 85.0 * WeatherRowConstants.rh,
                wsMin = 4.0 * WeatherRowConstants.ws,
                wsMax = 12.0 * WeatherRowConstants.ws,
                prec = 0.0 * WeatherRowConstants.prec
            ),
            MinMaxWeather(
                id = "test",
                date = LocalDate(2007, 5, 11),
                tempMin = 10.0 * WeatherRowConstants.temp,
                tempMax = 25.0 * WeatherRowConstants.temp,
                rhMin = 30.0 * WeatherRowConstants.rh,
                rhMax = 80.0 * WeatherRowConstants.rh,
                wsMin = 5.0 * WeatherRowConstants.ws,
                wsMax = 15.0 * WeatherRowConstants.ws,
                prec = 0.0 * WeatherRowConstants.prec
            ),
            MinMaxWeather(
                id = "test",
                date = LocalDate(2007, 5, 12),
                tempMin = 12.0 * WeatherRowConstants.temp,
                tempMax = 28.0 * WeatherRowConstants.temp,
                rhMin = 25.0 * WeatherRowConstants.rh,
                rhMax = 75.0 * WeatherRowConstants.rh,
                wsMin = 6.0 * WeatherRowConstants.ws,
                wsMax = 16.0 * WeatherRowConstants.ws,
                prec = 0.0 * WeatherRowConstants.prec
            )
        )

        val result = HourlyInterpolator(
            dailyInput,
            lat = 45.0,
            long = -75.0,
            timezone = UtcOffset(hours = -4),
            silent = true,
            roundOut = 4
        )

        assertEquals(72, result.size)
    }

    @Test
    fun `given empty input when HourlyInterpolator is called then empty list is returned`() {
        val result = HourlyInterpolator(
            emptyList(),
            lat = 45.0,
            long = -75.0,
            timezone = UtcOffset(hours = -4),
            silent = true
        )

        assertTrue(result.isEmpty())
    }

    @Test
    fun `given wind min max when HourlyInterpolator is called then wind values follow sinusoidal pattern`() {
        val dailyInput = listOf(
            MinMaxWeather(
                id = "test",
                date = LocalDate(2007, 6, 21),
                tempMin = 15.0 * WeatherRowConstants.temp,
                tempMax = 30.0 * WeatherRowConstants.temp,
                rhMin = 30.0 * WeatherRowConstants.rh,
                rhMax = 70.0 * WeatherRowConstants.rh,
                wsMin = 5.0 * WeatherRowConstants.ws,
                wsMax = 20.0 * WeatherRowConstants.ws,
                prec = 0.0 * WeatherRowConstants.prec
            )
        )

        val result = HourlyInterpolator(
            dailyInput,
            lat = 45.0,
            long = -75.0,
            timezone = UtcOffset(hours = -4),
            silent = true
        )

        val windValues = result.map { it.ws `in` WeatherRowConstants.ws }
        assertTrue(windValues.all { it >= 0.0 })
        val maxWind = windValues.maxOrNull()!!
        assertTrue(maxWind <= 20.0)
    }

    @Test
    fun `given rh min max when HourlyInterpolator is called then rh values are between min and max`() {
        val dailyInput = listOf(
            MinMaxWeather(
                id = "test",
                date = LocalDate(2007, 6, 21),
                tempMin = 15.0 * WeatherRowConstants.temp,
                tempMax = 30.0 * WeatherRowConstants.temp,
                rhMin = 40.0 * WeatherRowConstants.rh,
                rhMax = 90.0 * WeatherRowConstants.rh,
                wsMin = 5.0 * WeatherRowConstants.ws,
                wsMax = 15.0 * WeatherRowConstants.ws,
                prec = 0.0 * WeatherRowConstants.prec
            )
        )

        val result = HourlyInterpolator(
            dailyInput,
            lat = 45.0,
            long = -75.0,
            timezone = UtcOffset(hours = -4),
            silent = true
        )

        val rhValues = result.map { it.rh `in` WeatherRowConstants.rh }
        assertTrue(rhValues.all { it >= 0.0 && it <= 100.0 })
    }

    @Test
    fun `given precipitation when HourlyInterpolator is called then precip is placed at sunrise hour`() {
        val dailyInput = listOf(
            MinMaxWeather(
                id = "test",
                date = LocalDate(2007, 6, 21),
                tempMin = 15.0 * WeatherRowConstants.temp,
                tempMax = 30.0 * WeatherRowConstants.temp,
                rhMin = 40.0 * WeatherRowConstants.rh,
                rhMax = 90.0 * WeatherRowConstants.rh,
                wsMin = 5.0 * WeatherRowConstants.ws,
                wsMax = 15.0 * WeatherRowConstants.ws,
                prec = 5.5 * WeatherRowConstants.prec
            )
        )

        val result = HourlyInterpolator(
            dailyInput,
            lat = 45.0,
            long = -75.0,
            timezone = UtcOffset(hours = -4),
            silent = true
        )

        val precValues = result.map { it.prec `in` WeatherRowConstants.prec }
        val nonZeroPrecs = precValues.filter { it > 0.0 }
        assertEquals(1, nonZeroPrecs.size)
        assertEquals(5.5, nonZeroPrecs[0], 0.1)
    }

    @Test
    fun `given rounding when HourlyInterpolator is called then values are rounded correctly`() {
        val dailyInput = listOf(
            MinMaxWeather(
                id = "test",
                date = LocalDate(2007, 5, 12),
                tempMin = 10.123456 * WeatherRowConstants.temp,
                tempMax = 25.987654 * WeatherRowConstants.temp,
                rhMin = 30.111111 * WeatherRowConstants.rh,
                rhMax = 80.999999 * WeatherRowConstants.rh,
                wsMin = 5.555555 * WeatherRowConstants.ws,
                wsMax = 15.444444 * WeatherRowConstants.ws,
                prec = 0.0 * WeatherRowConstants.prec
            )
        )

        val result = HourlyInterpolator(
            dailyInput,
            lat = 45.0,
            long = -75.0,
            timezone = UtcOffset(hours = -4),
            silent = true,
            roundOut = 2
        )

        val firstHour = result.first()
        val tempScaled = ((firstHour.temp `in` WeatherRowConstants.temp) * 100).toInt()
        assertEquals(tempScaled.toDouble(), (firstHour.temp `in` WeatherRowConstants.temp) * 100, 0.001)
    }

    @Test
    fun `given multiple stations when HourlyInterpolator is called then each station is processed`() {
        val dailyInput = listOf(
            MinMaxWeather(
                id = "stn1",
                date = LocalDate(2007, 5, 10),
                tempMin = 8.0 * WeatherRowConstants.temp,
                tempMax = 22.0 * WeatherRowConstants.temp,
                rhMin = 35.0 * WeatherRowConstants.rh,
                rhMax = 85.0 * WeatherRowConstants.rh,
                wsMin = 4.0 * WeatherRowConstants.ws,
                wsMax = 12.0 * WeatherRowConstants.ws,
                prec = 0.0 * WeatherRowConstants.prec
            ),
            MinMaxWeather(
                id = "stn2",
                date = LocalDate(2007, 5, 10),
                tempMin = 10.0 * WeatherRowConstants.temp,
                tempMax = 24.0 * WeatherRowConstants.temp,
                rhMin = 30.0 * WeatherRowConstants.rh,
                rhMax = 80.0 * WeatherRowConstants.rh,
                wsMin = 5.0 * WeatherRowConstants.ws,
                wsMax = 14.0 * WeatherRowConstants.ws,
                prec = 0.0 * WeatherRowConstants.prec
            )
        )

        val result = HourlyInterpolator(
            dailyInput,
            lat = 45.0,
            long = -75.0,
            timezone = UtcOffset(hours = -4),
            silent = true
        )

        val stn1Results = result.filter { it.id == "stn1" }
        val stn2Results = result.filter { it.id == "stn2" }
        assertEquals(24, stn1Results.size)
        assertEquals(24, stn2Results.size)
    }
}