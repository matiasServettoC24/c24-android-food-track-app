package com.example.c24_android_food_track_app.ui.menu

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.c24_android_food_track_app.data.models.FoodTrackOrder
import com.example.c24_android_food_track_app.data.models.TimeSlot
import com.example.c24_android_food_track_app.domain.ViewEntity
import com.example.c24_android_food_track_app.domain.menu.MenuViewEntity
import com.example.c24_android_food_track_app.domain.menu.OrderPickedViewEntity
import com.example.c24_android_food_track_app.ui.menu.composables.LoadingMenuView
import com.example.c24_android_food_track_app.ui.menu.composables.MenuErrorView
import com.example.c24_android_food_track_app.ui.menu.composables.OrderReadyView
import com.example.c24_android_food_track_app.ui.menu.composables.WaitingForOrderView
import com.example.c24_android_food_track_app.util.updateContent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("NewApi")
class MenuFragment : Fragment() {

    private val menuViewModel by viewModels<MenuViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { menuViewModel.uiState.collectLatest(::onUiStateUpdated) }
            }
        }

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                LoadingMenuView()
            }
        }
    }

    private fun onUiStateUpdated(uiState: MenuUiState) {
        when (uiState) {
            MenuUiState.Loading -> showLoadingViews()
            MenuUiState.Error -> showErrorViews()
            is MenuUiState.DishSelection -> showDishes(uiState.dishes)
            is MenuUiState.TimeSelection -> showTimeSlots(uiState.timeSlots)
            is MenuUiState.WaitingForOrder -> showWaitingForOrder(uiState.currentOrder)
            is MenuUiState.OrderReady -> showOrderReady(uiState.currentOrder)
            is MenuUiState.OrderPicked -> showOrderPickedUp(uiState.currentOrder)
        }
    }

    private fun showOrderPickedUp(currentOrder: FoodTrackOrder) {
        showViewEntities(listOf(OrderPickedViewEntity(currentOrder.title)))
    }

    private fun showOrderReady(currentOrder: FoodTrackOrder) = updateContent {
        OrderReadyView(currentOrder)
    }

    private fun showWaitingForOrder(currentOrder: FoodTrackOrder) = updateContent {
        WaitingForOrderView(currentOrder)
    }

    private fun showTimeSlots(timeSlots: List<ViewEntity>) {
        showViewEntities(timeSlots)
    }

    private fun showDishes(dishes: List<ViewEntity>) {
        showViewEntities(dishes)
    }

    private fun showViewEntities(viewEntities: List<ViewEntity>) {
//        binding.loading.root.isVisible = false
//        binding.error.root.isVisible = false
//
//        binding.recyclerView.isVisible = true
//        adapter.items = viewEntities
//        adapter.notifyDataSetChanged()
    }

    private fun showLoadingViews() = updateContent { LoadingMenuView() }

    private fun showErrorViews() = updateContent { MenuErrorView() }

    private fun selectMenuCallback(selectedMenu: MenuViewEntity) {
        viewLifecycleOwner.lifecycleScope.launch {
            menuViewModel.loadTimeSlots(selectedMenu)
        }
    }

    private fun selectTimeCallback(selectedSlot: TimeSlot) {
        viewLifecycleOwner.lifecycleScope.launch {
            menuViewModel.updateSlotAndSendOrder(selectedSlot)
        }
    }

    private fun selectAsap() {
        viewLifecycleOwner.lifecycleScope.launch {
            menuViewModel.asapOrder()
        }
    }
}