# Geokit

**Stack**: KMP library. Geospatial toolkit (GeoJSON, GML, WFS, WKT-CRS).

> **Full docs**: [areaassist/geokit/.ai/](.ai/)

## Critical Rules

1. All data models must use `@Serializable` from kotlinx.serialization
2. Keep logic in `commonMain` unless platform-specific APIs are required
3. Use `CrsRegistry` and `Pipeline` for all CRS operations
4. ANTLR grammar changes require regeneration before compilation

## Build

```bash
./gradlew build
./gradlew :areaassist:geokit:coordinates:generateKotlinGrammarSource  # after grammar changes
./gradlew allTests
```
