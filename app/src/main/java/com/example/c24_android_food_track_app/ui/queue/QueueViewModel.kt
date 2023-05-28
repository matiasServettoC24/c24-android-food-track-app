package com.example.c24_android_food_track_app.ui.queue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.c24_android_food_track_app.data.models.FoodTrackOrder
import com.example.c24_android_food_track_app.data.repositories.OrdersRepository
import com.example.c24_android_food_track_app.data.models.Status
import com.example.c24_android_food_track_app.domain.admin.OrderViewEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QueueViewModel : ViewModel() {

    private val repository = OrdersRepository()

    private val _uiState = MutableStateFlow<QueueUiState>(QueueUiState.LoadingQueue)
    val uiState: StateFlow<QueueUiState> = _uiState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            launch { repository.orders.collect(::onOrdersCollected) }
            launch { repository.initOrdersDataBase() }
        }
    }

    fun onOrderReady(orderViewEntity: OrderViewEntity) {
        updateOrderState(orderViewEntity, Status.Ready)
        repository.onOrderReady(orderViewEntity.order)
    }

    fun onOrderPickedUp(orderViewEntity: OrderViewEntity) {
        updateOrderState(orderViewEntity, Status.Picked)
        repository.deliverOrder(orderViewEntity.order)
    }

    fun deleteOrder(orderViewEntity: OrderViewEntity) {
        repository.deleteOrder(orderViewEntity.order)
    }

    private suspend fun onOrdersCollected(orders: List<FoodTrackOrder>) {
        _uiState.emit(QueueUiState.Queue(orders.map { OrderViewEntity(it) }))
    }

    private fun updateOrderState(order: OrderViewEntity, state: Status) {
        val queue = uiState.value as? QueueUiState.Queue ?: return
        val updatedQueue = updateQueueWithOrderState(queue, order, state)
        _uiState.value = queue.copy(orders = updatedQueue)
    }

    private fun updateQueueWithOrderState(
        queue: QueueUiState.Queue,
        order: OrderViewEntity,
        state: Status
    ) = queue.orders.map {
        if (it == order) {
            order.copy(order = order.order.copy(status = state))
        } else {
            it
        }
    }
}