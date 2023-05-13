package com.example.c24_android_food_track_app.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.c24_android_food_track_app.data.repositories.OrdersRepository
import com.example.c24_android_food_track_app.domain.LoadingViewEntity
import com.example.c24_android_food_track_app.domain.ViewEntity
import com.example.c24_android_food_track_app.data.models.Status
import com.example.c24_android_food_track_app.domain.admin.OrderViewEntity
import com.example.c24_android_food_track_app.domain.admin.OrdersTitleViewEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {

    private val repository = OrdersRepository()

    private val _viewEntities = MutableStateFlow<List<ViewEntity>>(listOf(LoadingViewEntity))
    val viewEntities: StateFlow<List<ViewEntity>> = _viewEntities

    init {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                repository.orders.collect {
                    _viewEntities.value = arrayListOf<ViewEntity>()
                        .apply { add(OrdersTitleViewEntity) }
                        .apply { addAll(it.map { OrderViewEntity(it) }) }
                }
            }
            launch {
                repository.initOrdersDataBase()
            }
        }
    }

    fun onOrderReady(orderViewEntity: OrderViewEntity) {
        updateOrderState(orderViewEntity, Status.Ready)
        repository.onOrderReady(orderViewEntity.order)
    }

    fun onOrderPickedUp(orderViewEntity: OrderViewEntity) {
        updateOrderState(orderViewEntity, Status.Picked)
        repository.deliverOrder(orderViewEntity.order)
    }

    private fun updateOrderState(order: OrderViewEntity, state: Status) {
        _viewEntities.value = _viewEntities.value.map {
            if (it == order) {
                order.copy(order = order.order.copy(status = state))
            } else {
                it
            }
        }
    }
}