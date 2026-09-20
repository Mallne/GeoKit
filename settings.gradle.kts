rootProject.name = "geokit"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

include(":calculation")
include(":geojson")
include(":ogc")
include(":interop")
include(":coordinates")
include(":fwi")
