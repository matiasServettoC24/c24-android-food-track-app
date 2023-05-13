package com.example.c24_android_food_track_app.ui.menu

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.c24_android_food_track_app.data.models.FoodTrackOrder
import com.example.c24_android_food_track_app.data.models.Status
import com.example.c24_android_food_track_app.data.repositories.OrdersRepository
import com.example.c24_android_food_track_app.data.repositories.SlotsRepository
import com.example.c24_android_food_track_app.domain.ViewEntity
import com.example.c24_android_food_track_app.domain.menu.AsapBtnViewEntity
import com.example.c24_android_food_track_app.domain.menu.MenuTitleViewEntity
import com.example.c24_android_food_track_app.domain.menu.MenuViewEntity
import com.example.c24_android_food_track_app.domain.menu.TimeSlotViewEntity
import com.example.c24_android_food_track_app.ui.menu.models.DishType
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class MenuViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<MenuUiState>(MenuUiState.Loading)
    val uiState: StateFlow<MenuUiState> = _uiState

    private val ordersRepository = OrdersRepository()
    private val slotsRepository = SlotsRepository()

    private var selectedMenu: MenuViewEntity? = null

    init {
        viewModelScope.launch { ordersRepository.initCurrentOrderDataBase() }
        viewModelScope.launch(Dispatchers.IO) {
            ordersRepository.currentOrder.collect { currentOrder ->
                if (currentOrder == null) {
                    loadMenu()
                } else {
                    loadCurrentOrder(currentOrder)
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            slotsRepository.slots.collect { slots ->
                if (_uiState.value is MenuUiState.TimeSelection) {
                    updateTimeSlots(slots)
                }
            }
        }
    }

    private suspend fun loadCurrentOrder(currentOrder: FoodTrackOrder) {
        selectedMenu = null
        _uiState.emit(
            when (currentOrder.status) {
                Status.Ordered, Status.Picked -> MenuUiState.WaitingForOrder(currentOrder)
                Status.Ready -> MenuUiState.OrderReady(currentOrder)
            }
        )
    }

    private fun loadMenu() {
        _uiState.value = MenuUiState.DishSelection(
            listOf(
                MenuTitleViewEntity,
                MenuViewEntity(
                    dishTitle = "Pizza Mozzarella",
                    dishType = DishType.NON_VEGETARIAN.name
                ),
                MenuViewEntity(dishTitle = "Pizza Margarita", dishType = DishType.VEGETARIAN.name),
                MenuViewEntity(
                    dishTitle = "Pizza Pepperoni",
                    dishType = DishType.NON_VEGETARIAN.name
                ),
                MenuViewEntity(
                    dishTitle = "Pizza Quattro Formaggi",
                    dishType = DishType.VEGETARIAN.name
                ),
                MenuViewEntity(dishTitle = "Pizza Veggie", dishType = DishType.VEGETARIAN.name),
            )
        )
    }

    fun loadTimeSlots(selectedMenu: MenuViewEntity) {
        this.selectedMenu = selectedMenu
        updateTimeSlots(slotsRepository.slots.value)
    }

    private fun updateTimeSlots(slots: List<TimeSlotViewEntity>) {
        _uiState.value = MenuUiState.TimeSelection(
            arrayListOf<ViewEntity>()
                .apply { add(AsapBtnViewEntity) }
                .apply { addAll(slots) }
        )
    }

    fun updateSlotAndSendOrder(selectedSlot: TimeSlotViewEntity) {
        val selectedMenu = selectedMenu ?: return
        updateSlotInDb(selectedSlot)
        ordersRepository.placeOrder(selectedMenu.dishTitle, selectedSlot)
    }

    private fun updateSlotInDb(slot: TimeSlotViewEntity) {
        slotsRepository.useSlot(slot)
    }

    fun asapOrder() = updateSlotAndSendOrder(slotsRepository.asapSlot)
}
