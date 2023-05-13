package com.example.c24_android_food_track_app.data.models

import com.example.c24_android_food_track_app.domain.ViewEntity

data class TimeSlot(
    val timeStart: String,
    val timeEnd: String,
    val remainingOrders: String = "5",

    val slotId: String,
): ViewEntity