# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\YiBin\eclipse\Android_SDK_windows/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

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

-keep class com.hzy.lib7z.** { *; }
-keep class com.stardust.autojs.engine.** { *; }
-keep class com.stardust.autojs.engine.encryption** { *; }
-keep class com.stardust.autojs.engine.module.** { *; }
-keep class com.stardust.autojs.engine.preprocess** { *; }
-keep class com.stardust.autojs.engine.encryption.** { *; }
-keep class com.stardust.autojs.engine.module.** { *; }
-keep class com.stardust.autojs.engine.preprocess.** { *; }
-keep class com.stardust.autojs.execution.** { *; }
-keep class com.stardust.autojs.** { *; }
-keep interface com.stardust.autojs.** { *; }
-keep class org.mozilla.javascript.ScriptRuntime.** { *; }
-keep class com.stardust.autojs.annotation.** { *; }
-keep class com.stardust.autojs.codegeneration.** { *; }
-keep class com.stardust.autojs.core.** { *; }
-keep class com.stardust.autojs.core.accessibility.** { *; }
-keep class com.stardust.autojs.core.activity** { *; }
-keep class com.stardust.autojs.core.boardcast** { *; }
-keep class com.stardust.autojs.core.console** { *; }
-keep class com.stardust.autojs.core.cypto.** { *; }
-keep class com.stardust.autojs.core.database.** { *; }
-keep class com.stardust.autojs.core.eventloop.** { *; }
-keep class com.stardust.autojs.core.floaty.** { *; }
-keep class com.stardust.autojs.core.http.** { *; }
-keep class com.stardust.autojs.core.image.** { *; }
-keep class com.stardust.autojs.core.inputevent.** { *; }
-keep class com.stardust.autojs.core.internal.** { *; }
-keep class com.stardust.autojs.core.looper.** { *; }
-keep class com.stardust.autojs.core.mlkit.** { *; }
-keep class com.stardust.autojs.core.opencv.** { *; }
-keep class com.stardust.autojs.core.permission.** { *; }
-keep class com.stardust.autojs.core.plugin.** { *; }
-keep class com.stardust.autojs.core.pref.** { *; }
-keep class com.stardust.autojs.core.record.** { *; }
-keep class com.stardust.autojs.core.storage.** { *; }
-keep class com.stardust.autojs.core.ui.** { *; }
-keep class com.stardust.autojs.core.util.** { *; }
-keep class com.stardust.autojs.core.web.** { *; }
-keep class com.stardust.autojs.project.** { *; }
-keep class com.stardust.autojs.rhino.** { *; }
-keep class com.stardust.autojs.runtime.** { *; }
-keep class com.stardust.autojs.script.** { *; }
-keep class com.stardust.autojs.util.** { *; }
-keep class com.stardust.autojs.workground.** { *; }
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.hzy.lib7z.** { *; }

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { <fields>; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keep class com.google.gson.** {*;}
-keep class com.google.**{*;}
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

##---------------End: proguard configuration for Gson  ----------




































