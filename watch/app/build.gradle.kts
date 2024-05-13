plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.santeut"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.santeut"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.play.services.wearable)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.tooling.preview)
    implementation(libs.compose.material)
    implementation(libs.compose.foundation)
    implementation(libs.activity.compose)
    implementation(libs.core.splashscreen)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    implementation(libs.androidx.material3)

    implementation(libs.kotlinx.coroutines)

    // permission
    implementation(libs.accompanist.permissions)

    // Ongoing Activity
    implementation(libs.wear.ongoing.activity)

    // Lifecycle
    implementation (libs.androidx.lifecycle.viewmodel)
    implementation (libs.androidx.lifecycle.runtime)
    implementation (libs.androidx.lifecycle.common.java8)
    implementation (libs.androidx.lifecycle.extensions)
    implementation (libs.androidx.lifecycle.service)
    implementation (libs.androidx.lifecycle.runtime.compose)
    implementation (libs.androidx.core.splashscreen)

    implementation (libs.guava)
    implementation (libs.androidx.concurrent)

    // Health Services
    implementation (libs.androidx.health.services)

    implementation (libs.horologist.health.service)

    // Hilt
    implementation (libs.hilt.navigation.compose)
    implementation (libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.android.compiler)

    // Map
    implementation(libs.google.map.compose)
    implementation(libs.android.gms.service.map)
    implementation(libs.android.gms.service.location)
}
