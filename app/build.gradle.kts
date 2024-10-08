@file:Suppress("UNUSED_EXPRESSION")

plugins {
    alias(libs.plugins.android.application)
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    implementation("com.github.florent37:runtime.permission:1.1.2")  // for runtime permission

    implementation("com.github.bumptech.glide:glide:4.10.0")  // show images

    implementation("com.google.android.gms:play-services-cast-framework:21.5.0")

    implementation("androidx.appcompat:appcompat:1.6.1")

    implementation("com.google.android.material:material:1.10.0")

    implementation("androidx.activity:activity:1.8.0")

    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("com.android.support:support-annotations:28.0.0")  // support annotations

    debugImplementation("com.amitshekhar.android:debug-db:1.0.6")  // view database

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")  // for ViewModelScope

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")  // for lifecycleScope

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}