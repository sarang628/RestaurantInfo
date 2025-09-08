package com.sarang.torang

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.sarang.torang.di.image3.customImageLoader
import com.sarang.torang.di.restauarnt_info_di.restaurantInfo
import com.sryang.torang.ui.TorangTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TorangTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {

                    }

                    val viewmodel : RestaurantInfoViewModel = hiltViewModel()

                    Box{
                        CompositionLocalProvider(LocalRestaurantInfoImageLoader provides customImageLoader,
                            LocalRestaurantInfo provides restaurantInfo(viewmodel)) {
                            //RestaurantInfo_(restaurantId = 234)
                            //RestaurantInfoWithPermissionWithLocation(234)
                            LocalRestaurantInfo.current.invoke(234, {}, {}, {})
                        }

                        AssistChip(modifier = Modifier.align(Alignment.BottomCenter), onClick = { viewmodel.refresh(234) }, label = { Text("refresh") })
                    }
                }
            }
        }
    }
}
