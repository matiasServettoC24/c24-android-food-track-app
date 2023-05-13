package com.example.c24_android_food_track_app.ui.menu.adapters

import com.example.c24_android_food_track_app.domain.ViewEntity
import com.example.c24_android_food_track_app.domain.menu.MenuViewEntity
import com.example.c24_android_food_track_app.data.models.TimeSlot
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

class MenuAdapter(
    selectMenuCallback: (MenuViewEntity) -> Unit,
    selectTimeSlotCallback: (TimeSlot) -> Unit,
    asapBtnCallback: () -> Unit,
) : ListDelegationAdapter<List<ViewEntity>>(
    menuDelegate(selectMenuCallback),
    timeSlotDelegate(selectTimeSlotCallback),
    waitingForOrderAdapterDelegate(),
    orderReadyAdapterDelegate(),
    asapBtnAdapterDelegate(asapBtnCallback),
    menuTitleAdapterDelegate(),
)