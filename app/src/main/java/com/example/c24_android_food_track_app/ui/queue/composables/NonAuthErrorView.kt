package com.example.c24_android_food_track_app.ui.queue.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.c24_android_food_track_app.util.PREVIEW_HEIGHT_DP
import com.example.c24_android_food_track_app.util.PREVIEW_WIDTH_DP

@Composable
fun NonAuthErrorView() = Box(contentAlignment = Alignment.Center) {
    Text(
        text = "Ups! you do not have authorization to see the orders",
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        textAlign = TextAlign.Center,
        fontStyle = FontStyle.Italic
    )
}

@Preview(widthDp = PREVIEW_WIDTH_DP, heightDp = PREVIEW_HEIGHT_DP, showBackground = true)
@Composable
fun NonAuthErrorViewPreview() = NonAuthErrorView()
