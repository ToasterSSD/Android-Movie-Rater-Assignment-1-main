@file:Suppress("DEPRECATION")

// Top-level build file where you can add configuration options common to all sub-projects/modules.



buildscript {
    extra.apply {
        set("nav_version", "2.8.4")
        set("room_version", "2.6.1")
        set("lifecycle_version", "2.8.7")
    }
}


plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.android.library") version "8.8.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0" apply false

}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}