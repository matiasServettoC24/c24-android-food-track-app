package com.example.c24_android_food_track_app.ui.queue.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.c24_android_food_track_app.data.models.FoodTrackOrder
import com.example.c24_android_food_track_app.data.models.Status
import com.example.c24_android_food_track_app.domain.admin.OrderViewEntity
import com.example.c24_android_food_track_app.util.PREVIEW_WIDTH_DP


@Composable
fun OrderView(
    item: OrderViewEntity,
    onOrderReadyCallback: (OrderViewEntity) -> Unit,
    onOrderPickedUpCallback: (OrderViewEntity) -> Unit,
    deleteOrderCallback: (OrderViewEntity) -> Unit,
) = MaterialTheme {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row {
            OrderDetailsView(Modifier.weight(1f), item)
            OrderActionView(
                Modifier.align(Alignment.CenterVertically),
                item,
                onOrderReadyCallback,
                onOrderPickedUpCallback,
                deleteOrderCallback
            )
        }
    }
}

@Composable
private fun OrderActionView(
    modifier: Modifier,
    item: OrderViewEntity,
    onOrderReadyCallback: (OrderViewEntity) -> Unit,
    onOrderPickedUpCallback: (OrderViewEntity) -> Unit,
    deleteOrderCallback: (OrderViewEntity) -> Unit
) {
    when (item.order.status) {
        Status.Ordered -> ButtonView(modifier, text = "READY") {
            onOrderReadyCallback(item)
        }
        Status.Ready -> ButtonView(modifier, text = "PICKED UP") {
            onOrderPickedUpCallback(item)
        }
        Status.Picked -> ButtonView(modifier, text = "DELETE") {
            deleteOrderCallback(item)
        }
    }
}

@Composable
private fun OrderDetailsView(
    modifier: Modifier,
    item: OrderViewEntity
) = Column(modifier.padding(start = 8.dp, top = 4.dp, bottom = 4.dp)) {
    Text(item.order.email, maxLines = 1, overflow = TextOverflow.Ellipsis)
    Text(item.order.title)
    item.order.slotTime?.let { Text(it) }
}

@Composable
private fun ButtonView(
    modifier: Modifier,
    text: String,
    onClick: () -> Unit
) = Button(onClick, modifier.padding(horizontal = 8.dp)) { Text(text) }

@Preview(widthDp = PREVIEW_WIDTH_DP, showBackground = true)
@Composable
private fun StatusOrderedPreview() =
    OrderView(OrderViewEntity(createPreviewFoodTrackOrder(status = Status.Ordered)), {}, {}, {})

@Preview(widthDp = PREVIEW_WIDTH_DP, showBackground = true)
@Composable
private fun StatusReadyPreview() =
    OrderView(OrderViewEntity(createPreviewFoodTrackOrder(status = Status.Ready)), {}, {}, {})

@Preview(widthDp = PREVIEW_WIDTH_DP, showBackground = true)
@Composable
private fun StatusPickedPreview() =
    OrderView(OrderViewEntity(createPreviewFoodTrackOrder(status = Status.Picked)), {}, {}, {})

private fun createPreviewFoodTrackOrder(status: Status) = FoodTrackOrder(
    id = "id",
    title = "Pizza Margarita",
    status = status,
    email = "matias.servetto@check24.de",
    slot = "10-12",
    timeStart = "12:00",
    slotTime = "10:35-10:45"
)
