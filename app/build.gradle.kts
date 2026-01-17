plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    //Menambahkan plugin serialisasi sesuai REQ-312 untuk pertukaran data JSON
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.safeguard"
    compileSdk = 36
    // Menggunakan SDK stabil terbaru yang mendukung API 28+

    defaultConfig {
        applicationId = "com.example.safeguard"
        //WAJIB: Sesuai SRS Bagian 2.4 Sisi Client (Operating Environment)
        minSdk = 28 //Android 9.0 (Pie)
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    //Navigasi: Untuk alur Login ke Dashboard [cite: 209, 280]
    implementation(libs.androidx.navigation.compose)
    //Sesi: Untuk manajemen login petugas REQ-4
    implementation(libs.androidx.datastore.preferences)
    //Networking: Implementasi interaksi MySQL via API [cite: 127, 304, 307]
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    //Serialization: Pertukaran data format JSON sesuai REQ-312 [cite: 312]
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.kotlinx.serialization.converter)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    //Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}