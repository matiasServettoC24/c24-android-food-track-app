package com.example.c24_android_food_track_app.ui.admin.adapters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.example.c24_android_food_track_app.R
import com.example.c24_android_food_track_app.data.models.Status
import com.example.c24_android_food_track_app.databinding.AdminOrderItemComposeBinding
import com.example.c24_android_food_track_app.domain.ViewEntity
import com.example.c24_android_food_track_app.domain.admin.OrderViewEntity
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate

fun orderAdapterDelegate(
    onOrderReadyCallback: (OrderViewEntity) -> Unit,
    onOrderPickedUpCallback: (OrderViewEntity) -> Unit,
    deleteOrderCallback: (OrderViewEntity) -> Unit,
) = adapterDelegate<OrderViewEntity, ViewEntity>(R.layout.admin_order_item_compose) {

    val binding = AdminOrderItemComposeBinding.bind(itemView)
    binding.root.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

    bind {
        binding.root.setContent {
            MaterialTheme {
                Row {
                    Column {
                        Text(item.order.email)
                        Text(item.order.title)
                        item.order.slotTime?.let { Text(it) }
                    }
                    when (item.order.status) {
                        Status.Ordered -> {
                            Button(onClick = { onOrderReadyCallback(item) }) {
                                Text("READY")
                            }
                        }
                        Status.Ready -> {
                            Button(onClick = { onOrderPickedUpCallback(item) }) {
                                Text("PICKED UP")
                            }
                        }
                        Status.Picked -> {
                            Button(onClick = { deleteOrderCallback(item) }) {
                                Text("DELETE")
                            }
                        }
                    }

                }
            }
        }
    }
}