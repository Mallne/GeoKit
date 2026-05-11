# 🔥 Geokit - Fire Weather Index (FWI)

A strict Kotlin Multiplatform port of the **NRCan/CFS Python reference (NG_FWI.py)**. This module provides hourly calculations for the Canadian Forest Fire Weather Index (FWI) System.

## 🚀 Features

- **Strict Parity:** Verified against official PRF2007 test datasets with >99% matching accuracy.
- **Hourly Calculations:** Supports high-resolution temporal data.
- **Full Index Suite:** Calculates FFMC, DMC, DC, ISI, BUI, FWI, and DSR.
- **Grassland Extension:** Includes calculations for GFMC, GSI, and GFWI (Matted and Standing).
- **Automated Solar Math:** Built-in astronomical calculations for sunrise, sunset, and solar radiation.
- **State Propagation:** Correctly handles moisture state transitions across hourly weather streams.

## 📦 Installation

Add the dependency to your `build.gradle.kts`:

```kotlin
dependencies {
    implementation("cloud.mallne.geokit:fwi:1.0.0-SNAPSHOT")
}
```

## 🛠 Usage

### Basic Hourly Calculation

The simplest way to use the library is via the `FireWeatherIndex` orchestrator. It handles grouping by station/year, sorting by time, and solar radiation calculation automatically.

```kotlin
import cloud.mallne.geokit.fwi.calculator.FireWeatherIndex
import cloud.mallne.geokit.fwi.model.WeatherRow
import kotlinx.datetime.LocalDate

val weatherData = listOf(
    WeatherRow.Input(
        id = "PRF",
        date = LocalDate(2007, 5, 10),
        hr = 12,
        temp = 24.55,
        rh = 41.53,
        ws = 9.42,
        prec = 0.0,
        lat = 45.996,
        long = -77.427,
        timezone = -4.0
    ),
    // ... more hours
)

// Run the orchestrator
val results = FireWeatherIndex(weatherData)

results.forEach { row ->
    println("Time: ${row.date} ${row.hr}h | FWI: ${row.fwi} | FFMC: ${row.ffmc}")
}
```

### Advanced Configuration

You can provide startup values (default moisture codes) if you are resuming a calculation from a previous state:

```kotlin
val results = FireWeatherIndex(
    dfWx = weatherData,
    ffmcOld = 85.0,
    dmcOld = 6.0,
    dcOld = 15.0,
    precCumulative = 0.0, // Cumulative rain for canopy interception
    silent = false
)
```

## 📐 Scientific Reference

This implementation follows the mathematical logic described in:
- *Van Wagner, C.E. 1987. Development and structure of the Canadian Forest Fire Weather Index System.*
- *Wotton, B.M. 2009. Interpreting and using the Canadian Forest Fire Weather Index System.*
- The **NRCan/CFS NG_FWI.py** reference implementation.

## 🧪 Validation

The module is continuously validated against the `PRF2007` dataset. To run the validation tests locally:

```bash
./gradlew :geokit:fwi:jvmTest
```
