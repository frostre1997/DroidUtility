# ===== Shizuku (prevents ClassNotFoundException for ShizukuProvider) =====
# Keep all Shizuku classes (both old and new package names)
-keep class rikka.shizuku.** { *; }
-keep class dev.rikka.shizuku.** { *; }

# Explicitly keep the provider class (in case the above isn't enough)
-keep class rikka.shizuku.ShizukuProvider { *; }

# Keep the provider constructor (required for instantiation)
-keepclassmembers class rikka.shizuku.ShizukuProvider {
    public <init>();
}

# ===== Android general =====
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class * extends android.os.Binder

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep custom view constructors
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

# ===== Kotlin =====
# Keep Kotlin metadata
-keep class kotlin.Metadata { *; }

# Keep Kotlin reflection and internal classes
-keep class kotlin.** { *; }
-keepclassmembers class kotlin.** { *; }

# Keep serialization (if using kotlinx.serialization)
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.**
-keep class kotlinx.serialization.** { *; }
-keepclassmembers class kotlinx.serialization.** {
    *** Companion;
}

# Keep data classes (common pattern)
-keepclassmembers class * {
    *** copy(...);
}

# ===== Compose / Jetpack =====
# Keep Compose UI classes
-keep class androidx.compose.** { *; }
-keepclassmembers class androidx.compose.** { *; }

# Keep Compose runtime
-keep class androidx.compose.runtime.** { *; }

# Keep Compose material
-keep class androidx.compose.material.** { *; }

# Keep Compose navigation
-keep class androidx.navigation.** { *; }

# ===== Serialization (if using JSON) =====
# Keep serializable classes (annotated with @Serializable)
-keepclassmembers class * {
    @kotlinx.serialization.Serializable <fields>;
}

# ===== Coroutines =====
-keep class kotlinx.coroutines.** { *; }
-keepclassmembers class kotlinx.coroutines.** { *; }

# ===== Keep annotations =====
-keepattributes *Annotation*

# ===== Keep exceptions (optional) =====
-keep class * extends java.lang.Throwable

# ===== Debug helpers (optional) =====
# If you use Timber or other logging, keep them (only in debug builds)
# -keep class timber.log.** { *; }
# -dontwarn timber.log.**

# ===== Prevent removal of unused resources (if you want to keep all) =====
# -keepresources
