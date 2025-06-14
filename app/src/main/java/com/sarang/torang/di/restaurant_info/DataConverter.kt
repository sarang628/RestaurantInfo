package com.sarang.torang.di.restaurant_info

import com.sarang.torang.BuildConfig
import com.sarang.torang.HoursOfOperation
import com.sarang.torang.RestaurantInfoData
import com.sarang.torang.data.RestaurantDetail

fun RestaurantDetail.toRestaurantInfoData(): RestaurantInfoData {
    return RestaurantInfoData(
        foodType = this.restaurant.restaurantType,
        open = "영업 중",
        close = "오후 9:00에 영업 종료",
        address = this.restaurant.address,
        webSite = this.restaurant.website,
        tel = this.restaurant.tel,
        name = this.restaurant.restaurantName,
        imageUrl = BuildConfig.RESTAURANT_IMAGE_SERVER_URL + this.restaurant.imgUrl1,
        hoursOfOperation = this.hoursOfOperations.map { it.toHoursOfOperation() },
        rating = this.restaurant.rating,
        reviewCount = this.restaurant.reviewCount,
        price = this.restaurant.prices,
        lon = this.restaurant.lon,
        lat = this.restaurant.lat
    )
}

fun com.sarang.torang.data.HoursOfOperation.toHoursOfOperation(): HoursOfOperation {
    return HoursOfOperation(
        day = this.day,
        startTime = this.start_time,
        endTime = this.end_time
    )
}