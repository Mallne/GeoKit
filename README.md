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

_The GeoJson Package provides interop for the Maplibre SpatialK GeoJson Models._

### `ogc`

> A GML implementation for WFS services

- Based on kotlinx.serialization and io.github.pdvrieze.xmlutil:serialization
- WFS service support
- GML parsing and generation
- Geographic markup handling
- This does not contain full Support for FES, GML and WFS yet
- basically a Kotlin version of `GeoTools OGC`

### `interop`

> An interop layer between GeoJson and the OGC (mainly GML) Types
> _These types do not map 1:1, so there are some implications converting between those_

### `coordinates`

> A WKT-CRS parser made with ANTLR

- Coordinate Reference System (CRS) conversions
- EPSG dataset support
- WKT-CRS file parsing
- Coordinate transformation pipeline
