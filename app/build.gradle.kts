plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.android_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.android_app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {

    // ---Compose---
    implementation("androidx.compose.runtime:runtime:1.6.5")
    implementation ("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3-window-size-class:1.2.1")
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.compose.material:material:1.6.5")
    implementation("androidx.compose.ui:ui:1.6.5")
    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")
    // Full set of material icons
    implementation("androidx.compose.material:material-icons-extended")

    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Accompanist (google). I use it for custom status bar
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.27.0")

    // ---

    // TarsosDSP (audio processing) https://github.com/JorenSix/TarsosDSP
    implementation(files("libs/TarsosDSP-Android-2.4.jar"))

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")

    implementation(project(mapOf("path" to ":musiclib")))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation(kotlin("test-junit5"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
}