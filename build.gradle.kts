plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
    source.setFrom(
        "app/src/main/java",
        "app/src/test/java",
        "app/src/androidTest/java",
    )
}

dependencies {
    detektPlugins(libs.detekt.formatting)
    detektPlugins(libs.detekt.compose)
}
