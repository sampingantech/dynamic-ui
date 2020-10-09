# model
-keepnames @kotlin.Metadata class com.google.gson.JsonObject.** { *; }
-keep class com.google.gson.JsonObject.** { *; }
-keepclassmembers class com.google.gson.JsonObject.** { *; }

-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

-keepnames @kotlin.Metadata class co.sampingan.android.dynamic_ui.model.** { *; }
-keepnames @kotlin.Metadata class co.sampingan.android.dynamic_ui.rule.** { *; }
-keepnames @kotlin.Metadata class co.sampingan.android.dynamic_ui.utils.** { *; }

-keep class co.sampingan.android.dynamic_ui.model.** { *; }
-keep class co.sampingan.android.dynamic_ui.rule.** { *; }
-keep class co.sampingan.android.dynamic_ui.utils.** { *; }

-keepclassmembers class co.sampingan.android.dynamic_ui.model.** { *; }
-keepclassmembers class co.sampingan.android.dynamic_ui.rule.** { *; }
-keepclassmembers class co.sampingan.android.dynamic_ui.utils.** { *; }