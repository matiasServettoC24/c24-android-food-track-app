package com.example.c24_android_food_track_app.ui.admin

import android.content.ContentValues.TAG
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
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val collection get() = db.collection("Users")

    private val _viewEntities = MutableLiveData<List<ViewEntity>>().apply {
        value = listOf(LoadingViewEntity)
    }

    val viewEntities: LiveData<List<ViewEntity>> = _viewEntities

    fun loadOrders() {
        viewModelScope.launch(Dispatchers.IO) {

            collection
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
                            orders.add(
                                OrderViewEntity(
                                    id, orderTitle, false,
                                    status = status,
                                    email = email,
                                    slot = slot
                                )
                            )
                        }
                    }
                    _viewEntities.value = arrayListOf<ViewEntity>()
                        .apply { add(OrdersTitleViewEntity) }
                        .apply { addAll(orders) }
                }
        }
    }

    fun onOrderReady(orderViewEntity: OrderViewEntity) {
        _viewEntities.postValue(_viewEntities.value!!.map {
            if (it == orderViewEntity) {
                orderViewEntity.copy(isReady = true)
            } else {
                it
            }
        })
        viewModelScope.launch(Dispatchers.IO) {
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
}