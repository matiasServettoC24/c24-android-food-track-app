package com.example.c24_android_food_track_app.ui.queue.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.c24_android_food_track_app.ui.shared.composables.LoadingView
import com.example.c24_android_food_track_app.util.PREVIEW_HEIGHT_DP
import com.example.c24_android_food_track_app.util.PREVIEW_WIDTH_DP

@Composable
fun LoadingQueueView() = LoadingView(loadingMessage = "Loading Queue...")

@Preview(widthDp = PREVIEW_WIDTH_DP, heightDp = PREVIEW_HEIGHT_DP, showBackground = true)
@Composable
fun LoadingQueueViewPreview() = LoadingQueueView()
