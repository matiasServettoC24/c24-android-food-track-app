package com.example.c24_android_food_track_app.util

import com.example.c24_android_food_track_app.data.models.FoodTrackOrder
import com.example.c24_android_food_track_app.data.models.Status

const val PREVIEW_WIDTH_DP = 300
const val PREVIEW_HEIGHT_DP = 420

fun createPreviewFoodTrackOrder(status: Status) = FoodTrackOrder(
    id = "id",
    title = "Pizza Margarita",
    status = status,
    email = "matias.servetto@check24.de",
    slot = "10-12",
    timeStart = "12:00",
    slotTime = "10:35-10:45"
)
