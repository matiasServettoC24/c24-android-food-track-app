package com.example.c24_android_food_track_app.ui.menu

import com.example.c24_android_food_track_app.domain.ViewEntity

sealed class MenuUiState {
    object Loading : MenuUiState()
    class DishSelection(val dishList: List<ViewEntity>) : MenuUiState()
    class TimeSelection(val timeList: List<ViewEntity>) : MenuUiState()
    object Error : MenuUiState()
}
