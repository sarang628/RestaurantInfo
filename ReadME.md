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

뷰모델 없는 순수 UI 사용
```kotlin
RestaurantInfo(restaurantInfoData = RestaurantInfoData.dummy1())
```


## 이미지 로드 모듈 추가

이미지 로드 모듈 단일 버전 관리를 위해 루트 프로젝트에서 구현하고 하위 프로젝트들은 인터페이스만 받음.
```
cd app/src/main/java/[package]/di
git submodule add (or git clone) https://github.com/sarang628/image.git
```
이미지 로드 모듈에 줌 처리 기능이 추가되 추가 모듈 설정 필요.
```
cd app/src/main/java/[package]/di
git submodule add (or git clone) https://github.com/sarang628/pinchzoom.git
```
이미지 로드 모듈 다운로드
```
implementation("com.github.sarang628:CommonImageLoader:1999de5a48") 
```

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

### API 호출 저장소 추가
```
implementation("com.github.sarang628:TorangRepository:e0d12661da")

implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

implementation("androidx.room:room-runtime:2.5.1")
annotationProcessor("androidx.room:room-compiler:2.5.1")
implementation("androidx.room:room-paging:2.5.1")
```


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