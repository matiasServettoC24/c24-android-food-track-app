package com.example.c24_android_food_track_app.ui.menu

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.c24_android_food_track_app.domain.ViewEntity
import com.example.c24_android_food_track_app.domain.menu.AsapBtnViewEntity
import com.example.c24_android_food_track_app.domain.menu.MenuTitleViewEntity
import com.example.c24_android_food_track_app.domain.menu.MenuViewEntity
import com.example.c24_android_food_track_app.domain.menu.TimeSlotViewEntity
import com.example.c24_android_food_track_app.ui.menu.models.DishType
import kotlinx.coroutines.delay
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MenuViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<MenuUiState>(MenuUiState.Loading)
    val uiState: StateFlow<MenuUiState> = _uiState

    private val db = Firebase.firestore

    suspend fun loadDishes() {
        _uiState.emit(
            MenuUiState.DishSelection(
                listOf(
                    MenuTitleViewEntity,
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

    suspend fun sendOrder() {
        _uiState.emit(MenuUiState.WaitingForOrder("Pizza Muzzarella"))
        delay(10_000)
        _uiState.emit(MenuUiState.OrderReady("Pizza Muzzarella"))
    }
}