package com.example.c24_android_food_track_app.ui.queue.adapters

import com.example.c24_android_food_track_app.R
import com.example.c24_android_food_track_app.domain.ViewEntity
import com.example.c24_android_food_track_app.domain.admin.NonAuthAdminViewEntity
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate

fun nonAuthAdminAdapterDelegate() =
    adapterDelegate<NonAuthAdminViewEntity, ViewEntity>(R.layout.admin_non_auth_item) {}