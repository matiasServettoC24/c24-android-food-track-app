package com.example.c24_android_food_track_app.ui.admin.adapters

import com.example.c24_android_food_track_app.R
import com.example.c24_android_food_track_app.domain.ViewEntity
import com.example.c24_android_food_track_app.domain.admin.EmptyQueueViewEntity
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate

fun emptyQueueAdapterDelegate() =
    adapterDelegate<EmptyQueueViewEntity, ViewEntity>(R.layout.admin_empty_queue_item) {}