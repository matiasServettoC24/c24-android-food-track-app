package com.example.c24_android_food_track_app.ui.menu.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.c24_android_food_track_app.ui.shared.composables.ErrorView
import com.example.c24_android_food_track_app.util.PREVIEW_HEIGHT_DP
import com.example.c24_android_food_track_app.util.PREVIEW_WIDTH_DP


@Composable
fun MenuErrorView() = ErrorView(errorMessage = "Ups! There was an error loading your menu!")

@Preview(widthDp = PREVIEW_WIDTH_DP, heightDp = PREVIEW_HEIGHT_DP, showBackground = true)
@Composable
fun MenuErrorViewPreview() = MenuErrorView()
