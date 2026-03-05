package com.sarang.torang

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sarang.torang.di.image.TorangAsyncImageData
import com.sarang.torang.di.image.provideTorangAsyncImage
import com.sarang.torang.di.restauarnt_info_di.restaurantInfo
import com.sarang.torang.repository.FindRepository
import com.sryang.torang.ui.TorangTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var findRepository : FindRepository

    private val tag = "__MainActivity"

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TorangTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val viewmodel : RestaurantInfoViewModel = hiltViewModel()
                    val navController = rememberNavController()

                    NavHost(navController = navController,
                            startDestination = "Menu"){
                        composable("Menu"){
                            Menu(navController = navController)
                        }

                        composable("RestaurantPhotoRow"){
                            TestContainer(findRepository = findRepository, content = {
                                RestaurantPhotoRowTest(viewmodel = viewmodel)
                            }){
                                Log.d(tag,"onRefresh")
                                viewmodel.refresh(it)
                            }
                        }

                        composable("RestaurantInfo"){
                            TestContainer(findRepository = findRepository,{
                                RestaurantInfoTest(it, viewmodel)
                            }){
                                viewmodel.refresh(it)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun Menu(navController : NavHostController = rememberNavController()){
        Column {
            TextButton({navController.navigate("RestaurantPhotoRow")}) {
                Text("RestaurantPhotoRow")
            }
            TextButton({navController.navigate("RestaurantInfo")}) {
                Text("RestaurantInfo")
            }
        }
    }

    @Composable
    fun RestaurantInfoTest(restaurantId : Int = 0,
                           viewmodel : RestaurantInfoViewModel = hiltViewModel()){
        CompositionLocalProvider(LocalRestaurantInfoImageLoader provides {
            provideTorangAsyncImage().invoke(
                TorangAsyncImageData(modifier        = it.modifier,
                    model           = it.url,
                    progressSize    = it.progressSize,
                    errorIconSize   = it.errorIconSize,
                    contentScale    = it.contentScale)
            ) },
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

    @Composable
    fun RestaurantPhotoRowTest(viewmodel : RestaurantInfoViewModel = hiltViewModel()){
        when(val state = viewmodel.uiState){
            is RestaurantInfoUiState.Success -> {
                CompositionLocalProvider(LocalRestaurantInfoImageLoader provides {
                    provideTorangAsyncImage().invoke(
                        TorangAsyncImageData(modifier        = it.modifier,
                            model           = it.url,
                            progressSize    = it.progressSize,
                            errorIconSize   = it.errorIconSize,
                            contentScale    = it.contentScale)
                    ) }){
                    RestaurantPhotoRow(modifier = Modifier.height(300.dp),
                        list = state.restaurantInfoData.photoRowData)
                }
            }

            else -> {
                Text("Loading")
            }
        }
    }

    @Preview
    @Composable
    fun PreviewAddress(){
        TorangTheme {
            Address(
                address = "abcd",
                onLocation = {})
        }
    }
}
