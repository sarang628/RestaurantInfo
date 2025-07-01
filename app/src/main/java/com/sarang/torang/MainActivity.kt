package com.sarang.torang

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.sarang.torang.di.image3.customImageLoader
import com.sarang.torang.di.restaurant_info.restaurantInfo
import com.sryang.torang.ui.TorangTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TorangTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CompositionLocalProvider(LocalRestaurantInfoImageLoader provides customImageLoader, LocalRestaurantInfo provides restaurantInfo)
                    {
                        //RestaurantInfo_(restaurantId = 234)
                        //RestaurantInfoWithPermissionWithLocation(234)
                        LocalRestaurantInfo.current.invoke(234, {}, {}, {})
                    }
                }
            }
        }
    }
}
