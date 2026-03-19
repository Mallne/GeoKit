# Geokit - Kotlin Multiplatform Geospatial Toolkit

Geokit is a comprehensive geospatial library for Kotlin Multiplatform, providing utilities for coordinate calculations, GeoJSON, GML, WFS, and WKT-CRS parsing and transformations.

## Project Overview

Geokit is organized as a multi-module Kotlin project targeting JVM, Android, JS, WasmJS, iOS, and Linux. It focuses on providing a shared codebase for geographic data handling across different platforms.

### Modules

*   **`calculation`**: Core vector-based geometry and math operations (Turf-like).
*   **`coordinates`**: WKT-CRS parsing (using ANTLR), CRS registry, and coordinate transformation pipelines.
*   **`geojson`**: GeoJSON models and serialization using `kotlinx.serialization`, with interop for `maplibre-spatialk`.
*   **`ogc`**: GML and WFS (Web Feature Service) support, including models and serialization using `xmlutil`.
*   **`interop`**: Bridging layer between GeoJSON and OGC/GML types.

## Building and Running

The project uses Gradle (Kotlin DSL) for build management.

*   **Build all modules:**
    ```bash
    ./gradlew build
    ```
*   **Run JVM tests:**
    ```bash
    ./gradlew test
    ```
*   **Run all target tests:**
    ```bash
    ./gradlew allTests
    ```
*   **Generate ANTLR parser for WKT-CRS:**
    ```bash
    ./gradlew :coordinates:generateKotlinGrammarSource
    ```
*   **Publish to local Maven repository:**
    ```bash
    ./gradlew publishToMavenLocal
    ```

## Development Conventions

*   **Kotlin Multiplatform (KMP):** Logic should be placed in `commonMain` and tests in `commonTest` unless platform-specific implementations are required (`jvmMain`, `androidMain`, etc.).
*   **Serialization:** All data models should use `@Serializable` from `kotlinx.serialization`.
*   **XML/GML:** Use the `ogc` module for GML/XML handling, which relies on `io.github.pdvrieze.xmlutil`.
*   **CRS Handling:** When working with coordinate reference systems, use the `CrsRegistry` and `Pipeline` abstractions in the `coordinates` module.
*   **Code Style:** Follow standard Kotlin coding conventions and idiomatic KMP patterns.

## Testing

*   **Unit Tests:** Located in `src/commonTest/kotlin` within each module.
*   **Integration Tests:** The `ogc` module contains `.http` files (e.g., `WFS Server.http`) for testing against real WFS servers using the IntelliJ HTTP Client.

## Dependencies

Key libraries used in this project:
*   `kotlinx.serialization` (JSON & XML)
*   `xmlutil` (XML serialization)
*   `antlr-kotlin-runtime` (Parser generator)
*   `maplibre-spatialk` (GeoJSON models)
*   `cloud.mallne:units` (Unit management)
