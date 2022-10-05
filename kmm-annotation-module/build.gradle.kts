plugins {
    id("kotlin-multiplatform-setup")
}
android {
    namespace = "kmm.annotation"
}

kotlin {

    sourceSets {
        commonMain {
            dependencies {
                api(project(":kmm-metaannotation-module"))
                implementation(libs.androidxAnnotations)
                implementation(libs.decompose.core)
                implementation(libs.mviKotlin.mvikotlin)
                implementation(libs.lighthouseGames.logging)
                implementation(libs.kotlin.coroutinesCore)
                implementation(libs.kotlinx.serializationCore)
                implementation(libs.kotlin.datetime)
                implementation(libs.kodein.common)
                implementation(libs.moko.resources)
                implementation(libs.realm.libraryBase)
                implementation(libs.badoo.reaktive)
            }
        }
        named("commonTest") {
            dependencies {
                implementation(libs.kotlin.testCommon)
                implementation(libs.kotlin.testAnnotationsCommon)
            }
        }
        named("androidMain") {
            dependencies {
                implementation(libs.moko.resourcesCompose)
            }
        }
    }
}
