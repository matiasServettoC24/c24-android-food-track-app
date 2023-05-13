package com.example.c24_android_food_track_app.ui.admin.adapters

import com.example.c24_android_food_track_app.R
import com.example.c24_android_food_track_app.databinding.AdminOrderItemBinding
import com.example.c24_android_food_track_app.domain.ViewEntity
import com.example.c24_android_food_track_app.data.models.FoodTrackOrder
import com.example.c24_android_food_track_app.data.models.Status
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate

fun orderAdapterDelegate(
    onOrderReadyCallback: (FoodTrackOrder) -> Unit,
    onOrderPickedUpCallback: (FoodTrackOrder) -> Unit,
) = adapterDelegate<FoodTrackOrder, ViewEntity>(R.layout.admin_order_item) {

    val binding = AdminOrderItemBinding.bind(itemView)

    bind {
        binding.orderTitle.text = item.title
        when (item.status) {
            Status.Ordered -> {
                binding.orderReadyButton.text = "READY"
                binding.orderReadyButton.isEnabled = true
                binding.orderReadyButton.setOnClickListener {
                    onOrderReadyCallback(item)
                }
            }
            Status.Ready -> {
                binding.orderReadyButton.text = "PICKED UP"
                binding.orderReadyButton.isEnabled = true
                binding.orderReadyButton.setOnClickListener {
                    onOrderPickedUpCallback(item)
                }
            }
            Status.Picked -> {
                binding.orderReadyButton.isEnabled = false
                binding.orderReadyButton.setOnClickListener(null)
            }
        }
    }
}