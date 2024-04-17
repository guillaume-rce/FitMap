plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.oniverse.fitmap"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.oniverse.fitmap"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "A0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}


dependencies {
    implementation("com.github.lecho:hellocharts-library:1.5.8")
    implementation("org.osmdroid:osmdroid-android:6.1.18")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("org.simpleframework:simple-xml:2.7.1")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation ("com.nexmo.android:client-sdk:4.0.0")
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.google.firebase:firebase-analytics:21.6.2")
    implementation ("com.google.firebase:firebase-database:20.3.1")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.firebaseui:firebase-ui-auth:8.0.2")
    implementation ("com.google.firebase:firebase-firestore:24.11.1")
    implementation ("com.google.firebase:firebase-storage:20.3.0")
    implementation ("com.google.firebase:firebase-appcheck:17.1.2")
    implementation ("com.google.firebase:firebase-appcheck:16.0.0-beta01")
    implementation ("com.google.firebase:firebase-appcheck-playintegrity:17.1.2")
    implementation ("com.google.firebase:firebase-appcheck-debug:17.1.2")
    implementation ("com.google.android.play:integrity:1.3.0")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")


}