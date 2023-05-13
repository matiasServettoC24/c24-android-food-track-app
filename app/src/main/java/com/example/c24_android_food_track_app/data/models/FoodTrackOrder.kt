package com.example.c24_android_food_track_app.data.models

import com.example.c24_android_food_track_app.domain.ViewEntity

data class FoodTrackOrder(
    val id: String,
    val title: String,
    val status: Status,
    val email: String,
    val slot: String,
    val timeStart: String,
    val slotTime: String?,
)