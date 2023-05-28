package com.example.c24_android_food_track_app.ui.queue.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.c24_android_food_track_app.domain.ViewEntity
import com.example.c24_android_food_track_app.domain.admin.OrderViewEntity

@Composable
fun QueueView(
    orders: List<ViewEntity>,
    onOrderReadyCallback: (OrderViewEntity) -> Unit,
    onOrderPickedUpCallback: (OrderViewEntity) -> Unit,
    deleteOrderCallback: (OrderViewEntity) -> Unit,
    ) = Text(text = "hello queue!")

@Preview(widthDp = 300, showBackground = true)
@Composable
fun QueueViewPreview() = QueueView(emptyList(), {}, {}, {})
