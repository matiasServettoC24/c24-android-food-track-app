package com.example.c24_android_food_track_app.data.repositories

import com.example.c24_android_food_track_app.domain.menu.TimeSlotViewEntity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SlotsRepository {

    private val _slots = MutableStateFlow<List<TimeSlotViewEntity>>(listOf())
    val slots: StateFlow<List<TimeSlotViewEntity>> = _slots

    private val db = Firebase.firestore
    private val slotsCollection = db.collection("Slots")

    init {
        slotsCollection.addSnapshotListener { value, e ->
            val slots = ArrayList<TimeSlotViewEntity>()

            for (doc in value!!) {
                val slotId = doc.getString("slot_id")
                val timeStart = doc.getString("time_start")
                val timeEnd = doc.getString("time_end")
                val remainingOrders = doc.getString("remaining_orders")

                if (slotId != null
                    && timeStart != null
                    && timeEnd != null
                    && remainingOrders != null

                ) {
                    if (remainingOrders.toInt() > 0) {
                        // add to list the

                        slots.add(
                            TimeSlotViewEntity(
                                slotId = slotId,
                                timeStart = timeStart,
                                timeEnd = timeEnd,
                                remainingOrders = remainingOrders,
                            )
                        )
                    }
                }

            }
            _slots.value = slots
        }
    }

    fun useSlot(slot: TimeSlotViewEntity) {
        slotsCollection
            .document("slot" + slot.slotId)
            .update(
                mapOf(
                    "remaining_orders" to (slot.remainingOrders.toInt() - 1).toString(),
                    "slot_id" to slot.slotId,
                    "time_end" to slot.timeEnd,
                    "time_start" to slot.timeStart
                )
            )
    }
}