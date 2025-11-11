# Geokit

> A TurfJs, GML, GeoJson and WKT-CRS utility for Kotlin (Multiplatform)

Geokit is a comprehensive geospatial toolkit for Kotlin Multiplatform projects, providing implementations for various
geographic data formats and calculations.

## Features

### `calculation`

> A Turf-like Vector based Coordinate Math module

- Geometric calculations
- Distance measurements
- Area calculations
- Vector operations

### `geojson`

> A GeoJson implementation based on kotlinx.serialization

_The GeoJson Package is a repackaged version of `org.maplibre.spatialk:geojson` use this instead, if you want only a
serializable GeoJson._

### `gml`

> A GML implementation for WFS services

- Based on kotlinx.serialization and io.github.pdvrieze.xmlutil:serialization
- WFS service support
- GML parsing and generation
- Geographic markup handling

### `coordinates`

> A WKT-CRS parser made with ANTLR

- Coordinate Reference System (CRS) conversions
- EPSG dataset support
- WKT-CRS file parsing
- Coordinate transformation pipeline
