package com.example.c24_android_food_track_app.ui.queue.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.c24_android_food_track_app.domain.admin.OrderViewEntity

@Composable
fun QueueView(
    orders: List<OrderViewEntity>,
    onOrderReadyCallback: (OrderViewEntity) -> Unit,
    onOrderPickedUpCallback: (OrderViewEntity) -> Unit,
    deleteOrderCallback: (OrderViewEntity) -> Unit,
) = if (orders.isNotEmpty()) {
    LazyColumn {
        item { Text(text = "Orders:") }
        items(orders) { order ->
            Text(text = order.order.email)
        }
    }
} else {
    Column {
        Text(text = "Orders:")
        Text(text = "the queue is empty")
    }
}

@Preview(widthDp = 300, showBackground = true)
@Composable
fun QueueViewPreview() = QueueView(emptyList(), {}, {}, {})
