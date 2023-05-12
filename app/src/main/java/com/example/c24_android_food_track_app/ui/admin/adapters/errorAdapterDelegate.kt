package com.example.c24_android_food_track_app.ui.admin.adapters

import com.example.c24_android_food_track_app.R
import com.example.c24_android_food_track_app.domain.ViewEntity
import com.example.c24_android_food_track_app.domain.admin.ErrorViewEntity
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate

fun errorAdapterDelegate() =
    adapterDelegate<ErrorViewEntity, ViewEntity>(R.layout.admin_error_item) {}