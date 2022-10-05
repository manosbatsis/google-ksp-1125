import org.jetbrains.compose.compose
import build_logic.LibCatalogue
import org.gradle.jvm.toolchain.JavaLanguageVersion


plugins {
    id("java-toolchain-setup")
    kotlin("multiplatform")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.android.library")
    id("kotlin-parcelize")
}

val jdlVersion = LibCatalogue.`java`
val javaLanguageVersion = jdlVersion.split(".").let{
    JavaLanguageVersion.of(it.last())
}

kotlin {
    jvmToolchain {
        languageVersion.set(javaLanguageVersion)
    }

    jvm("desktop"){
        compilations.all { kotlinOptions { jvmTarget = jdlVersion } }
    }
    android(){
        //compilations.all { kotlinOptions { jvmTarget = jdlVersion } }
    }
    // ios()
/*
    js(IR) {
        browser()
    }
*/
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(LibCatalogue.`kotlin-coroutinesCore`)
                implementation(LibCatalogue.`kotlinx-serializationCore`)
                implementation(LibCatalogue.`kodein-common`)
                implementation(LibCatalogue.`lighthouseGames-logging`)
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

    tasks.register("allSourceSets") {
        doLast{
            project.sourceSets.forEach {  srcSet ->
                println("[${srcSet.name}]")
                print( "    --> Source directories: "+srcSet.allJava.srcDirs+"\n")
                print( "    --> Output directories: "+srcSet.output.classesDirs.files+"\n")
                print( "    --> Compile classpath:\n")
                srcSet.compileClasspath.files.forEach {
                    print( "        "+it.path+"\n")
                }
                println("")
            }
        }
    }
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

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(jdlVersion)
        targetCompatibility = JavaVersion.toVersion(jdlVersion)
    }

    composeOptions {
        kotlinCompilerExtensionVersion = LibCatalogue.composeCompiler
    }

//    kotlinOptions {
//        jvmTarget = jdlVersion
//        freeCompilerArgs = (LibCatalogue.freeCompilerArgs as List<String>) + "-opt-in=kotlin.RequiresOptIn"
//    }

}




afterEvaluate {
    val jdlVersion = jdlVersion
    tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = jdlVersion
        targetCompatibility = jdlVersion
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            freeCompilerArgs = LibCatalogue!!.freeCompilerArgs as List<String>
            jvmTarget = jdlVersion
            apiVersion = "1.7"
            languageVersion = "1.7"
            jvmTarget = jdlVersion
        }
    }

}

// TODO: Workaround until https://issuetracker.google.com/issues/223240936 is fixed
androidComponents {
    onVariants(selector().all()) {
        val capitalizedVariantName = it.name.substring(0, 1).toUpperCase() + it.name.substring(1)
        afterEvaluate {
            val processGoogleServicesName = "process${capitalizedVariantName}GoogleServices"
            val mapSourceSetPathsName = "map${capitalizedVariantName}SourceSetPaths"
            tasks.findByName(processGoogleServicesName)?.let { processGoogleServices ->
                tasks.named(mapSourceSetPathsName).configure {
                    dependsOn(processGoogleServices)
                }
            }
        }
    }
}