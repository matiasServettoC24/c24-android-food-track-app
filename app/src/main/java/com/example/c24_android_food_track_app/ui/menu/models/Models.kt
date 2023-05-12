package com.example.c24_android_food_track_app.ui.menu.models

data class FoodTrack(
    val title: String,
    val dishes: List<Dish>,
    val timeSlots: List<TimeSlot>
)

data class Dish(
    val name: String,
    val type: DishType,
)

enum class DishType {
    VEGETARIAN,
    NON_VEGETARIAN,
}

data class TimeSlot(
    val isAvailable: Boolean = true,
    val startTime: String,
    val endTime: String,
)

val mockData = FoodTrack(
    title = "Pizza truck",
    dishes = createDishes(),
    timeSlots = createTimeSlots(),
)

fun createDishes() : List<Dish> {
    return listOf(
        Dish(name = "Pizza1", type = DishType.NON_VEGETARIAN),
        Dish(name = "Pizza2", type = DishType.VEGETARIAN),
        Dish(name = "Pizza3", type = DishType.NON_VEGETARIAN),
        Dish(name = "Pizza4", type = DishType.VEGETARIAN),
        Dish(name = "Pizza5", type = DishType.NON_VEGETARIAN),
        Dish(name = "Pizza6", type = DishType.VEGETARIAN),
    )
}

fun createTimeSlots(): List<TimeSlot> {
    return listOf(
        TimeSlot(startTime = "12:00", endTime = "12:10"),
        TimeSlot(startTime = "12:10", endTime = "12:20"),
        TimeSlot(startTime = "12:30", endTime = "12:40"),
        TimeSlot(startTime = "12:40", endTime = "12:50"),
        TimeSlot(startTime = "12:50", endTime = "12:60"),
    )
}
