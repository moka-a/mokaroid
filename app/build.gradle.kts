import java.io.FileInputStream
import java.util.*
import java.time.*
import java.time.format.DateTimeFormatter.ofPattern
import org.jetbrains.kotlin.gradle.dsl.Coroutines

val kotlin_version: String by project

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("com.apollographql.apollo") version "2.3.1"
}

val BUILD_DATE = LocalDateTime.now().format(ofPattern("yyyy_MM_dd"))
val APP_NAME = "mokaroid"
val apiKeyProperties = Properties()
try {
    val apiKeyPropertiesFile = rootProject.file("app/apikey.properties")
    apiKeyProperties.load(FileInputStream(apiKeyPropertiesFile))
}
catch (e: Exception) {
    val apiKeyPropertiesFile = rootProject.file("app/apikey.properties.sample")
    apiKeyProperties.load(FileInputStream(apiKeyPropertiesFile))
    e.printStackTrace()
}

android {
    compileSdkVersion(29)
    buildToolsVersion = "29.0.2"

    defaultConfig {
        applicationId = "moka.land"

        minSdkVersion(21)
        targetSdkVersion(29)

        versionCode = 1
        versionName = "1.0"

        vectorDrawables.useSupportLibrary = true
        setProperty("archivesBaseName", APP_NAME + "_v" + versionCode + "(" + versionName + ")_" + BUILD_DATE)
        buildConfigField("String", "apikey", apiKeyProperties["apikey"].toString())
    }
    buildFeatures {
        viewBinding = true
    }
    buildTypes {
        val release by getting {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java).all {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

kotlin {
    experimental.coroutines = Coroutines.ENABLE
}

dependencies {
    /* Moka */
    implementation(project(":base"))
    implementation(project(":dialog"))
    implementation(project(":webview"))
    implementation(project(":imagehelper"))
    implementation(project(":fileutil"))
    implementation(project(":permissionmanager"))
    implementation(project(":adhelper"))

    /* Kotlin & Coroutine */
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.0")

    /* Koin */
    implementation("org.koin:koin-androidx-viewmodel:1.0.2")
    implementation("org.koin:koin-androidx-scope:1.0.2")

    /* Android X */
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.core:core-ktx:1.1.0")
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("androidx.exifinterface:exifinterface:1.2.0")

    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.2.0-alpha05")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.0-beta8")
    implementation("com.google.android.material:material:1.1.0")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.1.0")

    implementation("androidx.work:work-runtime-ktx:2.2.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.2.0-rc02")
    implementation("androidx.navigation:navigation-ui-ktx:2.2.0-rc02")
    implementation("androidx.lifecycle:lifecycle-extensions:2.1.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.2.0-rc02")
    kapt("androidx.lifecycle:lifecycle-compiler:2.1.0")

    /* glide */
    implementation("com.github.bumptech.glide:glide:4.10.0")

    /* Room */
    implementation("androidx.room:room-ktx:2.2.5")
    implementation("androidx.room:room-runtime:2.2.5")
    kapt("androidx.room:room-compiler:2.2.5")

    /* Network */
    implementation("com.apollographql.apollo:apollo-runtime:2.3.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.0.0")

    implementation("com.facebook.stetho:stetho:1.5.1")
    implementation("com.facebook.stetho:stetho-okhttp3:1.5.1")

}
