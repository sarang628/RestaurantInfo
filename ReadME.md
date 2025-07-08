# 음식점 정보 화면 모듈

jitpack에서 모듈 다운을 위한 의존성 추가
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

## [이미지 모듈 추가](https://github.com/sarang628/CommonImageLoader?tab=readme-ov-file#%EC%9D%B4%EB%AF%B8%EC%A7%80-%EB%A1%9C%EB%93%9C-%EB%AA%A8%EB%93%88-%EC%B6%94%EA%B0%80)

자식 모듈에 내려 줄 인터페이스 부모 프로젝트에 추가하기
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

## 뷰모델 사용

## [Hilt 추가](https://github.com/sarang628/HiltTest?tab=readme-ov-file#for-torang)

### usecase 구현 코드 추가 

```
git submodule add (or git clone) https://github.com/sarang628/restauarnt_info_di.git
```

## [usecase에 필요한 저장소를 추가](https://github.com/sarang628/TorangRepository?tab=readme-ov-file#%EB%AA%A8%EB%93%88-%EC%B6%94%EA%B0%80%ED%95%98%EA%B8%B0)


## 음식점과의 거리 계산을 위한 위치 권한 기능 추가

```
implementation("com.google.accompanist:accompanist-permissions:0.32.0")
implementation("com.github.sarang628:ComposePermissionTest:5159bc3d34")
implementation("com.google.android.gms:play-services-location:21.1.0")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
```

```
CompositionLocalProvider(LocalImageLoader provides customImageLoader) {
    RestaurantInfoWithPermission(restaurantId = 234, viewModel = BestPracticeViewModel())
}
```