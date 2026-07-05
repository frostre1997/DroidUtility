# DroidUtility ProGuard Rules
# Keep Shizuku classes
-keep class rikka.shizuku.** { *; }
-keep class dev.rikka.rikkax.shizuku.** { *; }

# Keep serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }
-keepclasseswithmembers class kotlinx.serialization.json.** { kotlinx.serialization.KSerializer serializer(...); }
-keep,includedescriptorclasses class com.frostre1997.droidutility.**$$serializer { *; }
-keepclassmembers class com.frostre1997.droidutility.** { *** Companion; }
-keepclasseswithmembers class com.frostre1997.droidutility.** { kotlinx.serialization.KSerializer serializer(...); }
