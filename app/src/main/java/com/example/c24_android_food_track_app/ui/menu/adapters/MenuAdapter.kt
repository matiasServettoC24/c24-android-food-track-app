package com.example.c24_android_food_track_app.ui.menu.adapters

import com.example.c24_android_food_track_app.domain.ViewEntity
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

class MenuAdapter(
    orderCallback: () -> Unit,
    selectTimeSlotCallback: () -> Unit,
    asapBtnCallback: () -> Unit,
) : ListDelegationAdapter<List<ViewEntity>>(
    menuDelegate(orderCallback),
    timeSlotDelegate(selectTimeSlotCallback),
    waitingForOrderAdapterDelegate(),
    orderReadyAdapterDelegate(),
    asapBtnAdapterDelegate(asapBtnCallback),
    menuTitleAdapterDelegate(),
)