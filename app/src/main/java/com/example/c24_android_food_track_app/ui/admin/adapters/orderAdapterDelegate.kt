package com.example.c24_android_food_track_app.ui.admin.adapters

import com.example.c24_android_food_track_app.R
import com.example.c24_android_food_track_app.databinding.AdminOrderItemBinding
import com.example.c24_android_food_track_app.domain.ViewEntity
import com.example.c24_android_food_track_app.domain.admin.OrderViewEntity
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate

fun orderAdapterDelegate(
    onOrderReadyCallback: (id: String) -> Unit
) = adapterDelegate<OrderViewEntity, ViewEntity>(R.layout.admin_order_item) {

    val binding = AdminOrderItemBinding.bind(itemView)

    bind {
        binding.orderTitle.text = item.title
        if (item.isReady.not()) {
            binding.orderReadyButton.isEnabled = true
            binding.orderReadyButton.setOnClickListener {
                onOrderReadyCallback(item.id)
            }
        } else {
            binding.orderReadyButton.isEnabled = false
            binding.orderReadyButton.setOnClickListener(null)
        }
    }
}