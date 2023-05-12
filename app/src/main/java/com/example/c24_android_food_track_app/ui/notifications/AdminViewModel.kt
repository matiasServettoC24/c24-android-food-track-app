package com.example.c24_android_food_track_app.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.c24_android_food_track_app.domain.ViewEntity
import com.example.c24_android_food_track_app.domain.admin.NonAuthAdminViewEntity

class AdminViewModel : ViewModel() {

    private val _viewEntities = MutableLiveData<List<ViewEntity>>().apply {
        value = listOf(NonAuthAdminViewEntity())
    }
    val viewEntities: LiveData<List<ViewEntity>> = _viewEntities
}