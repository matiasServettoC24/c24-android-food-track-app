package com.example.c24_android_food_track_app.domain.menu

import com.example.c24_android_food_track_app.domain.ViewEntity

data class TimeSlotViewEntity(
    val isAvailable: Boolean = true,
    val startTime: String,
    val endTime: String,
): ViewEntity