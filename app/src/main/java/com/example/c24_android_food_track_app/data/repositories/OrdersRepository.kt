package com.example.c24_android_food_track_app.data.repositories

import android.util.Log
import com.example.c24_android_food_track_app.data.models.FoodTrackOrder
import com.example.c24_android_food_track_app.data.models.Status
import com.example.c24_android_food_track_app.data.models.TimeSlot
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs

class OrdersRepository {

    private val formatter = DateTimeFormatter.ofPattern("HH:mm")

    private val _orders = MutableStateFlow<List<FoodTrackOrder>>(listOf())
    val orders: StateFlow<List<FoodTrackOrder>> = _orders

    private val db = Firebase.firestore
    private val collection get() = db.collection("Users")

    private val firebaseAuth = FirebaseAuth.getInstance()

    private val _currentOrder = MutableStateFlow<FoodTrackOrder?>(null)
    val currentOrder: StateFlow<FoodTrackOrder?> = _currentOrder

    fun initCurrentOrderDataBase() {
        firebaseAuth.currentUser?.let { user ->
            collection
                .whereEqualTo("email", user.email!!)
                .addSnapshotListener { value, e ->
                    if (e != null) {
                        _currentOrder.value = null
                        return@addSnapshotListener
                    }
                    for (doc in value!!) {
                        val id = doc.getString("user_id")
                        val orderTitle = doc.getString("food_order")


                        val email = doc.getString("email")
                        val slot = doc.getString("slot")
                        val status = doc.getString("status")
                        val slotTime = doc.getString("slot_time")
                        val timeStart = doc.getString("time_start")

                        if (id != null
                            && orderTitle != null
                            && status != null
                            && slot != null
                            && email != null
                            && timeStart != null
                        ) {
                            val orderStatus = Status.values().first { it.name.equals(status, ignoreCase = true) }
                            if (orderStatus != Status.Picked) {
                                _currentOrder.value = FoodTrackOrder(
                                    id = id,
                                    title = orderTitle,
                                    status = orderStatus,
                                    email = email,
                                    slot = slot,
                                    timeStart = timeStart,
                                    slotTime = slotTime,
                                )
                                return@addSnapshotListener
                            }
                        }
                    }
                    _currentOrder.value = null
                }
        }
    }

    fun initOrdersDataBase() {
        collection
            .whereIn("status", arrayListOf(Status.Ordered, Status.Ready))
            .addSnapshotListener { value, e ->
                if (e != null) {
                    _orders.value = listOf()
                    return@addSnapshotListener
                }
                val orders = arrayListOf<FoodTrackOrder>()
                for (doc in value!!) {
                    val id = doc.getString("user_id")
                    val orderTitle = doc.getString("food_order")


                    val email = doc.getString("email")
                    val slot = doc.getString("slot")
                    val status = doc.getString("status")
                    val slotTime = doc.getString("slot_time")
                    val timeStart = doc.getString("time_start")

                    if (id != null
                        && orderTitle != null
                        && status != null
                        && slot != null
                        && email != null
                        && timeStart != null
                    ) {
                        orders.add(
                            FoodTrackOrder(
                                id = id,
                                title = orderTitle,
                                status = Status.valueOf(status),
                                email = email,
                                slot = slot,
                                timeStart = timeStart,
                                slotTime = slotTime,
                            )
                        )
                    }
                }
                _orders.value = orders.sortedBy {
                    abs(LocalTime.now().toSecondOfDay() - LocalTime.parse(it.timeStart, formatter).toSecondOfDay())
                }
            }
    }

    fun onOrderReady(order: FoodTrackOrder) {
        collection
            .document("user" + order.id)
            .update(
                mapOf(
                    "email" to order.email,
                    "food_order" to order.title,
                    "slot" to order.slot,
                    "user_id" to order.id,
                    "time_start" to order.timeStart,
                    "status" to Status.Ready
                )
            )
    }

    fun placeOrder(foodOrder: String, selectedSlot: TimeSlot) {
        firebaseAuth.currentUser?.let { user ->
            collection
                .document("user" + user.uid)
                .set(
                    mapOf(
                        "email" to user.email,
                        "food_order" to foodOrder,
                        "slot" to selectedSlot.slotId,
                        "user_id" to user.uid,
                        "status" to Status.Ordered,
                        "slot_time" to selectedSlot.timeStart + " - " + selectedSlot.timeEnd,
                        "time_start" to selectedSlot.timeStart
                    )
                )
                .addOnCompleteListener {
                    Log.d("Mati", "place order completed")
                }
        }
    }

    fun deliverOrder(orderViewEntity: FoodTrackOrder) {
        collection
            .document("user" + orderViewEntity.id)
            .update(
                mapOf(
                    "email" to orderViewEntity.email,
                    "food_order" to orderViewEntity.title,
                    "slot" to orderViewEntity.slot,
                    "user_id" to orderViewEntity.id,
                    "status" to Status.Picked
                )
            )
    }
}