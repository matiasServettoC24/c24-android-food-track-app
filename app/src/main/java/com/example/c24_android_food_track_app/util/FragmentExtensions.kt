package com.example.c24_android_food_track_app.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment

fun Fragment.updateContent(content: @Composable () -> Unit) {
    val composeView = view as? ComposeView ?: return
    composeView.setContent {
        MaterialTheme {
            content()
        }
    }
}