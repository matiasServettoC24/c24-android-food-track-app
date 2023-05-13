package com.example.c24_android_food_track_app.ui.menu.adapters

import android.widget.TextView
import com.example.c24_android_food_track_app.R
import com.example.c24_android_food_track_app.databinding.AsapTimeslotItemBinding
import com.example.c24_android_food_track_app.databinding.MenuItemBinding
import com.example.c24_android_food_track_app.databinding.MenuOrderReadyItemBinding
import com.example.c24_android_food_track_app.databinding.MenuWaitingForOrderItemBinding
import com.example.c24_android_food_track_app.databinding.TimeslotItemBinding
import com.example.c24_android_food_track_app.domain.ViewEntity
import com.example.c24_android_food_track_app.domain.menu.AsapBtnViewEntity
import com.example.c24_android_food_track_app.domain.menu.MenuTitleViewEntity
import com.example.c24_android_food_track_app.domain.menu.MenuViewEntity
import com.example.c24_android_food_track_app.domain.menu.OrderReadyViewEntity
import com.example.c24_android_food_track_app.data.models.TimeSlot
import com.example.c24_android_food_track_app.databinding.MenuOrderPickedUpItemBinding
import com.example.c24_android_food_track_app.domain.menu.OrderPickedViewEntity
import com.example.c24_android_food_track_app.domain.menu.WaitingForOrderViewEntity
import com.example.c24_android_food_track_app.ui.menu.models.DishType
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate

fun menuDelegate(
    selectMenuCallback: (MenuViewEntity) -> Unit
) = adapterDelegate<MenuViewEntity, ViewEntity>(R.layout.menu_item) {

    val binding = MenuItemBinding.bind(itemView)

    bind {
        binding.dishTitle.text = item.dishTitle
        if (item.dishType == DishType.VEGETARIAN.name) {
            binding.dishType.setImageResource(R.drawable.vegetarian_symbol)

        } else {
            binding.dishType.setImageResource(R.drawable.meat_icon)

        }
    }

    binding.orderBtn.setOnClickListener { selectMenuCallback(item) }
}

fun timeSlotDelegate(selectTimeSlotCallback: (TimeSlot) -> Unit) = adapterDelegate<TimeSlot, ViewEntity>(R.layout.timeslot_item) {

    val binding = TimeslotItemBinding.bind(itemView)

    bind {
        binding.startTime.text = item.timeStart
        binding.endTime.text = item.timeEnd
        binding.remainingSlots.text = item.remainingOrders
    }

    binding.selectTimeSlotBtn.setOnClickListener { selectTimeSlotCallback(item) }
}

fun waitingForOrderAdapterDelegate() = adapterDelegate<WaitingForOrderViewEntity, ViewEntity>(R.layout.menu_waiting_for_order_item) {

    val binding = MenuWaitingForOrderItemBinding.bind(itemView)

    bind {
        binding.order.text = "Preparing your ${item.orderTitle} at ${item.orderTime}"
    }
}

fun orderReadyAdapterDelegate() = adapterDelegate<OrderReadyViewEntity, ViewEntity>(R.layout.menu_order_ready_item) {

    val binding = MenuOrderReadyItemBinding.bind(itemView)

    bind {
        binding.order.text = item.orderTitle
    }
}

fun orderPickedUpAdapterDelegate() = adapterDelegate<OrderPickedViewEntity, ViewEntity>(R.layout.menu_order_picked_up_item) {

    val binding = MenuOrderPickedUpItemBinding.bind(itemView)

    bind {
        binding.order.text = item.orderTitle
    }
}

fun asapBtnAdapterDelegate(asapBtnCallback: () -> Unit) = adapterDelegate<AsapBtnViewEntity, ViewEntity>(R.layout.asap_timeslot_item) {
    val binding = AsapTimeslotItemBinding.bind(itemView)
    binding.asapBtn.setOnClickListener { asapBtnCallback() }

}

fun menuTitleAdapterDelegate() =
    adapterDelegate<MenuTitleViewEntity, ViewEntity>(R.layout.admin_title_item) {
        itemView.findViewById<TextView>(R.id.title).text = "Menu:"
    }
