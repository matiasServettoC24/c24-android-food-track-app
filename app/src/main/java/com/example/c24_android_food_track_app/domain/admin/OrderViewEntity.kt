package com.example.c24_android_food_track_app.domain.admin

import com.example.c24_android_food_track_app.domain.ViewEntity

data class OrderViewEntity(
    val id: String,
    val title: String,
    val isReady: Boolean
): ViewEntity