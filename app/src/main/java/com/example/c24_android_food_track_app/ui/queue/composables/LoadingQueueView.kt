package com.example.c24_android_food_track_app.ui.queue.composables

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.c24_android_food_track_app.util.PREVIEW_WIDTH_DP

@Composable
fun LoadingQueueView() = CircularProgressIndicator()

@Preview(widthDp = PREVIEW_WIDTH_DP, showBackground = true)
@Composable
fun LoadingQueueViewPreview() = LoadingQueueView()
