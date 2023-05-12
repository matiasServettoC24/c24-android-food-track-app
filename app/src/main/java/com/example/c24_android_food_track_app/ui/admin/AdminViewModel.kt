package com.example.c24_android_food_track_app.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.c24_android_food_track_app.domain.LoadingViewEntity
import com.example.c24_android_food_track_app.domain.ViewEntity
import com.example.c24_android_food_track_app.domain.admin.NonAuthAdminViewEntity
import com.example.c24_android_food_track_app.domain.admin.OrderViewEntity
import com.example.c24_android_food_track_app.domain.admin.OrdersTitleViewEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {

    private val _viewEntities = MutableLiveData<List<ViewEntity>>().apply {
        value = listOf(LoadingViewEntity)
    }

    val viewEntities: LiveData<List<ViewEntity>> = _viewEntities

    fun loadOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1_000)
//            _viewEntities.postValue(listOf(NonAuthAdminViewEntity))
            _viewEntities.postValue(
                listOf(
                    OrdersTitleViewEntity,
                    OrderViewEntity("1", "Pizza funghi", false),
                    OrderViewEntity("2", "Pizza funghi", false),
                    OrderViewEntity("3", "Pizza muzzarella", false),
                    OrderViewEntity("4", "Pizza pepperoni", false),
                    OrderViewEntity("5", "Pizza muzzarella", false),
                    OrderViewEntity("6", "Pizza muzzarella", false),
                    OrderViewEntity("7", "Pizza muzzarella", false),
                    OrderViewEntity("8", "Pizza veggie", false),
                    OrderViewEntity("9", "Pizza pepperoni", false),
                )
            )
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
}