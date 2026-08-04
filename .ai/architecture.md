# Architecture -- AreaAssist Geokit

## Purpose

Geokit is a comprehensive geospatial toolkit for Kotlin Multiplatform, providing utilities for coordinate calculations, GeoJSON, GML, WFS, and WKT-CRS parsing and transformations.

## Tech Stack

- **Language:** Kotlin Multiplatform (KMP)
- **Serialization:** kotlinx.serialization (JSON and XML)
- **XML:** xmlutil (`io.github.pdvrieze.xmlutil`)
- **Parser:** ANTLR (antlr-kotlin-runtime) for WKT-CRS
- **Mapping:** maplibre-spatialk (GeoJSON models)

## Project Structure

```
areaassist/geokit/
+-- calculation/     # Core vector-based geometry and math operations (Turf-like)
+-- coordinates/     # WKT-CRS parsing (ANTLR), CRS registry, coordinate transformations
+-- geojson/         # GeoJSON models and serialization, maplibre-spatialk interop
+-- ogc/             # GML and WFS models and serialization (xmlutil)
+-- interop/         # Bridging layer between GeoJSON and OGC/GML types
+-- fwi/             # Forest Weather Index calculations
```

## Submodules

| Module | Purpose |
|--------|---------|
| `calculation` | Vector geometry, area/distance calculations, Turf-like operations |
| `coordinates` | WKT-CRS parsing via ANTLR, `CrsRegistry`, coordinate transformation `Pipeline` |
| `geojson` | GeoJSON data models with `kotlinx.serialization`, spatialk interop |
| `ogc` | GML/XML and WFS models using `xmlutil` |
| `interop` | Type conversion between GeoJSON and OGC/GML representations |
| `fwi` | Forest Weather Index (FWI) calculations |

## Dependencies on Other Modules

- **Units** (`cloud.mallne:units`) -- Physical unit types for measurements

## Non-negotiable Rules

- All data models must use `@Serializable` from `kotlinx.serialization`
- Logic must stay in `commonMain` unless platform-specific APIs are required
- Use `CrsRegistry` and `Pipeline` for all CRS operations (never raw coordinate math)
