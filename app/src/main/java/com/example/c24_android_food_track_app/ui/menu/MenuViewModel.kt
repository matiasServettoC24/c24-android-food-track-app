package com.example.c24_android_food_track_app.ui.menu

import androidx.lifecycle.ViewModel
import com.example.c24_android_food_track_app.domain.menu.MenuViewEntity
import com.example.c24_android_food_track_app.domain.menu.TimeSlotViewEntity
import com.example.c24_android_food_track_app.ui.menu.models.DishType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MenuViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<MenuUiState>(MenuUiState.Loading)
    val uiState: StateFlow<MenuUiState> = _uiState

    suspend fun loadDishes() {
        _uiState.emit(
            MenuUiState.DishSelection(
                listOf(
                    MenuViewEntity(dishTitle = "Pizza1", dishType = DishType.NON_VEGETARIAN.name),
                    MenuViewEntity(dishTitle = "Pizza2", dishType = DishType.VEGETARIAN.name),
                    MenuViewEntity(dishTitle = "Pizza3", dishType = DishType.NON_VEGETARIAN.name),
                    MenuViewEntity(dishTitle = "Pizza4", dishType = DishType.VEGETARIAN.name),
                    MenuViewEntity(dishTitle = "Pizza5", dishType = DishType.NON_VEGETARIAN.name),
                    MenuViewEntity(dishTitle = "Pizza6", dishType = DishType.VEGETARIAN.name),
                )
            )
        )
    }

    suspend fun loadTimeSlots() {
        _uiState.emit(
            MenuUiState.TimeSelection(
                listOf(
                    TimeSlotViewEntity(startTime = "12:00", endTime = "12:10"),
                    TimeSlotViewEntity(startTime = "12:10", endTime = "12:20"),
                    TimeSlotViewEntity(startTime = "12:30", endTime = "12:40"),
                    TimeSlotViewEntity(startTime = "12:40", endTime = "12:50"),
                    TimeSlotViewEntity(startTime = "12:50", endTime = "12:60")
                )
            )
        )
    }
}
