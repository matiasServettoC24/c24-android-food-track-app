package com.example.c24_android_food_track_app.ui.menu

import com.example.c24_android_food_track_app.domain.ViewEntity

sealed class MenuUiState {
    object Loading : MenuUiState()
    data class DishSelection(val dishList: List<ViewEntity>) : MenuUiState()
    data class TimeSelection(val timeList: List<ViewEntity>) : MenuUiState()

    data class WaitingForOrder(val orderTitle: String) : MenuUiState()

    data class OrderReady(val orderTitle: String) : MenuUiState()

    object Error : MenuUiState()
}
