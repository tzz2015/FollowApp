import java.util.*
import kotlin.collections.*

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")

}

val propFile: File = File("E:/资料/jks/autojs-inrt/sign.properties");
val properties = Properties()
if (propFile.exists()) {
    propFile.reader().use {
        properties.load(it)
    }
}

android {
    buildToolsVersion = versions.buildTool
    compileSdk = 33
    defaultConfig {
        applicationId = "org.autojs.autoxjs.inrt"
        minSdk = versions.mini
        targetSdk = versions.target
        versionCode = versions.appVersionCode
        versionName = versions.appVersionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        multiDexEnabled = true
//        buildConfigField("boolean","isMarket","true") // 这是有注册码的版本
        buildConfigField("boolean", "isMarket", "false")
        javaCompileOptions {
            annotationProcessorOptions {
                arguments["resourcePackageName"] = applicationId.toString()
                arguments["androidManifestFile"] = "$projectDir/src/main/AndroidManifest.xml"
            }
        }
    }

    lint.disable.apply {
        add("MissingTranslation")
        add("ExtraTranslation")
    }
    compileOptions {
        encoding = "utf-8"
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    signingConfigs {
        if (propFile.exists()) {
            getByName("release") {
                storeFile = file(properties.getProperty("storeFile"))
                storePassword = properties.getProperty("storePassword")
                keyAlias = properties.getProperty("keyAlias")
                keyPassword = properties.getProperty("keyPassword")
            }
        }
    }
    buildTypes {
        named("debug") {
            isMinifyEnabled = false
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android.txt"),
                    "proguard-rules.pro"
                )
            )
            if (propFile.exists()) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
        named("release") {
            isMinifyEnabled = false
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android.txt"),
                    "proguard-rules.pro"
                )
            )
            if (propFile.exists()) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }
    flavorDimensions.apply {
        add("channel")
    }


    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
    productFlavors {
        create("common") {
            buildConfigField("boolean", "isMarket", "false")
            manifestPlaceholders.putAll(mapOf("appName" to "互关助手"))
            ndk.abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a"))
        }
        create("template") {
            manifestPlaceholders.putAll(mapOf("appName" to "互关助手"))
            packagingOptions.apply {
                jniLibs.excludes.add("*")
            }
            ndk.abiFilters.add("")
        }
    }
    sourceSets {
        named("main") {
            jniLibs.srcDir("/libs")
            res.srcDirs("src/main/res", "src/main/res-i18n")
        }
    }
    packagingOptions {
        jniLibs {
            useLegacyPackaging = true
        }
        jniLibs.pickFirsts.addAll(
            listOf(
                "lib/arm64-v8a/libc++_shared.so",
                "lib/arm64-v8a/libhiai.so",
                "lib/arm64-v8a/libhiai_ir.so",
                "lib/arm64-v8a/libhiai_ir_build.so",
                "lib/arm64-v8a/libNative.so",
                "lib/arm64-v8a/libpaddle_light_api(_shared.so",
                "lib/armeabi-v7a/libc++_shared.so",
                "lib/armeabi-v7a/libhiai.so",
                "lib/armeabi-v7a/libhiai_ir.so",
                "lib/armeabi-v7a/libhiai_ir_build.so",
                "lib/armeabi-v7a/libNative.so",
                "lib/armeabi-v7a/libpaddle_light_api(_shared.so"
            )
        )

    }


}

android.applicationVariants.all {
    val variant = this
    variant.mergeAssetsProvider.configure {
        doLast {
            if (variant.flavorName == "template") {
                delete(
                    fileTree(outputDir) {
                        include(
                            "models/**/*",
                            "mlkit-google-ocr-models/**/*",
                            "project/**/*"
                        )
                    }
                )
            }
        }
    }

}

fun buildApkPlugin(pluginProjectDir: File, isDebug: Boolean) {
    copy {
        var form = "build\\outputs\\apk\\template\\"
        form += if (isDebug) "debug\\"
        else "release\\"
        from(file(form))
        into(File(pluginProjectDir, "src\\main\\assets"))
        var fileName = "inrt-template-release-unsigned.apk"
        if (isDebug) fileName = "inrt-template-debug.apk"
        include(fileName)
        rename(fileName, "template.apk")
    }
    println("app cp ok")
}

tasks.register("cp2APP") {
    doLast {
        copyTemplateToAPP(false)
    }
}

tasks.register("cp2APPDebug") {
    doLast {
        copyTemplateToAPP(true)
    }
}

fun copyTemplateToAPP(isDebug: Boolean) {
    val pluginProjectDirPath = "..\\app"
    println(pluginProjectDirPath)
    val pluginProjectDir = file(pluginProjectDirPath)
    if (!pluginProjectDir.exists() || !pluginProjectDir.isDirectory) {
        println("app 目录 not exists")
        return
    }
    println(pluginProjectDir)
    // buildApkPluginForAbi(pluginProjectDir, "armeabi-v7a")
    buildApkPlugin(pluginProjectDir, isDebug)
}

dependencies {
    implementation("androidx.activity:activity-ktx:1.3.1")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.6.0")
    implementation("androidx.annotation:annotation:1.4.0")
    implementation("androidx.preference:preference-ktx:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.1.1-alpha01") {
        exclude(group = "com.android.support", "support-annotations")
    }

    // RxJava
    implementation("io.reactivex.rxjava2:rxjava:2.2.21")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("com.fanjun:keeplive:1.1.22")
    implementation("com.dhh:websocket2:2.1.4")
    implementation("com.github.SenhLinsh:Utils-Everywhere:3.0.0")
    testImplementation("junit:junit:4.13.2")
    implementation(project(":automator"))
    implementation(project(":common"))
    implementation(project(":autojs"))
    implementation(project(":DataLibaray"))
    implementation("androidx.multidex:multidex:2.0.1")
    api(fileTree("../app/libs") { include("dx.jar", "rhino-1.7.14-jdk7.jar") })


    //glide
    implementation("com.github.bumptech.glide:glide:4.2.0") {
        exclude(group = "com.android.support")
    }
    val dagger_version = "2.44"
    //Dagger
    implementation("com.google.dagger:hilt-android:${dagger_version}")
    kapt("com.google.dagger:hilt-android-compiler:${dagger_version}")

    // x5
    implementation("com.tencent.tbs.tbssdk:sdk:43939")
    implementation("pub.devrel:easypermissions:3.0.0")

    // 标签库
    implementation("com.google.android.flexbox:flexbox:3.0.0")

}
