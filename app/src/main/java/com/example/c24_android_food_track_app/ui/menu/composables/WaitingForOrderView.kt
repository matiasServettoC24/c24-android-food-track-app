package com.example.c24_android_food_track_app.ui.menu.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.c24_android_food_track_app.data.models.FoodTrackOrder
import com.example.c24_android_food_track_app.data.models.Status
import com.example.c24_android_food_track_app.util.PREVIEW_HEIGHT_DP
import com.example.c24_android_food_track_app.util.PREVIEW_WIDTH_DP
import com.example.c24_android_food_track_app.util.createPreviewFoodTrackOrder

@Composable
fun WaitingForOrderView(order: FoodTrackOrder) = Column(
    modifier = Modifier
        .fillMaxSize()
        .background(Color(241, 216, 118))
        .padding(horizontal = 32.dp),
    verticalArrangement = Arrangement.Center
) {
    Text(
        text = "Your order is being prepared!",
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    )
    Text(
        text = "Preparing your order ${order.title}",
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    )
    Text(
        text = "Your queue time: ${order.slotTime}",
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        textAlign = TextAlign.Center,
        fontSize = 20.sp
    )
    Text(
        text = "You will receive a notification once it is ready.",
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        fontStyle = FontStyle.Italic,
        fontSize = 22.sp
    )
}


@Preview(widthDp = PREVIEW_WIDTH_DP, heightDp = PREVIEW_HEIGHT_DP, showBackground = true)
@Composable
fun WaitingForOderViewPreview() =
    WaitingForOrderView(createPreviewFoodTrackOrder(status = Status.Ordered))
