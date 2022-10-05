package jvm.ksp.processor

import kmm.annotation.MyAnnotation
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import kmm.metaannotation.MyMetaAnnotation

class KspAnnotationProcessor(
    private val environment: SymbolProcessorEnvironment
) : SymbolProcessor {

    @OptIn(KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {

        environment.logger.warn("KspAnnotationProcessor called")
        val annotatedWithImmutableFormStateStoreImpl: List<KSAnnotated> =
            resolver.getSymbolsWithAnnotation(MyAnnotation::class.qualifiedName!!, true)
                .toList()
        environment.logger.warn("KspAnnotationProcessor found symbols annotated with @MyAnnotation: ${annotatedWithImmutableFormStateStoreImpl.isNotEmpty()}")
        val annotated: Sequence<KSDeclaration> =
            resolver.getNewFiles()
                .flatMap { it.declarations.filterIsInstance<KSClassDeclaration>() }
                .filter { declaration ->
                    declaration.isAnnotationPresent(MyAnnotation::class)

                }
        environment.logger.warn("KspAnnotationProcessor found new files annotated with @MyAnnotation: ${annotated.iterator().hasNext()}")
        val metaAnnotated: Sequence<KSDeclaration> =
            resolver.getNewFiles()
                .flatMap { it.declarations.filterIsInstance<KSClassDeclaration>() }
                .filter { declaration ->
                    declaration.annotations.any { it.annotationType.isAnnotationPresent(
                        MyMetaAnnotation::class
                    ) }
                }
        environment.logger.warn("KspAnnotationProcessor found meta-annotated with @MyMetaAnnotation: ${metaAnnotated.iterator().hasNext()}")


        return listOf()
    }
}