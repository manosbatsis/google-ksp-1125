import org.gradle.jvm.toolchain.JavaToolchainService
import org.jetbrains.kotlin.gradle.tasks.UsesKotlinJavaToolchain
import build_logic.LibCatalogue



afterEvaluate {

    val jdlVersion = LibCatalogue.`java`
    val javaLanguageVersion = jdlVersion.split(".").let{
        JavaLanguageVersion.of(it.last())
    }
    val service = project.extensions.getByType<JavaToolchainService>()

    // The Java Toolchains that will compile/launch *main* code
    val customCompiler = service.compilerFor {
        languageVersion.set(javaLanguageVersion)
    }
    val customLauncher = service.launcherFor {
        languageVersion.set(javaLanguageVersion)
    }

    project.tasks.withType<UsesKotlinJavaToolchain>().configureEach {
        kotlinJavaToolchain.toolchain.use(customLauncher)
    }
}