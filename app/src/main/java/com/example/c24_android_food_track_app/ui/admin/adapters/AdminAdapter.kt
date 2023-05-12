package com.example.c24_android_food_track_app.ui.admin.adapters

import com.example.c24_android_food_track_app.domain.ViewEntity
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

class AdminAdapter : ListDelegationAdapter<List<ViewEntity>>(
    nonAuthAdminAdapterDelegate()
)