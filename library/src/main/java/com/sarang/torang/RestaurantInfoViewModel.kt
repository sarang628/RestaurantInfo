package com.sarang.torang

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RestaurantInfoViewModel @Inject constructor(
    private val getRestaurantInfoUseCase: GetRestaurantInfoUseCase
) :
    ViewModel() {
    val tag = "__RestaurantInfoViewModel"
    var uiState: RestaurantInfoUiState by mutableStateOf(RestaurantInfoUiState.Loading); private set
    val latitude : Double by mutableDoubleStateOf(0.0)
    val longitude : Double by mutableDoubleStateOf(0.0)

    suspend fun fetchRestaurantInfo1(restaurantId: Int) {
        try { uiState = RestaurantInfoUiState.Success(getRestaurantInfoUseCase.invoke(restaurantId)) }
        catch (e : Exception){ Log.e(tag, "$e") }
    }

    fun setCurrentLocation(latitude: Double, longitude: Double) {
        Log.d(tag, "setCurrentLocation: ${latitude}, ${longitude}}")
        //uiState = uiState.copy(myLatitude = latitude, myLongitude = longitude)
    }
}