package com.example.c24_android_food_track_app.ui.queue.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun NonAuthErrorView() = Text(text = "Ups! you do not have authorization to see the orders")

@Preview(widthDp = 300, showBackground = true)
@Composable
fun NonAuthErrorViewPreview() = NonAuthErrorView()
