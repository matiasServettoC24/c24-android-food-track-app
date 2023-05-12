package com.example.c24_android_food_track_app.ui.admin.adapters

import com.example.c24_android_food_track_app.R
import com.example.c24_android_food_track_app.domain.LoadingViewEntity
import com.example.c24_android_food_track_app.domain.ViewEntity
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate

fun loadingAdapterDelegate() =
    adapterDelegate<LoadingViewEntity, ViewEntity>(R.layout.admin_loading_item) {}