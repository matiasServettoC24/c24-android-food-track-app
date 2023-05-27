package com.example.c24_android_food_track_app.ui.admin.adapters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import com.example.c24_android_food_track_app.R
import com.example.c24_android_food_track_app.data.models.FoodTrackOrder
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
            ItemViewContent(
                item,
                onOrderReadyCallback,
                onOrderPickedUpCallback,
                deleteOrderCallback
            )
        }
    }
}

@Composable
private fun ItemViewContent(
    item: OrderViewEntity,
    onOrderReadyCallback: (OrderViewEntity) -> Unit,
    onOrderPickedUpCallback: (OrderViewEntity) -> Unit,
    deleteOrderCallback: (OrderViewEntity) -> Unit,
) = MaterialTheme {
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

private const val previewWidthDp = 300

private val previewFoodTrackOrder = FoodTrackOrder(
    id = "id",
    title = "Pizza Margarita",
    status = Status.Ordered,
    email = "matias.servetto@check24.de",
    slot = "10-12",
    timeStart = "12:00",
    slotTime = "10:35-10:45"
)

@Preview(widthDp = previewWidthDp, showBackground = true)
@Composable
private fun StatusOrderedPreview() = ItemViewContent(
    OrderViewEntity(previewFoodTrackOrder.copy(status = Status.Ordered)), {}, {}, {})

@Preview(widthDp = previewWidthDp, showBackground = true)
@Composable
private fun StatusReadyPreview() = ItemViewContent(
    OrderViewEntity(previewFoodTrackOrder.copy(status = Status.Ready)), {}, {}, {})

@Preview(widthDp = previewWidthDp, showBackground = true)
@Composable
private fun StatusPickedPreview() = ItemViewContent(
    OrderViewEntity(previewFoodTrackOrder.copy(status = Status.Picked)), {}, {}, {})