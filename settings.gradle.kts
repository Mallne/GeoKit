rootProject.name = "geokit"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

val unitsDir = file("../units")
if (unitsDir.exists()) {
    includeBuild(unitsDir.absolutePath) {
        dependencySubstitution {
            substitute(module("cloud.mallne:units")).using(project(":"))
        }
    }
} else {
    println("[GEOKIT:units] This Project seems to be running without the Monorepo Context, please consider using the Monorepo")
}

include(":calculation")
include(":geojson")
include(":ogc")
include(":interop")
include(":coordinates")