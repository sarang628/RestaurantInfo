# 음식점 정보 화면 모듈

```kotlin
RestaurantInfo(restaurantInfoData = RestaurantInfoData.dummy())
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