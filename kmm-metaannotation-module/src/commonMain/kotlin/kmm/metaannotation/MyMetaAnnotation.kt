@file:Suppress("unused")

package kmm.metaannotation

import androidx.annotation.Keep

@Target(allowedTargets = [AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS])
@MustBeDocumented
@Keep
annotation class MyMetaAnnotation