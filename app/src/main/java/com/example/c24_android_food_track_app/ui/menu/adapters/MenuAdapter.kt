package com.example.c24_android_food_track_app.ui.menu.adapters

import com.example.c24_android_food_track_app.domain.ViewEntity
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

class MenuAdapter: ListDelegationAdapter<List<ViewEntity>>(
    menuDelegate(),
    timeSlotDelegate(),
)