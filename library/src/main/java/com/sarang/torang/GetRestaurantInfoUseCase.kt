package com.sarang.torang

interface GetRestaurantInfoUseCase {
    suspend fun invoke(restaurantId: Int) : RestaurantInfoData
}