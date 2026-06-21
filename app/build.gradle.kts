plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.detekt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "org.goodexpert.ridefit"
    compileSdk = 37

    defaultConfig {
        applicationId = "org.goodexpert.ridefit"
        minSdk = 28
        versionCode = 1
        versionName = "0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("debug.keystore")
            storePassword = "android"
            keyAlias = "AndroidDebugKey"
            keyPassword = "android"
        }

        create("release") {
            storeFile = file("../keystore/release-key.jks")
            storePassword = "ridefit123"
            keyAlias = "ridefit-alias"
            keyPassword = "ridefit123"
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
            signingConfig = signingConfigs.getByName("debug")
        }
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = if (file("../keystore/release-key.jks").exists()) {
                signingConfigs.getByName("release")
            } else {
                signingConfigs.getByName("debug")
            }
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
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            all { test ->
                // Forward roborazzi mode flags from Gradle command line to the forked test JVM.
                // Record:  ./gradlew testDebugUnitTest -Proborazzi.test.record=true
                // Verify:  ./gradlew testDebugUnitTest  (default — compare only, no fail)
                listOf("roborazzi.test.record", "roborazzi.test.verify", "roborazzi.test.compare")
                    .forEach { key ->
                        project.findProperty(key)?.toString()?.let { test.systemProperty(key, it) }
                    }
            }
        }
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.text.google.fonts)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    testImplementation(libs.junit)
    testImplementation(platform(libs.androidx.compose.bom))
    testImplementation(libs.roborazzi.compose)
    testImplementation(libs.robolectric)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}