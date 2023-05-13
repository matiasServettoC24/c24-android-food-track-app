package com.example.c24_android_food_track_app.ui.admin.adapters

import com.example.c24_android_food_track_app.domain.ViewEntity
import com.example.c24_android_food_track_app.data.models.FoodTrackOrder
import com.example.c24_android_food_track_app.domain.admin.OrderViewEntity
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

class AdminAdapter(
    onOrderReadyCallback: (OrderViewEntity) -> Unit,
    onOrderPickedUpCallback: (OrderViewEntity) -> Unit,
    deleteOrderCallback: (OrderViewEntity) -> Unit,
) : ListDelegationAdapter<List<ViewEntity>>(
    nonAuthAdminAdapterDelegate(),
    loadingAdapterDelegate(),
    orderAdapterDelegate(onOrderReadyCallback, onOrderPickedUpCallback, deleteOrderCallback),
    ordersTitleAdapterDelegate(),
    errorAdapterDelegate(),
)