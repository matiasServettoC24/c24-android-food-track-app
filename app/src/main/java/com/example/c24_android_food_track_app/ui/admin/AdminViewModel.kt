package com.example.c24_android_food_track_app.ui.admin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.c24_android_food_track_app.domain.LoadingViewEntity
import com.example.c24_android_food_track_app.domain.ViewEntity
import com.example.c24_android_food_track_app.domain.admin.ErrorViewEntity
import com.example.c24_android_food_track_app.domain.admin.OrderViewEntity
import com.example.c24_android_food_track_app.domain.admin.OrdersTitleViewEntity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {

    private val db = Firebase.firestore

    private val _viewEntities = MutableLiveData<List<ViewEntity>>().apply {
        value = listOf(LoadingViewEntity)
    }

    val viewEntities: LiveData<List<ViewEntity>> = _viewEntities

    fun loadOrders() {
        viewModelScope.launch(Dispatchers.IO) {

            db.collection("Users")
                .whereEqualTo("status", "ordered")
                .addSnapshotListener { value, e ->
                    if (e != null) {
                        _viewEntities.value = listOf(ErrorViewEntity)
                        return@addSnapshotListener
                    }
                    val orders = ArrayList<OrderViewEntity>()
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
                            orders.add(OrderViewEntity(id, orderTitle, false,
                                status = status,
                                email = email,
                                slot = slot
                            ))
                        }
                    }
                    _viewEntities.value = arrayListOf<ViewEntity>()
                        .apply { add(OrdersTitleViewEntity) }
                        .apply { addAll(orders) }
                }
        }
    }

    fun onOrderReady(orderId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(300)
            _viewEntities.postValue(_viewEntities.value!!.map {
                if (it is OrderViewEntity && it.id.equals(orderId, ignoreCase = true)) {
                    it.copy(isReady = true)
                } else {
                    it
                }
            })
        }
    }

    fun submitOrder(orderViewEntity: OrderViewEntity) {
        db.collection("users")
            .document("user" + orderViewEntity.id)
            .update(mapOf(
                "email" to orderViewEntity.email,
                "food_order" to orderViewEntity.title,
                "slot" to orderViewEntity.slot,
                "user_id" to orderViewEntity.id,
                "status" to "Ready"
            ))
    }
}