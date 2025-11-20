package com.sarang.torang

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sarang.torang.restaurant.defaultShimmerBrush
import androidx.core.net.toUri

/**
 * ## 음식점 정보
 * - 음식점 상호, 평점, 음식 종류, 거리, 가격, 주소, 웹사이트, 운영시간, 전화번호
 * - 권한과 위치 요청에 관한 기능은 제공하지 않음.
 */
@Composable
fun RestaurantInfoScreen(
    modifier                    : Modifier                  = Modifier,
    restaurantId                : Int,
    viewModel                   : RestaurantInfoViewModel   = hiltViewModel(),
    tag                         : String                    = "__RestaurantInfo",
    currentLocation             : Pair<Double,Double>?      = null,
    progressTintColor           : Color?                    = null,
    isLocationPermissionGranted : Boolean                   = false,
    onLocation                  : () -> Unit                = { Log.w(tag, "onLocation doesn't set") },
    onRequestPermission         : () -> Unit                = { Log.w(tag, "onRequestPermission doesn't set") },
) {
    val context = LocalContext.current
    LaunchedEffect(restaurantId) { viewModel.fetchRestaurantInfo1(restaurantId) }

    LaunchedEffect(currentLocation) {
        currentLocation?.let { viewModel.setCurrentLocation(it.first, it.second) }
    }

    RestaurantInfo(
        modifier = modifier,
        uiState = viewModel.uiState,
        onLocation = onLocation,
        progressTintColor = progressTintColor,
        onRequestPermission = onRequestPermission,
        isLocationPermissionGranted = isLocationPermissionGranted
    )
}

@Composable
fun RestaurantInfo(
    modifier                    : Modifier              = Modifier,
    tag                         : String                = "__RestaurantInfo",
    uiState                     : RestaurantInfoUiState = RestaurantInfoUiState.Loading,
    progressTintColor           : Color?                = null,
    isLocationPermissionGranted : Boolean               = false,
    onLocation                  : () -> Unit            = { Log.w(tag, "onLocation doesn't set") },
    onRequestPermission         : () -> Unit            = { Log.w(tag, "onRequestPermission doesn't set") },
) {
    val context = LocalContext.current
    //@formatter:off
    when(uiState){
        RestaurantInfoUiState.Loading -> {
            Shimmer()
        }
        is RestaurantInfoUiState.Success -> {
            uiState.restaurantInfoData.let {
                Column(modifier = modifier) {
                    Box (modifier = Modifier.fillMaxWidth().height(300.dp)){ // 음식점명 + 평점 박스
                        if(it.imageUrl.isNotEmpty())
                            LocalRestaurantInfoImageLoader.current.invoke(Modifier.fillMaxSize(), it.imageUrl, null, null, ContentScale.Crop)
                        RestaurantTitleAndRating  (modifier = Modifier.align(Alignment.BottomEnd), restaurantName = it.name, rating = it.rating, reviewCount = it.reviewCount, progressTintColor = progressTintColor)
                    }
                    Row { // 음식점 종류, 거리, 가격
                        IconButton({}){ Icon  (modifier = Modifier.size(50.dp).padding(10.dp), painter = painterResource(id = R.drawable.ic_info), contentDescription = "") }
                        Text  (modifier = Modifier.align(Alignment.CenterVertically).clickable(onClick = onRequestPermission),
                            text = "${it.foodType} • ${if(isLocationPermissionGranted) it.distance else "(위치 권한 필요.)"} • ${it.price}")
                    }
                    HorizontalDivider()
                    Row { // 주소
                        IconButton (onClick = onLocation) { Icon(painter = painterResource(id = R.drawable.ic_loc), contentDescription = "", modifier = Modifier.size(21.dp)) }
                        Text (modifier = Modifier.align(Alignment.CenterVertically).padding(top = 5.dp, bottom = 5.dp).clickable { onLocation.invoke() }, text = it.address)
                    }
                    HorizontalDivider()
                    Row { // 웹사이트
                        IconButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, it.webSite.toUri())
                                context.startActivity(intent)
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_web),
                                contentDescription = null,
                                modifier = Modifier.size(21.dp)
                            )
                        }
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .clickable {
                                    val intent = Intent(Intent.ACTION_VIEW, it.webSite.toUri())
                                    context.startActivity(intent)
                                           },
                            text = it.webSite
                        )
                    }
                    if(!it.isEmptyOperation())
                    {
                        HorizontalDivider()
                        Row { // 운영시간
                            IconButton({}) {
                                Icon (painter = painterResource(id = R.drawable.ic_time), contentDescription = "")
                            }
                            Row(Modifier.align(Alignment.CenterVertically)) {
                                Text (modifier = Modifier.padding(vertical = 8.dp), text = it.toDayOfOperation())
                                Spacer(Modifier.width(8.dp))
                                Text (modifier = Modifier.padding(vertical = 8.dp), text = it.toHoursOfOperation())
                            }
                        }
                    }
                    HorizontalDivider()
                    Row { // 전화번호
                        IconButton (onClick = {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = "tel:${it.tel}".toUri()
                            }
                            context.startActivity(intent)
                        }) { Icon(modifier = Modifier.size(21.dp), painter = painterResource(id = R.drawable.ic_phone), contentDescription = "") }
                        Text (modifier = Modifier.align(Alignment.CenterVertically).padding(top = 8.dp, bottom = 8.dp, end = 8.dp).clickable {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = "tel:${it.tel}".toUri()
                            }
                            context.startActivity(intent)
                        }, text = it.tel)
                    }
                    HorizontalDivider()
                    //@formatter:on
                }
            }
        }
    }
}


@Composable
fun RestaurantTitleAndRating(
    modifier: Modifier = Modifier,
    restaurantName: String = "",
    rating: Float = 0f,
    reviewCount: Int = 0,
    progressTintColor: Color? = null
) {
    Box(modifier
        .padding(8.dp)
        .clip(RoundedCornerShape(8.dp))) {
        Column(Modifier
            .background(Color(0x55000000))
            .padding(8.dp), horizontalAlignment = Alignment.End) {
            Text(text = restaurantName, maxLines = 1, fontSize = 25.sp, color = Color.White, fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = rating.toString(), fontSize = 25.sp, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(5.dp))
                AndroidViewRatingBar(rating = rating, progressTintColor = progressTintColor, changable = false, isSmall = true)
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "(${reviewCount})", color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRestaurantTitleAnd() {
    RestaurantTitleAndRating()
}

@Preview(showBackground = true)
@Composable
fun PreviewRestaurantInfo1() {
    val restaurantInfoData = RestaurantInfoData(
//        foodType = "fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfoodfastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfoodfastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfoodfastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfoodfastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfoodfastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfoodfastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfoodfastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfoodfastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfoodfastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfoodfastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfoodfastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood fastfood",
        foodType = "fastfood",
//        distance = "100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m 100m",
//        open = "영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중영업 중",
//        close = "오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료오후 9:00에 영업 종료",
//        address = "서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000 서울특별시 강남구 삼성동 삼성로 3000",
        address = "삼성로 3000",
//        webSite = "https://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.koreahttps://torang.co.korea",
        webSite = "https://torang.co.korea",
//        tel = "02-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-567802-1234-5678",
        tel = "02-1234-5678",
        hoursOfOperation = ArrayList<HoursOfOperation>().apply {
            add(HoursOfOperation("mon", "10:00", "22:00"))
            add(HoursOfOperation("tue", "10:00", "22:00"))
            add(HoursOfOperation("wed", "10:00", "22:00"))
            add(HoursOfOperation("thu", "10:00", "22:00"))
            add(HoursOfOperation("fri", "10:00", "22:00"))
            add(HoursOfOperation("sat", "10:00", "22:00"))
            add(HoursOfOperation("sun", "10:00", "22:00"))
        },
//        price = "$$$$$ aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
        rating = 4.5f,
        reviewCount = 100,
        imageUrl = "",
//        name = "맥도날드맥도날드맥도날드맥도날드맥도날드맥도날드맥도날드맥도날드맥도날드맥도날드맥도날드맥도날드맥도날드맥도날드맥도날드맥도날드맥도날드"
    )
    RestaurantInfo(/*Preview*/
        modifier = Modifier.verticalScroll(rememberScrollState()),
    )
}

typealias RestaurantInfo = @Composable (RestaurantInfoScreenData) -> Unit

data class RestaurantInfoScreenData(
    val restaurantId    : Int,
    val onLocation      : () -> Unit
)

val LocalRestaurantInfo = compositionLocalOf<RestaurantInfo> {
    // 기본 구현: 경고 로그 출력
    @Composable {
        Log.w("__LocalRestaurantInfo", "No LocalRestaurantInfo provided.")
    }
}

@Preview
@Composable
fun Shimmer(){
    Column {
        Box(Modifier
            .height(300.dp)
            .fillMaxWidth()
            .background(defaultShimmerBrush()))
        Spacer(Modifier.height(1.dp))
        Box(Modifier
            .height(60.dp)
            .fillMaxWidth()
            .background(defaultShimmerBrush()))
        Spacer(Modifier.height(1.dp))
        Box(Modifier
            .height(60.dp)
            .fillMaxWidth()
            .background(defaultShimmerBrush()))
        Spacer(Modifier.height(1.dp))
        Box(Modifier
            .height(250.dp)
            .fillMaxWidth()
            .background(defaultShimmerBrush()))
        Spacer(Modifier.height(1.dp))
        Box(Modifier
            .height(60.dp)
            .fillMaxWidth()
            .background(defaultShimmerBrush()))
        Spacer(Modifier.height(1.dp))
        Box(Modifier
            .height(60.dp)
            .fillMaxWidth()
            .background(defaultShimmerBrush()))
        Spacer(Modifier.height(1.dp))
        Box(Modifier
            .height(60.dp)
            .fillMaxWidth()
            .background(defaultShimmerBrush()))
        Spacer(Modifier.height(10.dp))
        Box(Modifier
            .height(60.dp)
            .fillMaxWidth()
            .background(defaultShimmerBrush()))
    }
}