@file:Keep
package kmm.annotation

import androidx.annotation.Keep
import kmm.metaannotation.MyMetaAnnotation
import kotlin.reflect.KClass

@MyMetaAnnotation
@Target(allowedTargets = [AnnotationTarget.CLASS])
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class MyAnnotation(
    val sourceTarget: KClass<*> = Nothing::class,
    val sourceTargetQualifiedName: String = "",
    val interfaces: Array<KClass<*>> = [],
)
