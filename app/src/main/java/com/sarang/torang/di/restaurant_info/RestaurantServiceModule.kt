package com.sarang.torang.di.restaurant_info

import com.sarang.torang.GetRestaurantInfoUseCase
import com.sarang.torang.RestaurantInfoData
import com.sarang.torang.api.ApiRestaurant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class RestaurantServiceModule {
    @Provides
    fun providesGetRestaurantInfoUseCase(apiRestaurant: ApiRestaurant): GetRestaurantInfoUseCase {
        return object : GetRestaurantInfoUseCase {
            override suspend fun invoke(restaurantId: Int): RestaurantInfoData {
                return apiRestaurant.getRestaurantDetail(restaurantId).toRestaurantInfoData()
            }
        }
    }
}