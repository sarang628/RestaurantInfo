package com.sarang.torang

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.AssistChip
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sarang.torang.di.image.provideTorangAsyncImage
import com.sarang.torang.di.restauarnt_info_di.restaurantInfo
import com.sarang.torang.repository.FindRepository
import com.sryang.torang.ui.TorangTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var findRepository : FindRepository

    @OptIn(ExperimentalMaterial3Api::class)
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
                    val restaurants by findRepository.restaurants.collectAsStateWithLifecycle()
                    val scaffoldState = rememberBottomSheetScaffoldState()
                    val scope = rememberCoroutineScope()
                    var restaurantId by remember { mutableStateOf(0) }

                    Box{
                        BottomSheetScaffold(
                            scaffoldState = scaffoldState,
                            sheetPeekHeight = 0.dp,
                            sheetContent = {
                                LazyColumn(Modifier.fillMaxSize()) {
                                    items(restaurants.reversed()){
                                        TextButton({
                                            restaurantId = it.restaurant.restaurantId
                                            scope.launch {
                                                scaffoldState.bottomSheetState.partialExpand()
                                            }
                                        }) {
                                            Text(it.restaurant.restaurantName)
                                        }
                                    }
                                }
                            }
                        ){
                            CompositionLocalProvider(LocalRestaurantInfoImageLoader provides provideTorangAsyncImage(),
                                LocalRestaurantInfo provides restaurantInfo(viewmodel)) {
                                //RestaurantInfo_(restaurantId = restaurantId)
                                //RestaurantInfoWithPermissionWithLocation(restaurantId)
                                LocalRestaurantInfo.current.invoke(
                                    RestaurantInfoScreenData(
                                        restaurantId = restaurantId,
                                        onLocation = {}
                                    )
                                )
                            }
                        }
                        AssistChip(modifier = Modifier.align(Alignment.BottomCenter), onClick = { viewmodel.refresh(restaurantId) }, label = { Text("refresh") })

                        FloatingActionButton(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(bottom = 24.dp, end = 12.dp),
                            onClick = {
                                scope.launch {
                                    scope.launch {
                                        findRepository.findFilter()
                                    }
                                    scaffoldState.bottomSheetState.expand()
                                }
                            }) {
                            Icon(Icons.AutoMirrored.Default.List, null)
                        }
                    }
                }
            }
        }
    }
}
