import org.jetbrains.compose.compose
import build_logic.LibCatalogue


plugins {
    id("kotlin-multiplatform-setup")
    id("org.jetbrains.compose")
    //id("android-lib-setup")
}

kotlin {
    @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
    sourceSets {
        named("commonMain") {
            dependencies {

                implementation(LibCatalogue.`jetbrains-compose-animation`)
                implementation(LibCatalogue.`jetbrains-compose-animationGraphics`)
                implementation(LibCatalogue.`jetbrains-compose-foundation`)
                implementation(LibCatalogue.`jetbrains-compose-material`)
                implementation(LibCatalogue.`jetbrains-compose-material3`)
                implementation(LibCatalogue.`jetbrains-compose-material-iconsextended`)
                implementation(LibCatalogue.`jetbrains-compose-ui`)
                //implementation(LibCatalogue.`jetbrains-compose-uiTooling`)
                //implementation(LibCatalogue.`jetbrains-compose-preview`)
                //implementation(LibCatalogue.`jetbrains-compose-runtime`)

            }
        }
        named("commonTest") {
            dependencies {
                implementation(LibCatalogue.`kotlin-testCommon`)
                implementation(LibCatalogue.`kotlin-testAnnotationsCommon`)
            }
        }

        named("androidMain") {
            dependencies {
                implementation(LibCatalogue.`androidx-appcompat`)
                implementation(LibCatalogue.`androidx-coreKtx`)
                implementation(LibCatalogue.`kodein-compose`)
            }
        }
        named("androidTest") {
            dependencies {
                implementation(LibCatalogue.`kotlin-testJunit`)
            }
        }
        named("desktopMain") {
            dependencies {
                implementation(compose.desktop.common)
                implementation(compose.desktop.currentOs)
                implementation(LibCatalogue.`kodein-compose`)
            }
        }
        named("desktopTest") {
            dependencies {
                //implementation(LibCatalogue.`kotlin-testJunit5`)
            }
        }
        /*named("jsTest") {
            dependencies {
                implementation(Deps.JetBrains.Kotlin.testJs)
            }
        }*/
    }
}
