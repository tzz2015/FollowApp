
import java.util.*
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

val propFile: File = File("D:\\code\\android\\AutoX-dev-test\\keystore.properties")
val properties = Properties()
if (propFile.exists()) {
    propFile.reader().use {
        properties.load(it)
    }
}

android {
    namespace = "com.lyflovelyy.followhelper"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.lyflovelyy.followhelper"
        minSdk = 24
        targetSdk = 33
        versionCode = 100
        versionName = "1.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        if (propFile.exists()) {
            create("release") {
                storeFile = file(properties.getProperty("storeFile"))
                storePassword = properties.getProperty("storePassword")
                keyAlias = properties.getProperty("keyAlias")
                keyPassword = properties.getProperty("keyPassword")
            }
        }
    }
    buildTypes {
        getByName("debug") {
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
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
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
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.6.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation(project(":DataLibaray"))
    implementation("com.github.SenhLinsh:Utils-Everywhere:3.0.0")
    //Dagger
    val dagger_version = "2.44"
    implementation("com.google.dagger:hilt-android:${dagger_version}")
    kapt("com.google.dagger:hilt-android-compiler:${dagger_version}")

}