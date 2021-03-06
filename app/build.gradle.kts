plugins {
    id("com.google.secrets_gradle_plugin") version "0.4"
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "com.example.frequency"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        applicationVariants.all {
            val variant = this
            variant.outputs
                    .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
                    .forEach { output ->
                        val outputFileName =
                                "${rootProject.name} ${variant.versionName} - ${variant.baseName}.apk"
                        println("OutputFileName: $outputFileName")
                        output.outputFileName = outputFileName
                    }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
    viewBinding {
        isEnabled = true
    }

}

dependencies {

    val lifecycleVersion = "2.4.1"
    val roomVersion = "2.4.2"
    val lottieVersion = "5.0.3"
    val okhttp3Version = "5.0.0-alpha.6"
    val glideVersion = "4.13.1"

    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    // for activity & fragments viewMode
    implementation("androidx.fragment:fragment-ktx:1.4.1")
    implementation("androidx.activity:activity-ktx:1.4.0")
    //lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycleVersion")
    //coroutines flow
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")
    // firebase
    implementation(platform("com.google.firebase:firebase-bom:29.1.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-auth:21.0.3")
    // splashscreen
    implementation("androidx.core:core-splashscreen:1.0.0-beta02")
    // Ro")
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //okhttp
    implementation("com.squareup.okhttp3:okhttp:$okhttp3Version")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttp3Version")
    // Glide
    implementation("com.github.bumptech.glide:glide:$glideVersion")
    implementation("com.github.bumptech.glide:cronet-integration:$glideVersion")
    implementation("com.github.bumptech.glide:recyclerview-integration:$glideVersion") {
        // Excludes the support library because it("s already included by Glide
        isTransitive = false
    }
    kapt("com.github.bumptech.glide:compiler:$glideVersion")
    // lottie
    implementation("com.airbnb.android:lottie:$lottieVersion")
    // hilt di
    implementation("com.google.dagger:hilt-android:2.41")
    implementation("androidx.hilt:hilt-navigation-fragment:1.0.0")
    kapt("com.google.dagger:hilt-compiler:2.41")
    // Shapable Image View
    implementation("com.google.android.material:material:1.5.0")
    // Circle Image View
    implementation("de.hdodenhof:circleimageview:3.1.0")
    // com.example.frequency.preferences.Preference
    implementation("androidx.preference:preference:1.2.0")
    // google service
    implementation("com.google.android.gms:play-services-auth:20.1.0")
    // using ExoPlayer
    implementation("com.google.android.exoplayer:exoplayer:2.17.1")
    // Coronet for QUIC streaming
    implementation("com.google.android.exoplayer:extension-cronet:2.17.1")
    implementation("com.google.android.gms:play-services-cronet:18.0.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}