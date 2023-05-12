package com.example.c24_android_food_track_app.ui.menu.adapters

import com.example.c24_android_food_track_app.R
import com.example.c24_android_food_track_app.domain.ViewEntity
import com.example.c24_android_food_track_app.domain.menu.MenuViewEntity
import com.example.c24_android_food_track_app.domain.menu.TimeSlotViewEntity
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate

fun menuDelegate() =
    adapterDelegate<MenuViewEntity, ViewEntity>(R.layout.menu_item) {}


fun timeSlotDelegate() =
    adapterDelegate<TimeSlotViewEntity, ViewEntity>(R.layout.menu_item) {}