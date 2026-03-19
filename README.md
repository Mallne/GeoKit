# 🌍 Geokit

[![Kotlin](https://img.shields.io/badge/kotlin-grey.svg?logo=kotlin)](https://kotlinlang.org/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin-Multiplatform-blue.svg?logo=kotlin)](https://kotlinlang.org/docs/multiplatform.html)

**Geokit** is a powerful, type-safe geospatial toolkit for **Kotlin Multiplatform**. It brings a "Turf-like" experience
to the Kotlin ecosystem, providing comprehensive utilities for coordinate math, GeoJSON handling, GML/WFS parsing, and
WKT-CRS transformations.

---

## 🚀 Key Features

* **📐 Turf-like Math:** Geometric calculations, distance measurements, and vector operations.
* **🌐 Multi-module Design:** Only use what you need (`calculation`, `geojson`, `ogc`, `coordinates`).
* **🛠️ CRS Transformations:** Parse WKT-CRS and perform coordinate conversions (e.g., EPSG:4326 to EPSG:25832).
* **📦 GeoJSON & GML:** First-class support for industry-standard formats via `kotlinx.serialization`.
* **🏗️ Kotlin Multiplatform:** Targets JVM, Android, JS, WasmJS, iOS, and Linux.

---

## 📦 Modules

### 1. `calculation`

Core vector-based coordinate math. Uses a type-safe `units` library for measurements.

```kotlin
val point1 = Vertex(50.96, 10.99)
val point2 = Vertex(51.00, 11.05)

// Type-safe distance and bearing
val dist: Measure<Length> = point1 distanceTo point2
val bearing: Measure<Angle> = point1 angleTo point2

// Point-in-polygon check
val isInside = vertex inside shape
```

### 2. `coordinates`

WKT-CRS parser and transformation pipeline. Supports complex coordinate operations.

```kotlin
val registry = CrsRegistry()
registry.ingest(BuiltInCoordinateReferenceSystems.EPSG4326)
registry.ingest(BuiltInCoordinateReferenceSystems.EPSG25832)

val pipeline = registry.compose(
    source = LocalCoordinate(50.963841, 10.998273),
    fromEPSG = "EPSG:4326",
    toEPSG = "EPSG:25832"
)

val result = pipeline.execute() // Northing/Easting
```

### 3. `geojson`

GeoJSON implementation based on `kotlinx.serialization`. Provides interop
with [MapLibre SpatialK](https://github.com/maplibre/maplibre-spatialk-geojson).

```kotlin
val position = Position(longitude = 10.99, latitude = 50.96)
val vertex = position.toVertex() // Convert to Geokit Vertex for math
```

### 4. `ogc`

GML and WFS (Web Feature Service) implementation. Handles XML parsing and generation for OGC services.

### 5. `interop`

The "glue" module that maps between GeoJSON models and OGC/GML types.

---

## 🛠 Installation

Add the following to your `build.gradle.kts`:

```kotlin
repositories {
    mavenCentral()
    // Add your registry if needed
}

dependencies {
    implementation("cloud.mallne.geokit:calculation:1.0.0-SNAPSHOT")
    implementation("cloud.mallne.geokit:geojson:1.0.0-SNAPSHOT")
    // ... other modules
}
```

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request or open an issue.

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

