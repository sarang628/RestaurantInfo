# 음식점 정보 화면 모듈

```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

dependencies {
    implementation("com.github.sarang628:RestaurantInfo:62628e5b8e")
}
```

```kotlin
RestaurantInfo(restaurantInfoData = RestaurantInfoData.dummy1())
```


## 이미지 로드 모듈 추가

```
cd app/src/main/java/[package]/di
git submodule add (or git clone) https://github.com/sarang628/image.git
```

```
cd app/src/main/java/[package]/di
git submodule add (or git clone) https://github.com/sarang628/pinchzoom.git
```

```
implementation("com.github.sarang628:CommonImageLoader:1999de5a48") 
```

``` kotlin
val customImageLoader: RestaurantInfoImageLoader = { modifier, url, width, height, scale ->
    // 여기서 실제 이미지 로딩 구현 예시
    provideTorangAsyncImage().invoke(modifier, url, width, height, scale)
}
```

``` kotlin
CompositionLocalProvider(LocalImageLoader provides customImageLoader) {
    RestaurantInfo(
        restaurantInfoData = RestaurantInfoData.dummy1()
    )
}
```

## 위치 권한 모듈 추가

```
git submodule add (or git clone) https://github.com/sarang628/restauarnt_info_di.git
```

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

## 

```
git clone https://github.com/sarang628/restauarnt_info_di.git

git clone https://github.com/sarang628/repository.git
```

```
implementation("com.github.sarang628:RestaurantInfo:62628e5b8e")
implementation("com.github.sarang628:CommonImageLoader:1999de5a48")
implementation("com.google.dagger:hilt-android:2.46")
kapt("com.google.dagger:hilt-android-compiler:2.46")
implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
implementation("com.github.sarang628:TorangRepository:e0d12661da")

implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

implementation("androidx.room:room-runtime:2.5.1")
annotationProcessor("androidx.room:room-compiler:2.5.1")
implementation("androidx.room:room-paging:2.5.1")
```

