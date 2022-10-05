
plugins {
    id("jb-compose-setup")
    id("io.realm.kotlin")
    id("com.google.devtools.ksp")
    //id("org.jetbrains.kotlin.plugin.serialization")
    idea
}

idea {
    module {
        // Not using += due to https://github.com/gradle/gradle/issues/8749
        sourceDirs =
            sourceDirs + file("build/generated/ksp/metadata/commonMain/kotlin") // or tasks["kspKotlin"].destination
        generatedSourceDirs =
            generatedSourceDirs + file("build/generated/ksp/metadata/commonMain/kotlin")
    }
}

kotlin {
    sourceSets {
        @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
        named("commonMain") {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
            dependencies {
                implementation(project(":kmm-annotation-module"))
                implementation(project(":kmm-metaannotation-module"))

                implementation(libs.essenty.lifecycle)
                implementation(libs.essenty.instanceKeeper)
                implementation(libs.essenty.parcelable)
                implementation(libs.decompose.core)
                implementation(libs.mviKotlin.mvikotlin)
                implementation(libs.mviKotlin.coroutines)
                implementation(libs.badoo.reaktive)
                implementation(libs.benasher44.uuid)
                implementation(libs.kotlin.coroutinesCore)
                implementation(libs.kotlin.coroutinesNativeMt)
                implementation(libs.kotlin.datetime)
                implementation(libs.kotlinx.serializationCore)
                implementation(libs.kodein.common)
                implementation(libs.lighthouseGames.logging)
                implementation(libs.realm.libraryBase)
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

dependencies {
    add("kspCommonMainMetadata", project(":kmm-metaannotation-module"))
    add("kspCommonMainMetadata", project(":kmm-annotation-module"))
    add("kspCommonMainMetadata", project(":jvm-ksp-processor-module"))
}
// Try making sure KSP-generated attaches to all target via commonMain
tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().all {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}
tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompile>().all {
    if (name != "kspCommonMainKotlinMetadata" && !name.toLowerCase().contains("android")) {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

configurations.all {
    attributes {
        attribute(Attribute.of("ui", String::class.java), "awt")
    }
}
android {
    namespace = "kmm.ksp.client"
}