package com.example.c24_android_food_track_app.ui.menu

import android.content.ContentValues
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Random
import kotlin.math.abs
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class MenuViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<MenuUiState>(MenuUiState.Loading)
    val uiState: StateFlow<MenuUiState> = _uiState

    private val db = Firebase.firestore
    private val repository = OrdersRepository()

    private var selectedMenu: MenuViewEntity? = null

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

    fun sendOrder(selectedSlot: TimeSlotViewEntity) {
        val selectedMenu = selectedMenu ?: return
        repository.placeOrder(selectedMenu.dishTitle, selectedSlot.slotId, selectedSlot.timeStart + " - " + selectedSlot.timeEnd)
    }

    private fun updateSlotInDb(slot: Slots) {
        db.collection("Slots")
            .document("slot" + slot.slotId)
            .update(
                mapOf(
                    "remaining_orders" to (slot.remainingOrders.toInt() - 1).toString(),
                    "slot_id" to slot.slotId,
                    "time_end" to slot.timeEnd,
                    "time_start" to slot.timeStart
                )
            )
    }

    fun asapOrder() {
        val slots = ArrayList<Slots>()

        val docRef = db.collection("Slots")
        docRef.get()
            .addOnSuccessListener { document ->
                for (doc in document!!) {
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

                val formatter = DateTimeFormatter.ofPattern("hh:mm a")
                val nearestEmptySlot = slots.maxBy {
                    abs(LocalTime.now().toSecondOfDay() -
                                LocalTime.parse(it.timeStart + " PM", formatter).toSecondOfDay())
                }

             updateSlotInDb(nearestEmptySlot)
             // add user/order in the DB
            }
            .addOnFailureListener { exception ->
                //Log.d(TAG, "get failed with ", exception)
            }
    }
    data class Slots(
        val slotId: String,
        val timeStart: String,
        val timeEnd: String,
        val remainingOrders: String,
    )

}
