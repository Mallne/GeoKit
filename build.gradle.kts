import nl.littlerobots.vcu.plugin.resolver.VersionSelectors

plugins {
    alias(libs.plugins.kmp) apply false
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.version.catalog.update)
    alias(libs.plugins.ben.manes.versions)
}

versionCatalogUpdate {
    versionSelector(VersionSelectors.STABLE)
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://registry.mallne.cloud/repository/DiCentraArtefacts/")
            credentials {
                username = providers.environmentVariable("NEXUS_USERNAME").getOrElse("")
                password = providers.environmentVariable("NEXUS_PASSWORD").getOrElse("")
            }
        }
    }
}
