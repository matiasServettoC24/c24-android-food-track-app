package com.example.c24_android_food_track_app.util

sealed class Resource<T> (
    val data: T? = null,
    val message: String? = null
    ) {
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(message: String): Resource<T>(message = message)
    class Loading<T>: Resource<T>()

    class Init<T>: Resource<T>()
}
