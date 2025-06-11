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