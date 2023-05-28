package com.example.c24_android_food_track_app.ui.menu

import com.example.c24_android_food_track_app.data.models.FoodTrackOrder
import com.example.c24_android_food_track_app.domain.ViewEntity
import com.example.c24_android_food_track_app.domain.menu.DishViewEntity

sealed class MenuUiState {
    object Loading : MenuUiState()
    data class DishSelection(val dishes: List<DishViewEntity>) : MenuUiState()
    data class TimeSelection(val timeSlots: List<ViewEntity>) : MenuUiState()

    data class WaitingForOrder(val currentOrder: FoodTrackOrder) : MenuUiState()

    data class OrderReady(val currentOrder: FoodTrackOrder) : MenuUiState()

    data class OrderPicked(val currentOrder: FoodTrackOrder) : MenuUiState()

    object Error : MenuUiState()
}
