package com.example.c24_android_food_track_app.data.repositories

import android.util.Log
import com.example.c24_android_food_track_app.data.models.FoodTrackOrder
import com.example.c24_android_food_track_app.data.models.Status
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OrdersRepository {

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

                        if (id != null
                            && orderTitle != null
                            && status != null
                            && slot != null
                            && email != null
                        ) {
                            val orderStatus = Status.values().first { it.name.equals(status, ignoreCase = true) }
                            if (orderStatus != Status.Picked) {
                                _currentOrder.value = FoodTrackOrder(
                                    id = id,
                                    title = orderTitle,
                                    status = orderStatus,
                                    email = email,
                                    slot = slot,
                                    slotTime = slotTime
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

                    if (id != null
                        && orderTitle != null
                        && status != null
                        && slot != null
                        && email != null
                    ) {
                        orders.add(
                            FoodTrackOrder(
                                id = id,
                                title = orderTitle,
                                status = Status.valueOf(status),
                                email = email,
                                slot = slot,
                                slotTime = slotTime,
                            )
                        )
                    }
                }
                _orders.value = orders
            }
    }

    fun onOrderReady(orderViewEntity: FoodTrackOrder) {
        collection
            .document("user" + orderViewEntity.id)
            .update(
                mapOf(
                    "email" to orderViewEntity.email,
                    "food_order" to orderViewEntity.title,
                    "slot" to orderViewEntity.slot,
                    "user_id" to orderViewEntity.id,
                    "status" to Status.Ready
                )
            )
    }

    fun placeOrder(foodOrder: String, slot: String, slotTime: String) {
        firebaseAuth.currentUser?.let { user ->
            collection
                .document("user" + user.uid)
                .set(
                    mapOf(
                        "email" to user.email,
                        "food_order" to foodOrder,
                        "slot" to slot,
                        "user_id" to user.uid,
                        "status" to Status.Ordered,
                        "slot_time" to slotTime,
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