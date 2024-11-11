plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)


    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.sqldelight)

    id("dev.icerock.mobile.multiplatform-resources")

}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
//            isStatic = true
            export("dev.icerock.moko:resources:0.24.3")
//            export(libs.moko.resources)
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.stdlib)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlin.stdlib)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)

            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines)

            implementation(libs.multiplatform.settings)

            api(libs.moko.resources)

            api(libs.moko.mvvm.core)
            api(libs.moko.mvvm.flow)
            api(libs.koin.core)

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        androidMain.dependencies {
            commonMain
            implementation(libs.sqldelight.android.driver)
            implementation(libs.mlkit.translate)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.kotlinx.coroutines.play.services)
            api(libs.moko.mvvm.viewbinding)
        }
        iosMain.dependencies {
            commonMain
            implementation(libs.sqldelight.native.driver)
        }
    }
}

android {
    namespace = "com.example.translator_kmm"
    compileSdk = 34
    defaultConfig {
        minSdk = 28
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

sqldelight {
    database("AppDatabase") {
        packageName = "com.example.translator_kmm.data.source"
        sourceFolders = listOf("sqldelight")
    }
}

multiplatformResources {
    resourcesPackage.set("com.example.translator_kmm")
}
