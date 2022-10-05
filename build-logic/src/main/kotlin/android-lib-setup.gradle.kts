import org.jetbrains.compose.compose
import build_logic.LibCatalogue


plugins {
    id("java-toolchain-setup")
    id("kotlin-multiplatform")
    id("com.android.library")
    id("kotlin-parcelize")
    id("org.jetbrains.kotlin.plugin.serialization")

}


@Suppress("UnstableApiUsage")
android {

    sourceSets {
        named("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            res.srcDirs(
                File(buildDir, "src/androidMain/res"),
                File(buildDir, "generated/moko/androidMain/res")
            )
            kotlin.srcDir(File(buildDir, "generated/ksp/metadata/commonMain/kotlin"))
        }
    }

    compileSdk = LibCatalogue.androidCompileSdk.toInt()

    defaultConfig {
        minSdk = LibCatalogue.androidMinSdk.toInt()
        targetSdk = LibCatalogue.androidTargetSdk.toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        //versionCode = 21//versioning.getVersionCode()
       //versionName = "${project.version}"
    }

    testFixtures {
        enable = false
        // enable testFixtures's android resources (disabled by default)
        androidResources = false
    }

    buildFeatures {
        compose = true
    }

    buildTypes {
        release {
            isShrinkResources = false
            isMinifyEnabled = false
        }
        debug {
            isShrinkResources = false
            isMinifyEnabled = false
        }
    }

    @Suppress("UnstableApiUsage")
    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(LibCatalogue.`java`)
        targetCompatibility = JavaVersion.toVersion(LibCatalogue.`java`)
    }

    composeOptions {
        kotlinCompilerExtensionVersion = LibCatalogue.composeCompiler
    }

//    kotlinOptions {
//        jvmTarget = LibCatalogue.`java`
//        freeCompilerArgs = (LibCatalogue.freeCompilerArgs as List<String>) + "-opt-in=kotlin.RequiresOptIn"
//    }

    @Suppress("UnstableApiUsage")
    sourceSets {
        named("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            res.srcDirs("src/androidMain/res")
        }
    }
}
