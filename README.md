# google-ksp-1125

reproducer for https://github.com/google/ksp/issues/1125

## Setup

Define a valid SDK location with an ANDROID_HOME environment variable 
or by setting the sdk.dir path in a local.properties 

## Reproduce

Simply run 

    ./gradlew clean build

and you should see something like:

```
> Task :kmm-ksp-client-module:kspCommonMainKotlinMetadata
w: [ksp] KspProcessorProvider called
w: [ksp] KspAnnotationProcessor called
w: [ksp] KspAnnotationProcessor found symbols annotated with @MyAnnotation: false
w: [ksp] KspAnnotationProcessor found new files annotated with @MyAnnotation: false
w: [ksp] KspAnnotationProcessor found meta-annotated with @MyMetaAnnotation: false

```

The last three lines above end with `false` when they should be  `true`