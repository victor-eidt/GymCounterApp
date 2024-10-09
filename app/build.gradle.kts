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

    implementation("com.github.florent37:runtime-permission:1.1.2")  // For runtime permissions
    implementation("com.github.bumptech.glide:glide:4.10.0")         // For showing images
    debugImplementation("com.amitshekhar.android:debug-db:1.0.6")    // For viewing database
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")  // For ViewModelScope
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1") // For lifecycleScope
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.support.annotations)
    implementation(libs.play.services.cast.framework)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.runtime.permission)  //for runtime permission
    implementation(libs.glide)   //show images
    debugImplementation (libs.debug.db)  //view database
    implementation (libs.androidx.lifecycle.viewmodel.ktx) // for viewModelScope
    implementation (libs.lifecycle.runtime.ktx) // for lifecycleScope
    implementation (libs.lifecycle.runtime.ktx) // for lifecycleScope
}