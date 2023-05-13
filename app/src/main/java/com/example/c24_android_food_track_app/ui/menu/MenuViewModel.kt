package com.example.c24_android_food_track_app.ui.menu

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.c24_android_food_track_app.data.models.FoodTrackOrder
import com.example.c24_android_food_track_app.data.models.Status
import com.example.c24_android_food_track_app.data.repositories.OrdersRepository
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
import kotlinx.coroutines.launch

class MenuViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<MenuUiState>(MenuUiState.Loading)
    val uiState: StateFlow<MenuUiState> = _uiState

    private val db = Firebase.firestore
    private val repository = OrdersRepository()

    init {
        viewModelScope.launch { repository.initCurrentOrderDataBase() }
        viewModelScope.launch(Dispatchers.IO) {
            repository.currentOrder.collect { currentOrder ->
                if (currentOrder == null) {
                    loadMenu()
                } else {
                    loadCurrentOrder(currentOrder)
                }
            }
        }
    }

    private suspend fun loadCurrentOrder(currentOrder: FoodTrackOrder) {
        _uiState.emit(
            when (currentOrder.status) {
                Status.Ordered, Status.Picked -> MenuUiState.WaitingForOrder(currentOrder.title)
                Status.Ready -> MenuUiState.OrderReady(currentOrder.title)
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

    fun loadTimeSlots() {
        db.collection("Slots")
            .addSnapshotListener { value, e ->
                val slots = ArrayList<ViewEntity>()
                slots.add( AsapBtnViewEntity)
                if (e != null) {
                    Log.w(ContentValues.TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                for (doc in value!!) {
                    val slotId = doc.getString("slot_id")
                    val timeStart = doc.getString("time_start")
                    val timeEnd = doc.getString("time_end")
                    val remainingOrders = doc.getString("remaining_orders")

                    if (slotId != null
                        && timeStart != null
                        && timeEnd != null
                        && remainingOrders != null

                    ) {
                        if (remainingOrders.toInt() > 0) {
                            // add to list the

                            slots.add(
                                TimeSlotViewEntity(
                                    slotId = slotId,
                                    timeStart = timeStart,
                                    timeEnd = timeEnd,
                                    remainingOrders = remainingOrders,
                                )
                            )
                        }
                    }

                }
                _uiState.value = MenuUiState.TimeSelection(slots)
            }
    }

    fun sendOrder(foodOrder: String, slotId: String) {
        repository.placeOrder(foodOrder, slotId)
    }
}
