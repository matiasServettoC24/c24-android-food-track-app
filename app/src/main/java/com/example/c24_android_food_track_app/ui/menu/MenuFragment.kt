package com.example.c24_android_food_track_app.ui.menu

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.c24_android_food_track_app.data.models.FoodTrackOrder
import com.example.c24_android_food_track_app.data.models.TimeSlot
import com.example.c24_android_food_track_app.domain.ViewEntity
import com.example.c24_android_food_track_app.domain.menu.DishViewEntity
import com.example.c24_android_food_track_app.ui.menu.composables.LoadingMenuView
import com.example.c24_android_food_track_app.ui.menu.composables.MenuErrorView
import com.example.c24_android_food_track_app.ui.menu.composables.MenuView
import com.example.c24_android_food_track_app.ui.menu.composables.OrderPickedUpView
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
            is MenuUiState.DishSelection -> showMenuViews(uiState.dishes)
            is MenuUiState.TimeSelection -> showTimeSlotsViews(uiState.timeSlots)
            is MenuUiState.WaitingForOrder -> showWaitingForOrderViews(uiState.currentOrder)
            is MenuUiState.OrderReady -> showOrderReadyViews(uiState.currentOrder)
            is MenuUiState.OrderPicked -> showOrderPickedUpViews(uiState.currentOrder)
        }
    }

    private fun showOrderPickedUpViews(currentOrder: FoodTrackOrder) = updateContent {
        OrderPickedUpView(currentOrder)
    }

    private fun showOrderReadyViews(currentOrder: FoodTrackOrder) = updateContent {
        OrderReadyView(currentOrder)
    }

    private fun showWaitingForOrderViews(currentOrder: FoodTrackOrder) = updateContent {
        WaitingForOrderView(currentOrder)
    }

    private fun showTimeSlotsViews(timeSlots: List<ViewEntity>) {
//        showViewEntities(timeSlots)
    }

    private fun showMenuViews(dishes: List<DishViewEntity>) = updateContent {
        MenuView(dishes, ::selectMenuCallback)
    }

    private fun showLoadingViews() = updateContent { LoadingMenuView() }

    private fun showErrorViews() = updateContent { MenuErrorView() }

    private fun selectMenuCallback(selectedMenu: DishViewEntity) {
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