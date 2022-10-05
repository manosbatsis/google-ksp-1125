enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("VERSION_CATALOGS")

rootProject.name = "google-ksp-1125"

pluginManagement {
    includeBuild("build-logic")

    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        google()

        maven("https://oss.sonatype.org/content/repositories/releases/")
        maven("https://s01.oss.sonatype.org/content/repositories/releases/")

        //Early Access versions
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
        maven { url = uri("https://maven.hq.hydraulic.software") }
        maven("https://repo.repsy.io/mvn/chrynan/public")
    }
}

include(":jvm-ksp-processor-module")
include(":kmm-annotation-module")
include(":kmm-ksp-client-module")
include(":kmm-metaannotation-module")


