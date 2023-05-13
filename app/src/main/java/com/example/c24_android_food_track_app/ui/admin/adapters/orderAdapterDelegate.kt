package com.example.c24_android_food_track_app.ui.admin.adapters

import androidx.core.view.isVisible
import com.example.c24_android_food_track_app.R
import com.example.c24_android_food_track_app.databinding.AdminOrderItemBinding
import com.example.c24_android_food_track_app.domain.ViewEntity
import com.example.c24_android_food_track_app.data.models.Status
import com.example.c24_android_food_track_app.domain.admin.OrderViewEntity
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate

fun orderAdapterDelegate(
    onOrderReadyCallback: (OrderViewEntity) -> Unit,
    onOrderPickedUpCallback: (OrderViewEntity) -> Unit,
    deleteOrderCallback: (OrderViewEntity) -> Unit,
) = adapterDelegate<OrderViewEntity, ViewEntity>(R.layout.admin_order_item) {

    val binding = AdminOrderItemBinding.bind(itemView)

    bind {
        binding.email.text = item.order.email
        binding.orderTitle.text = item.order.title
        binding.orderTime.text = item.order.slotTime
        when (item.order.status) {
            Status.Ordered -> {
                binding.orderReadyButton.text = "READY"
                binding.orderReadyButton.setOnClickListener {
                    onOrderReadyCallback(item)
                }
            }
            Status.Ready -> {
                binding.orderReadyButton.text = "PICKED UP"
                binding.orderReadyButton.setOnClickListener {
                    onOrderPickedUpCallback(item)
                }
            }
            Status.Picked -> {
                binding.orderReadyButton.setOnClickListener(null)
            }
        }
        binding.orderReadyButton.isVisible = item.order.status != Status.Picked
        binding.orderDelete.isVisible = item.order.status == Status.Picked
        binding.orderDelete.setOnClickListener {
            deleteOrderCallback(item)
        }
    }
}