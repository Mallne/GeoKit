package cloud.mallne.geokit.fwi.model

import cloud.mallne.units.Area.Companion.squareMeters
import cloud.mallne.units.Length.Companion.kilometers
import cloud.mallne.units.Length.Companion.millimeters
import cloud.mallne.units.Mass.Companion.kilograms
import cloud.mallne.units.Power.Companion.kilowatts
import cloud.mallne.units.Probability.Companion.percent
import cloud.mallne.units.Temperature.Companion.celsius
import cloud.mallne.units.Time.Companion.hours
import cloud.mallne.units.Velocity
import cloud.mallne.units.times
import cloud.mallne.units.div

object WeatherRowConstants {
    val temp get() = celsius
    val rh get() = percent
    val ws: Velocity get() = kilometers / hours
    val prec get() = millimeters
    val solrad get() = kilowatts / squareMeters
    val percentCured get() = percent
    val mcffmc get() = percent
    val mcffmcMatted get() = mcffmc
    val mcffmcStanding get() = mcffmc
    val precCumulative get() = prec
    val grassFuelLoad get() = kilograms / squareMeters
}