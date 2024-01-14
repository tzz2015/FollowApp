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
-keep class android.support.** { *; }
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v7.** { *; }
-keep public class * extends android.support.v7.**
-keep interface android.support.v7.app.** { *; }
-dontwarn android.support.**
-keep public class * implements android.support.v4.**
-dontwarn android.support.v4.**
-keepclasseswithmembers class * {
    native <methods>;
}
-keepclasseswithmembers class * {
        public <init>(android.content.Context);
    }
-keepclasseswithmembers class * {
        public <init>(android.content.Context,android.util.AttributeSet);
    }
-keepclasseswithmembers class * {
        public <init>(android.content.Context,android.util.AttributeSet,int);
    }
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-assumenosideeffects class android.util.Log {
        public static *** d(...);
        public static *** v(...);
        public static *** i(...);
        public static *** e(...);
        public static *** w(...);
    }

    -dontwarn android.support.v4.**
    -keep class android.support.v4.app.** { *; }
    -keep interface android.support.v4.app.** { *; }
    -keep class android.support.v4.** { *; }
    -keep public class * extends android.app.Application

    -dontwarn android.support.v7.**
    -keep class android.support.v7.internal.** { *; }
    -keep interface android.support.v7.internal.** { *; }
    -keep class android.support.v7.** { *; }

    -keep public class * extends android.app.Activity
    -keep public class * extends android.app.Fragment
    -keep public class * extends android.app.Application
    -keep public class * extends android.app.Service
    -keep public class * extends android.content.BroadcastReceiver
    -keep public class * extends android.content.ContentProvider
    -keep public class * extends android.app.backup.BackupAgentHelper
    -keep public class * extends android.preference.Preference
    -keep public class * extends androidx.lifecycle.ViewModel
    -keep public class * extends androidx.lifecycle.MutableLiveData

-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }
-keepclassmembers class **.R$* {
        public static <fields>;
    }

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



-keep class com.stardust.auojs.inrt.autojs.**{*;}


#log4j
-dontwarn org.apache.log4j.**
-keep class  org.apache.log4j.** { *;}

#kotlin
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}
-keep class kotlinx.coroutines.android.** {*;}

-keep public class * extends com.mind.lib.base.BaseViewModel
-keep class androidx.arch.core.internal.** { *; }


# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { <fields>; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keep class com.google.gson.** {*;}
-keep class com.google.**{*;}
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

##---------------End: proguard configuration for Gson  ----------

# Okhttp3的混淆配置
# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*
# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform


# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
# Logging Interceptor
-dontwarn okhttp3.logging.**
# OkHttp3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-keep class okhttp3.** { *;}
-dontwarn okio.**

# Okio
-dontwarn com.squareup.**
-dontwarn okio.**
-keep class org.codehaus.** { *; }
-keep class java.nio.** { *; }
-keep class okio.** {*;}

# Other libraries you might be using with Retrofit
-keep class org.apache.commons.logging.** { *; }

#BaseRecyclerViewAdapterHelper
-keepclassmembers  class **$** extends com.chad.library.adapter.base.viewholder.BaseViewHolder {
     <init>(...);
}
-keep class com.chad.library.adapter.base.viewholder.BaseDataBindingHolder {*;}

#-keepattributes InnerClasses
#
#-keep class androidx.** {*;}


#open_ad_sdk
	-keep class com.bytedance.sdk.openadsdk.** { *; }
	-keep public interface com.bytedance.sdk.openadsdk.downloadnew.** {*;}
	-keep class com.ss.**{*;}
	-dontwarn com.ss.**
	# miitmdid
	-keep class com.bun.miitmdid.core.** {*;}
	-dontwarn com.bun.miitmdid.core.**

	#广点通 start
	-keep class com.qq.e.** {*;}
	-dontwarn com.qq.e.**
	#广点通 end

	#快手
	-keep class org.chromium.** {*;}
	-keep class org.chromium.** { *; }
	-keep class aegon.chrome.** { *; }
	-keep class com.kwai.**{ *; }
	-dontwarn com.kwai.**
	-dontwarn com.kwad.**
	-dontwarn com.ksad.**
	-dontwarn aegon.chrome.**
	#快手

	# WindAd
	-keep class com.sigmob.**{*;}
	-dontwarn com.sigmob.**
	# WindAd


#baidu start
-ignorewarnings
-dontwarn com.baidu.mobads.sdk.api.**
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class com.baidu.mobads.** { *; }
-keep class com.style.widget.** {*;}
-keep class com.component.** {*;}
-keep class com.baidu.ad.magic.flute.** {*;}
-keep class com.baidu.mobstat.forbes.** {*;}
#baidu end

#oaid start
-keep class com.asus.msa.**{*;}
-keep class com.bun.**{*;}
-keep class com.huawei.hms.ads.identifier.**{*;}
-keep class com.netease.nis.sdkwrapper.**{*;}
-keep class com.samsung.android.deviceidservice.**{*;}
-keep class com.zui.**{*;}
-keep class XI.**{*;}
#oaid end

#openset start
-keep class com.kc.openset.**{*;}
-dontwarn com.kc.openset.**
#openset end

#-------------- okhttp3 start-------------
# OkHttp3
# https://github.com/square/okhttp
# okhttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.* { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

# okhttp 3
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

# Okio
-dontwarn com.squareup.**
-dontwarn okio.**
-keep public class org.codehaus.* { *; }
-keep public class java.nio.* { *; }
#----------okhttp end--------------

# log start
-keep class com.aliyun.sls.android.producer.* { *; }
-keep interface com.aliyun.sls.android.producer.* { *; }
# log end

# 倍孜混淆
-dontwarn com.beizi.fusion.**
-dontwarn com.beizi.ad.**
-keep class com.beizi.fusion.** {*; }
-keep class com.beizi.ad.** {*; }

-keep class com.qq.e.** {
    public protected *;
}

-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

-dontwarn  org.apache.**

#sdk加固

##Glide
-dontwarn com.bumptech.glide.**
-keep class com.bumptech.glide.**{*;}
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

-keep class com.lyflovelyy.followhelper.entity.** {*;}













