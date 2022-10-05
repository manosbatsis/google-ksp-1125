plugins {
    id("kotlin-multiplatform-setup")
}
android {
    namespace = "kmm.metaannotation"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.androidxAnnotations)
            }
        }
        named("commonTest") {
            dependencies {
                implementation(libs.kotlin.testCommon)
                implementation(libs.kotlin.testAnnotationsCommon)
            }
        }
    }
}
