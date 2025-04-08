// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.parcelize) apply false
    alias(libs.plugins.androidx.navigation.safeargs) apply false // required for navigation component
    alias(libs.plugins.jetbrains.kotlin.kapt) apply false
    alias(libs.plugins.com.google.devtools.ksp) apply false
    alias(libs.plugins.com.google.dagger.hilt.android) apply false // for dependency injection
    alias(libs.plugins.kotlin.serialization) apply false
}
tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}