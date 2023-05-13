package com.example.c24_android_food_track_app.data.repositories

import com.example.c24_android_food_track_app.data.models.TimeSlot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs

class SlotsRepository {

    private val formatter = DateTimeFormatter.ofPattern("HH:mm")

    private val _slots = MutableStateFlow<List<TimeSlot>>(listOf())
    val slots: StateFlow<List<TimeSlot>> = _slots

    private val db = Firebase.firestore
    private val slotsCollection = db.collection("Slots")

    val asapSlot get() = slots.value.minBy {
        abs(LocalTime.now().toSecondOfDay() - LocalTime.parse(it.timeStart, formatter).toSecondOfDay())
    }

    init {
        slotsCollection.addSnapshotListener { value, e ->
            val slots = ArrayList<TimeSlot>()

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
                            TimeSlot(
                                slotId = slotId,
                                timeStart = timeStart,
                                timeEnd = timeEnd,
                                remainingOrders = remainingOrders,
                            )
                        )
                    }
                }

            }
            _slots.value = slots.sortedBy {
                abs(LocalTime.now().toSecondOfDay() - LocalTime.parse(it.timeStart, formatter).toSecondOfDay())
            }
        }
    }

    fun useSlot(slot: TimeSlot) {
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