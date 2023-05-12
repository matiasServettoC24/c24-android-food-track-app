package com.example.c24_android_food_track_app.domain.menu

import com.example.c24_android_food_track_app.domain.ViewEntity

data class TimeSlotViewEntity(
    val timeStart: String,
    val timeEnd: String,
    val remainingOrders: String = "5",

    val slotId: String,
): ViewEntity