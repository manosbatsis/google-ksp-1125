plugins {
    kotlin("jvm")
    `java-library`
    //id("com.google.devtools.ksp")
}

dependencies {
    api(project(":kmm-annotation-module"))
    api(project(":kmm-metaannotation-module"))
    implementation(libs.google.ksp)
    implementation(libs.decompose.core)
    implementation(libs.essenty.instanceKeeper)
    implementation(libs.essenty.parcelable)
    implementation(libs.mviKotlin.mvikotlin)
    implementation(libs.mviKotlin.coroutines)
    implementation(libs.badoo.reaktive)
    implementation(libs.kotlin.coroutinesCore)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.datetime)
    implementation(libs.kodein.common)
    implementation(libs.lighthouseGames.logging)
    implementation(libs.kotlin.poet)
    implementation(libs.kotlin.poetKsp)
    implementation(libs.realm.libraryBase)
}

