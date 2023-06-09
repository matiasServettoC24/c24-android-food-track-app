package com.example.c24_android_food_track_app.domain.admin

import com.example.c24_android_food_track_app.data.models.FoodTrackOrder
import com.example.c24_android_food_track_app.domain.ViewEntity

data class OrderViewEntity(
    val order: FoodTrackOrder
): ViewEntity