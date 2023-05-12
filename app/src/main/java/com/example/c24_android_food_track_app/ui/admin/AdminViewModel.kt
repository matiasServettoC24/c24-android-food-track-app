package com.example.c24_android_food_track_app.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.c24_android_food_track_app.data.repositories.OrdersRepository
import com.example.c24_android_food_track_app.domain.LoadingViewEntity
import com.example.c24_android_food_track_app.domain.ViewEntity
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
                        .apply { addAll(it) }
                }
            }
            launch {
                repository.initDataBase()
            }
        }
    }

    fun onOrderReady(orderViewEntity: OrderViewEntity) {
        _viewEntities.value = _viewEntities.value.map {
            if (it == orderViewEntity) {
                orderViewEntity.copy(isReady = true)
            } else {
                it
            }
        }
        repository.onOrderReady(orderViewEntity)
    }
}