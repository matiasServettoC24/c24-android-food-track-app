package com.example.c24_android_food_track_app.ui.menu.adapters

import com.example.c24_android_food_track_app.R
import com.example.c24_android_food_track_app.databinding.MenuItemBinding
import com.example.c24_android_food_track_app.databinding.MenuOrderReadyItemBinding
import com.example.c24_android_food_track_app.databinding.MenuWaitingForOrderItemBinding
import com.example.c24_android_food_track_app.databinding.TimeslotItemBinding
import com.example.c24_android_food_track_app.domain.ViewEntity
import com.example.c24_android_food_track_app.domain.menu.*
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate

fun menuDelegate(
    orderCallback: () -> Unit
) = adapterDelegate<MenuViewEntity, ViewEntity>(R.layout.menu_item) {

    val binding = MenuItemBinding.bind(itemView)

    bind {
        binding.dishTitle.text = item.dishTitle
        binding.dishType.text = item.dishType
    }

    binding.orderBtn.setOnClickListener { orderCallback() }
}

fun timeSlotDelegate(selectTimeSlotCallback: () -> Unit) = adapterDelegate<TimeSlotViewEntity, ViewEntity>(R.layout.timeslot_item) {

    val binding = TimeslotItemBinding.bind(itemView)

    bind {
        binding.startTime.text = item.timeStart
        binding.endTime.text = item.timeEnd
        binding.remainingSlots.text = item.remainingOrders
    }

    binding.selectTimeSlotBtn.setOnClickListener { selectTimeSlotCallback() }
}

fun waitingForOrderAdapterDelegate() = adapterDelegate<WaitingForOrderViewEntity, ViewEntity>(R.layout.menu_waiting_for_order_item) {

    val binding = MenuWaitingForOrderItemBinding.bind(itemView)

    bind {
        binding.order.text = "Enjoy your ${item.orderTitle}"
    }
}

fun orderReadyAdapterDelegate() = adapterDelegate<OrderReadyViewEntity, ViewEntity>(R.layout.menu_order_ready_item) {

    val binding = MenuOrderReadyItemBinding.bind(itemView)

    bind {
        binding.order.text = item.orderTitle
    }
}

fun asapBtnAdapterDelegate() = adapterDelegate<AsapBtnViewEntity, ViewEntity>(R.layout.asap_timeslot_item) {

}
