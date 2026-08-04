# Commands and Environment -- AreaAssist Geokit

## Scripts

```bash
# Build all modules
./gradlew build

# Run JVM tests
./gradlew test

# Run all target tests
./gradlew allTests

# Generate ANTLR parser for WKT-CRS
./gradlew :areaassist:geokit:coordinates:generateKotlinGrammarSource

# Publish to local Maven
./gradlew publishToMavenLocal
```

## Local Dev Setup

1. Ensure JDK 17+ is installed
2. Run `./gradlew build` from the `areaassist/geokit/` directory
3. After modifying `.g4` files in `coordinates/antlr/`, run the ANTLR generation command before building

## Environment Variables

No environment variables required for development.

## Runtime Notes

- ANTLR source generation must happen before compilation when WKT-CRS grammar files change.
- The `ogc` module contains `.http` files for integration testing against real WFS servers (IntelliJ HTTP Client).
