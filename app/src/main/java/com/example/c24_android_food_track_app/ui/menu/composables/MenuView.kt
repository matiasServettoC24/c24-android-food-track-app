package com.example.c24_android_food_track_app.ui.menu.composables

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.c24_android_food_track_app.domain.menu.DishViewEntity
import com.example.c24_android_food_track_app.util.PREVIEW_HEIGHT_DP
import com.example.c24_android_food_track_app.util.PREVIEW_WIDTH_DP

@Composable
fun MenuView(
    dishes: List<DishViewEntity>,
    selectDishSelectedCallback: (DishViewEntity) -> Unit
) = LazyColumn {
    item { Text(text = "Select your meal:") }
    items(dishes) { dish ->
        Text(text = dish.dishTitle)
    }
}

@Preview(widthDp = PREVIEW_WIDTH_DP, heightDp = PREVIEW_HEIGHT_DP, showBackground = true)
@Composable
fun EmptyMenuViewPreview() = MenuView(emptyList()) {}

@Preview(widthDp = PREVIEW_WIDTH_DP, heightDp = PREVIEW_HEIGHT_DP, showBackground = true)
@Composable
fun MenuViewPreview() = MenuView(
    listOf(
        DishViewEntity(dishTitle = "Pizza Margarita", dishType = "Veggie"),
        DishViewEntity(dishTitle = "Pizza Quattro formaggi", dishType = "Veggie"),
        DishViewEntity(dishTitle = "Pizza Pepperoni", dishType = "Meat"),
    )
) {}
