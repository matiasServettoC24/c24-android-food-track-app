package com.example.c24_android_food_track_app.ui.admin.adapters

import android.widget.TextView
import com.example.c24_android_food_track_app.R
import com.example.c24_android_food_track_app.domain.ViewEntity
import com.example.c24_android_food_track_app.domain.admin.OrdersTitleViewEntity
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate

fun ordersTitleAdapterDelegate() =
    adapterDelegate<OrdersTitleViewEntity, ViewEntity>(R.layout.admin_title_item) {
        itemView.findViewById<TextView>(R.id.title).text = "Orders:"
    }