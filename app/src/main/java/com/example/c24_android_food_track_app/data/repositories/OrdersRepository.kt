package com.example.c24_android_food_track_app.data.repositories

import com.example.c24_android_food_track_app.data.models.FoodTrackOrder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OrdersRepository {

    private val _orders = MutableStateFlow<List<FoodTrackOrder>>(listOf())
    val orders: StateFlow<List<FoodTrackOrder>> = _orders

    private val db = Firebase.firestore
    private val collection get() = db.collection("Users")

    suspend fun initDataBase() {
        collection
            .whereEqualTo("status", "ordered")
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

                    if (id != null
                        && orderTitle != null
                        && status != null
                        && slot != null
                        && email != null
                    ) {
                        orders.add(
                            FoodTrackOrder(
                                id, orderTitle, false,
                                status = status,
                                email = email,
                                slot = slot
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
                    "status" to "Ready"
                )
            )
    }
}