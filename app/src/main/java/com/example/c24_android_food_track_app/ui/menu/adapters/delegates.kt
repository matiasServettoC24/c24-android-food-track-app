package com.example.c24_android_food_track_app.ui.menu.adapters

import com.example.c24_android_food_track_app.R
import com.example.c24_android_food_track_app.databinding.MenuItemBinding
import com.example.c24_android_food_track_app.databinding.TimeslotItemBinding
import com.example.c24_android_food_track_app.domain.ViewEntity
import com.example.c24_android_food_track_app.domain.menu.MenuViewEntity
import com.example.c24_android_food_track_app.domain.menu.TimeSlotViewEntity
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate

fun menuDelegate(
    orderCallback: () -> Unit
) = adapterDelegate<MenuViewEntity, ViewEntity>(R.layout.menu_item) {

    val binding = MenuItemBinding.bind(itemView)

    bind {
        binding.dishTitle.text = item.dishTitle
        binding.dishType.text = item.dishType
    }

    binding.orderBtn.setOnClickListener { orderCallback() }
}


fun timeSlotDelegate(selectTimeSlotCallback: () -> Unit) = adapterDelegate<TimeSlotViewEntity, ViewEntity>(R.layout.menu_item) {

    val binding = TimeslotItemBinding.bind(itemView)

    bind {
        binding.startTime.text = item.startTime
        binding.endTime.text = item.endTime
    }

    binding.selectTimeSlotBtn.setOnClickListener { selectTimeSlotCallback() }
}