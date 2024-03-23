plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.dladukedev.bibleyeartracker"
    compileSdk = 34

    defaultConfig {
        applicationId =  "com.dladukedev.bibleyeartracker"
        minSdk =  29
        targetSdk = 34
        versionCode =  1
        versionName = "1.0"

        testInstrumentationRunner = "com.dladukedev.bibleyeartracker.HiltTestApplicationRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    packagingOptions.resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
}

dependencies {

    // TODO: Clean out
    implementation(libs.androidx.ktx)
    implementation(libs.lifecycle.runtime.ktx)

    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.toolingPreview)
    implementation(libs.compose.material3)
    implementation(project(":feature:settings"))
    implementation(project(":feature:reading"))
    implementation(project(":feature:statistics"))
    implementation(project(":core:preferences"))
    implementation(project(":common:models"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.navigation.compose)
    implementation(libs.hilt.navigationCompose)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    kspAndroidTest(libs.hilt.android.compiler)
    androidTestImplementation(libs.hilt.android.testing)
    implementation(libs.androidx.lifecycle.compose)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.datastore)
    implementation(libs.splashscreen)
}
