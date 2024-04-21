plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
    // id("com.google.gms.google-services")
}

android {
    namespace = "com.oniverse.fitmap"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.oniverse.fitmap"
        minSdk = 24
        targetSdk = 34
        versionCode = 2
        versionName = "A0.2"

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
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")


}