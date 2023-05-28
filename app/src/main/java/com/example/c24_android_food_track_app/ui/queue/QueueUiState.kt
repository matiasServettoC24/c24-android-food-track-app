package com.example.c24_android_food_track_app.ui.queue

import com.example.c24_android_food_track_app.domain.queue.OrderViewEntity

sealed class QueueUiState {

    object LoadingQueue: QueueUiState()

    object ErrorLoadingQueue: QueueUiState()

    object ErrorNonAuthorized: QueueUiState()

    data class Queue(val orders: List<OrderViewEntity>): QueueUiState()
}