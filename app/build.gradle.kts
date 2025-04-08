plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.parcelize)
    alias(libs.plugins.androidx.navigation.safeargs)// required for navigation component
    alias(libs.plugins.jetbrains.kotlin.kapt)
    alias(libs.plugins.com.google.devtools.ksp)
    alias(libs.plugins.com.google.dagger.hilt.android)
}

android {
    namespace = "com.example.hrms"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.hrms"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures{
        viewBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }
    kapt {
        correctErrorTypes = true
    }


}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.cardview)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation (libs.ssp.android)
    implementation (libs.sdp.android)

    implementation (libs.gson)
    implementation (libs.okhttp )
    implementation (libs.logging.interceptor)
    implementation (libs.rxjava)
    implementation (libs.rxjava3.rxandroid)
    implementation (libs.retrofit.v290)
    implementation (libs.converter.gson.v290)
    implementation (libs.retrofit2.adapter.rxjava3)

    /////////////////////////
    // For MVVM architecture
    ////////////////////////
    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Core & ViewModel & Livedata
    implementation(libs.androidx.core.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Dagger-2 for dependency injection
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
    implementation(libs.dagger.android)
    kapt(libs.dagger.android.processor)

    // Hilt for dependency injection
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    //////////////////////
    implementation(libs.kotlinx.serialization.json)

    implementation ("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")
    ksp("com.github.bumptech.glide:ksp:4.14.2")


}