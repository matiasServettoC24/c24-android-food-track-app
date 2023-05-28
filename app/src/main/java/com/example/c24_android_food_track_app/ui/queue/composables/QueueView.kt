package com.example.c24_android_food_track_app.ui.queue.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.c24_android_food_track_app.data.models.FoodTrackOrder
import com.example.c24_android_food_track_app.data.models.Status
import com.example.c24_android_food_track_app.domain.admin.OrderViewEntity
import com.example.c24_android_food_track_app.util.PREVIEW_WIDTH_DP

@Composable
fun QueueView(
    orders: List<OrderViewEntity>,
    onOrderReadyCallback: (OrderViewEntity) -> Unit,
    onOrderPickedUpCallback: (OrderViewEntity) -> Unit,
    deleteOrderCallback: (OrderViewEntity) -> Unit,
) = if (orders.isNotEmpty()) {
    LazyColumn {
        item { QueueTitleView() }
        items(orders) { order ->
            OrderView(order, onOrderReadyCallback, onOrderPickedUpCallback, deleteOrderCallback)
        }
    }
} else {
    Column {
        QueueTitleView()
        EmptyQueueMessage()
    }
}

@Composable
private fun QueueTitleView() = Text(
    text = "Orders:",
    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
    fontSize = 20.sp,
    fontWeight = FontWeight.Bold
)

@Composable
private fun EmptyQueueMessage() = Text(
    text = "the queue is empty",
    modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
    fontStyle = FontStyle.Italic,
    textAlign = TextAlign.Center
)

@Preview(widthDp = PREVIEW_WIDTH_DP, showBackground = true)
@Composable
fun EmptyQueueViewPreview() = QueueView(emptyList(), {}, {}, {})

@Preview(widthDp = PREVIEW_WIDTH_DP, showBackground = true)
@Composable
fun QueueViewPreview() = QueueView(listOf(
    OrderViewEntity(createPreviewFoodTrackOrder(status = Status.Ordered)),
    OrderViewEntity(createPreviewFoodTrackOrder(status = Status.Ready)),
    OrderViewEntity(createPreviewFoodTrackOrder(status = Status.Picked)),
), {}, {}, {})

private fun createPreviewFoodTrackOrder(status: Status) = FoodTrackOrder(
    id = "id",
    title = "Pizza Margarita",
    status = status,
    email = "matias.servetto@check24.de",
    slot = "10-12",
    timeStart = "12:00",
    slotTime = "10:35-10:45"
)