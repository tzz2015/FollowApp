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

#指定代码的压缩级别
-optimizationpasses 5

#包明不混合大小写
-dontusemixedcaseclassnames

#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses

 #优化  不优化输入的类文件
-dontoptimize

 #预校验
-dontpreverify

 #混淆时是否记录日志
-verbose

 # 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#保护注解
-keepattributes *Annotation*

-dontwarn org.mozilla.javascript.**
-dontwarn jackpal.androidterm.**
-keep class org.mozilla.javascript.** { *; }

# tencent

-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

-dontwarn dalvik.**
-dontwarn com.tencent.smtt.**

-keep class com.tencent.smtt.** {
    *;
}

-keep class com.tencent.tbs.** {
    *;
}

-keep class org.autojs.autoxjs.inrt.BuildConfig{
   *;
}

-keep interface kotlin.reflect.jvm.internal.impl.builtins.BuiltInsLoader{
    *;
}
-keep class kotlin.reflect.jvm.internal.impl.serialization.deserialization.builtins.BuiltInsLoaderImpl{
    *;
}

-keep class androidx.recyclerview.widget.RecyclerView$LayoutParams { *; }
-keep class androidx.recyclerview.widget.RecyclerView$ViewHolder { *; }
-keep class androidx.recyclerview.widget.ChildHelper { *; }
-keep class androidx.recyclerview.widget.RecyclerView$LayoutManager { *; }
-keep class com.google.android.material.tabs.TabLayout { *; }
-keep class com.google.android.material.tabs.TabLayout$TabView { *; }


-keepattributes JavascriptInterface
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keep class com.tencent.mm.opensdk.** {
   *;
}

-keep class androidx.core.view.GestureDetectorCompat {*;}

#修复TimelinePanel容易除非长按Event混淆
-keep class androidx.core.view.GestureDetectorCompat {*;}
-keep class androidx.core.view.GestureDetectorCompat$GestureDetectorCompatImplJellybeanMr2 {*;}
-keep class androidx.core.view.GestureDetectorCompat$GestureDetectorCompatImplBase {*;}
#修复广告库无法获取exoplayer
-keep class com.google.android.exoplayer2.**{*;}


