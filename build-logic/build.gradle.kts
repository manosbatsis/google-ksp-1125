import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.asClassName
import org.jetbrains.kotlin.gradle.utils.toSetOrEmpty
import org.jetbrains.kotlin.util.prefixIfNot

plugins {
    `kotlin-dsl`
    `version-catalog`
    id("com.github.gmazzo.buildconfig") version "3.1.0"
}

dependencies {

    implementation(libs.jetbrains.compose.gradleplugin)
    implementation(libs.kotlinSerializationPlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.android.gradlePlugin)
    implementation(libs.mokoResourcesGradlePlugin)
    implementation(libs.sqldelight.gradlePlugin)
    //implementation(plugin(libs.spotless.gradlePlugin))
}


kotlin {
    // Make BuildConfig, and BuildSrcConfig available in main project
    sourceSets.getByName("main").kotlin.srcDirs.addAll(
        listOf(
            "build/generated/ksp/metadata/commonMain/kotlin",
            "build/generated/source/buildConfig/main/main"
        ).map { fn ->
            project.file(fn)
        }
    )
}


afterEvaluate {
    tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>> {
        dependsOn("generateBuildConfig")
    }
}

val libsCatalogue = project.extensions.getByType<VersionCatalogsExtension>().named("libs")
buildConfig {

    sourceSets.getByName("main") {

        buildConfig {
            className("LibCatalogue")
            useKotlinOutput()

            libsCatalogue.versionAliases.forEach { alias ->
                buildConfigField(
                    "String",
                    alias.toKotlinName(),
                    "\"${libsCatalogue.resolveVersion(alias)}\""
                )

            }
            libsCatalogue.libraryAliases.forEach { alias ->
                buildConfigField(
                    "String",
                    alias.toKotlinName(),
                    "\"${libsCatalogue.findCoordinates(alias)}\""
                )
            }

            buildConfigField(Any::class.qualifiedName!!, "freeCompilerArgs", listOf(
                "-Xjvm-default=all",
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=kotlin.Experimental",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.InternalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview",
            ).joinToString(", ") { "\"$it\"" }.let { "listOf($it)" })

            buildConfigField(Any::class.qualifiedName!!, "freeCoroutineCompilerArgs", listOf(
                "-Xjvm-default=all",
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=kotlin.Experimental",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.InternalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview"
            ).joinToString(", ") { "\"$it\"" }.let { "listOf($it)" })

        }
    }
}

fun VersionCatalog.findCoordinates(alias: String) =
    toStringCoordinates(findLibrary(alias).get())

fun VersionCatalog.toStringCoordinates(dep: Provider<MinimalExternalModuleDependency>) =
    dep.orNull?.let{
        "${it.module.group}:${it.module.name}${it.resolveVersion(":")}"
    }

fun String.toKotlinName(): String {
    return this.replace(".", "-")
        .replace(" ", "-")

}

fun VersionCatalog.resolveVersion(alias: String) =
    findVersion(alias).get().resolveVersion()

fun MinimalExternalModuleDependency.resolveVersion(prefix: String = ""): String? =
    versionConstraint.resolveVersion(version)
    ?.prefixIfNot(prefix)

fun VersionConstraint.resolveVersion(default: Any? = null): String? = listOfNotNull(
    requiredVersion,
    strictVersion,
    preferredVersion,
    default)
    .map { it.toString() }
    .find { it.isNotBlank() }

