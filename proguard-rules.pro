# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# all companion object
-printconfiguration full-r8-config.txt

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes *Annotation*                      # Keep annotations
-keepattributes SourceFile,LineNumberTable        # Keep file names and line numbers.
-keep public class * extends java.lang.Exception  # Optional: Keep custom exceptions.
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**
-keep class com.google.firebase.crashlytics.** { *; }
-dontwarn com.google.firebase.crashlytics.**

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# ServiceLoader support
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepnames class kotlinx.coroutines.android.AndroidExceptionPreHandler {}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}

# Most of volatile fields are updated with AFU and should not be mangled
-dontwarn org.jetbrains.annotations.**
-keep class kotlin.Metadata { *; }
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# https://stackoverflow.com/a/60449002/3763032
-keep class org.threeten.bp.zone.*

# DataBinding
-dontwarn androidx.databinding.**
-keep class androidx.databinding.** { *; }
-keep class * extends androidx.databinding.DataBinderMapper

# Constant
-keepnames @kotlin.Metadata class com.sampingan.agentapp.dynamic_ui.model.** { *; }
-keepnames @kotlin.Metadata com.sampingan.agentapp.dynamic_ui.rule.** { *; }
-keepnames @kotlin.Metadata com.sampingan.agentapp.dynamic_ui.utils.** { *; }

-keep  class com.sampingan.agentapp.dynamic_ui.model.** { *; }
-keep  class com.sampingan.agentapp.dynamic_ui.rule.** { *; }
-keep  class com.sampingan.agentapp.dynamic_ui.utils.** { *; }

-keepclassmembers class com.sampingan.agentapp.dynamic_ui.model.** { *; }
-keepclassmembers class com.sampingan.agentapp.dynamic_ui.rule.** { *; }
-keepclassmembers class com.sampingan.agentapp.dynamic_ui.utils.** { *; }

-keepclassmembers class * {
 public static ** Companion;
}