import build_logic.LibCatalogue

buildscript {

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

plugins {
    id("com.gradle.build-scan") version "3.11.1" apply false
    id("com.android.library") version libs.versions.androidGradlePlugin.get() apply false
    id("com.google.gms.google-services") version libs.versions.googleGmsServices.get() apply false
    id("com.google.devtools.ksp") version libs.versions.googleKsp.get() apply false
    id("dev.icerock.mobile.multiplatform-resources") version libs.versions.mokoResourcesVersion.get() apply false
    kotlin("jvm") version libs.versions.kotlin.get() apply false
    kotlin("multiplatform") version libs.versions.kotlin.get() apply false
    id("org.jetbrains.kotlin.plugin.serialization") version libs.versions.kotlin.get()  apply false
    id("org.jetbrains.compose") version libs.versions.jetbrainsCompose.get() apply false
    id("io.realm.kotlin") version libs.versions.realm.get() apply false
    id("com.github.gmazzo.buildconfig") version libs.versions.gmazzoBuildconfig.get() apply false
    id("com.squareup.sqldelight") version libs.versions.sqlDelight.get() apply false
    id("io.kotest.multiplatform") version libs.versions.kotest.get() apply false
    id("dev.hydraulic.conveyor") version libs.versions.hydraulicConveyor.get() apply false
    id("catalogue-setup") apply false
}

allprojects {
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

    configurations.all {

        // Fixes "Could not resolve org.jetbrains.skiko:skiko"
        attributes {
            attribute(Attribute.of("ui", String::class.java), "awt")
        }
        @Suppress("UnstableApiUsage")
        resolutionStrategy.eachDependency {
            if (requested.group == "org.jetbrains.kotlin"
                || requested.group.startsWith("org.jetbrains.kotlin.")
            ) useVersion(libs.versions.kotlin.get())
            if (requested.group == "org.jetbrains.kotlinx"
                && requested.name.startsWith("kotlinx-coroutines")
            ) useVersion(libs.versions.kotlinCoroutines.get())
            if (requested.group == "androidx.test.espresso") {
                useVersion(libs.versions.androidxEspresso.get())
            }
            if (requested.group ==  "org.jetbrains.skiko"
                && requested.name == "skiko"){
                useVersion(libs.versions.skiko.get())
            }

        }
        resolutionStrategy.force("org.objenesis:objenesis:3.1")
    }

    tasks.register<DependencyReportTask>("allDeps") {
        //setConfiguration("compileClasspath")
    }

    afterEvaluate {

        var libCatalogue: LibCatalogue? by project.extra
        if(libCatalogue == null) libCatalogue = LibCatalogue

        val javaVersion = libs.versions.`java`.get()
        tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>> {
            kotlinOptions{
                freeCompilerArgs = (this.freeCompilerArgs + libCatalogue!!.freeCompilerArgs as List<String>).distinct()
                when(this){
                    is org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions -> jvmTarget = javaVersion
                }
            }
        }
        tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompile> {
            kotlinOptions{
                freeCompilerArgs = (this.freeCompilerArgs + libCatalogue!!.freeCompilerArgs as List<String>).distinct()
                when(this){
                    is org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions -> jvmTarget = javaVersion
                }
            }
        }

        project.extensions.findByType<org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension>()
            ?.let { kmpExt ->
                kmpExt.sourceSets.run {
                    all {
                        languageSettings.optIn("kotlin.RequiresOptIn")
                        languageSettings.optIn("kotlin.Experimental")
                        languageSettings.optIn("kotlinx.serialization.ExperimentalSerializationApi")
                    }
                    removeAll { sourceSet ->
                        setOf(
                            "androidAndroidTestRelease",
                            "androidTestFixtures",
                            "androidTestFixturesDebug",
                            "androidTestFixturesRelease",
                        ).contains(sourceSet.name)
                    }
                }
            }
    }
}
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}