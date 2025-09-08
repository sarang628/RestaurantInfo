package com.sarang.torang

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantInfoViewModel @Inject constructor(
    private val getRestaurantInfoUseCase: GetRestaurantInfoUseCase
) :
    ViewModel() {
    val tag = "__RestaurantInfoViewModel"
    var uiState: RestaurantInfoUiState by mutableStateOf(RestaurantInfoUiState.Loading); private set
    var myLatitude : Double = 0.0
    var myLongitude : Double = 0.0
    var isRefresh : Boolean by mutableStateOf(false); private set

    suspend fun fetchRestaurantInfo1(restaurantId: Int) {
        try { uiState = RestaurantInfoUiState.Success(getRestaurantInfoUseCase.invoke(restaurantId)
            .copy(myLatitude = myLatitude, myLongitude = myLongitude))}
        catch (e : Exception){ Log.e(tag, "$e") }
    }

    fun refresh(restaurantId : Int){
        isRefresh = true
        viewModelScope.launch {
            fetchRestaurantInfo1(restaurantId)
            isRefresh = false
        }
    }

    fun setCurrentLocation(latitude: Double, longitude: Double) {
        Log.d(tag, "setCurrentLocation: ${latitude}, ${longitude}}")
        this.myLatitude = latitude
        this.myLongitude = longitude
        if(uiState is RestaurantInfoUiState.Success){
            uiState = RestaurantInfoUiState.Success((uiState as RestaurantInfoUiState.Success)
                .restaurantInfoData.copy(myLatitude = latitude, myLongitude = longitude))
        }
    }
}