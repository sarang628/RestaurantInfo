package com.sarang.torang

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

typealias RestaurantInfoImageLoader = @Composable (RestaurantInfoImageLoaderData) -> Unit

data class RestaurantInfoImageLoaderData(
    val modifier        : Modifier      = Modifier,
    val url             : String        = "",
    val progressSize    : Dp            = 30.dp,
    val errorIconSize   : Dp            = 30.dp,
    val contentScale    : ContentScale  = ContentScale.Fit
)

val LocalRestaurantInfoImageLoader = compositionLocalOf<RestaurantInfoImageLoader> {
    // 기본 구현: 경고 로그 출력
    @Composable {
        Log.w("__RestaurantInfoImageLoader", "No RestaurantInfoImageLoader provided.")
    }
}