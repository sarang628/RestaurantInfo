
```
android.buildFeatures.buildConfig = true

buildTypes {
    getByName("debug") {
        buildConfigField("String", "SERVER_URL", "\"http://sarang628.iptime.org\"")
        buildConfigField("String", "IMAGE_PORT", "\"89\"")
        buildConfigField("String", "PROFILE_IMAGE_SERVER_URL", "\"http://sarang628.iptime.org:89/profile_images/\"")
        buildConfigField("String", "REVIEW_IMAGE_SERVER_URL", "\"http://sarang628.iptime.org:89/review_images/\"")
        buildConfigField("String", "RESTAURANT_IMAGE_SERVER_URL", "\"http://sarang628.iptime.org:89/restaurant_images/\"")
        buildConfigField("String", "MENU_IMAGE_SERVER_URL", "\"http://sarang628.iptime.org:89/menu_images/\"")
    }

    getByName("release") {
        buildConfigField("String", "SERVER_URL", "\"http://sarang628.iptime.org\"")
        buildConfigField("String", "IMAGE_PORT", "\"89\"")
        buildConfigField("String", "PROFILE_IMAGE_SERVER_URL", "\"http://sarang628.iptime.org:89/profile_images/\"")
        buildConfigField("String", "REVIEW_IMAGE_SERVER_URL", "\"http://sarang628.iptime.org:89/review_images/\"")
        buildConfigField("String", "RESTAURANT_IMAGE_SERVER_URL", "\"http://sarang628.iptime.org:89/restaurant_images/\"")
        buildConfigField("String", "MENU_IMAGE_SERVER_URL", "\"http://sarang628.iptime.org:89/menu_images/\"")
        isMinifyEnabled = false
        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        signingConfig = signingConfigs.getByName("debug")
    }
}

implementation("com.github.sarang628:ComposePermissionTest:5159bc3d34")
implementation("com.github.sarang628:TorangRepository:e0d12661da")
implementation("com.google.accompanist:accompanist-permissions:0.32.0")
implementation("com.google.android.gms:play-services-location:21.1.0")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
```

hilt 추가

```
build.gradle
plugins {
  ...
  id("com.google.dagger.hilt.android") version "2.56.2" apply false
}

app/build.gradle
plugins {
  id("com.google.devtools.ksp")
  id("com.google.dagger.hilt.android")
}

android {
  ...
}

dependencies {
  implementation("com.google.dagger:hilt-android:2.56.2")
  ksp("com.google.dagger:hilt-android-compiler:2.56.2")
}
```