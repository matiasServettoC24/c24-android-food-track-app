package com.example.c24_android_food_track_app.ui.menu

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.c24_android_food_track_app.domain.menu.MenuViewEntity
import com.example.c24_android_food_track_app.domain.menu.TimeSlotViewEntity
import com.example.c24_android_food_track_app.ui.menu.models.DishType
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

    fun getSlots() {

        db.collection("Slots")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w(ContentValues.TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }
                val slots = ArrayList<Slots>()
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
                                Slots(
                                    slotId = slotId,
                                    timeStart = timeStart,
                                    timeEnd = timeEnd,
                                    remainingOrders = remainingOrders,
                                )
                            )
                        }
                    }
                }
            }
    }
}

data class Slots(
    val slotId: String,
    val timeStart: String,
    val timeEnd: String,
    val remainingOrders: String,
)