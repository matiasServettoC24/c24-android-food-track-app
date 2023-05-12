package com.example.c24_android_food_track_app.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.c24_android_food_track_app.domain.LoadingViewEntity
import com.example.c24_android_food_track_app.domain.ViewEntity
import com.example.c24_android_food_track_app.domain.admin.NonAuthAdminViewEntity
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
            _viewEntities.postValue(listOf(NonAuthAdminViewEntity))
        }
    }
}