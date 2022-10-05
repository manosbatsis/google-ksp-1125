import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.provideDelegate
import build_logic.LibCatalogue

var libCatalogue: LibCatalogue? by project.extra
if(libCatalogue == null) libCatalogue = LibCatalogue