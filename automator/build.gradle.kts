plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    buildToolsVersion = versions.buildTool
    compileSdk = versions.compile

    defaultConfig {
        minSdk = versions.mini
        targetSdk = versions.target
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    lintOptions.isAbortOnError = false
    buildTypes {
        named("release") {
            isMinifyEnabled = false
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android.txt"),
                    "proguard-rules.pro"
                )
            )
        }
    }
    sourceSets {
        named("main") {
            jniLibs.srcDirs("/libs")
        }
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    androidTestImplementation("androidx.test.espresso:espresso-core:3.1.1-alpha01") {
        exclude(group = "com.android.support", module = "support-annotations")
    }
    testImplementation("junit:junit:4.13.2")
    api("androidx.appcompat:appcompat:1.4.2") {
        exclude(group = "com.google.android.material")
    }
    api(project(":common"))
}
