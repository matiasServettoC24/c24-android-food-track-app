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
import com.example.c24_android_food_track_app.data.models.TimeSlot
import com.example.c24_android_food_track_app.domain.ViewEntity
import com.example.c24_android_food_track_app.domain.menu.MenuViewEntity
import com.example.c24_android_food_track_app.domain.menu.OrderPickedViewEntity
import com.example.c24_android_food_track_app.domain.menu.OrderReadyViewEntity
import com.example.c24_android_food_track_app.domain.menu.WaitingForOrderViewEntity
import com.example.c24_android_food_track_app.ui.menu.composables.LoadingMenuView
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
            is MenuUiState.DishSelection -> showDishes(uiState)
            MenuUiState.Error -> showError()
            MenuUiState.Loading -> showLoading()
            is MenuUiState.TimeSelection -> showTimeSlots(uiState)
            is MenuUiState.OrderReady -> showOrderReady(uiState)
            is MenuUiState.WaitingForOrder -> showWaitingForOrder(uiState)
            is MenuUiState.OrderPicked -> showOrderPickedup(uiState)
        }
    }

    private fun showOrderPickedup(uiState: MenuUiState.OrderPicked) {
        showViewEntities(listOf(OrderPickedViewEntity(uiState.currentOrder.title)))
    }

    private fun showOrderReady(uiState: MenuUiState.OrderReady) {
        showViewEntities(listOf(OrderReadyViewEntity(uiState.currentOrder.title)))
    }

    private fun showWaitingForOrder(uiState: MenuUiState.WaitingForOrder) {
        showViewEntities(listOf(WaitingForOrderViewEntity(
            uiState.currentOrder.title, uiState.currentOrder.slotTime
        )))
    }

    private fun showTimeSlots(uiState: MenuUiState.TimeSelection) {
        showViewEntities(uiState.timeList)
    }

    private fun showViewEntities(viewEntities: List<ViewEntity>) {
//        binding.loading.root.isVisible = false
//        binding.error.root.isVisible = false
//
//        binding.recyclerView.isVisible = true
//        adapter.items = viewEntities
//        adapter.notifyDataSetChanged()
    }

    private fun showLoading() = updateContent { LoadingMenuView() }

    private fun showError() {
//        binding.recyclerView.isVisible = false
//        binding.loading.root.isVisible = false
//
//        binding.error.root.isVisible = true
    }

    private fun showDishes(uiState: MenuUiState.DishSelection) {
        showViewEntities(uiState.dishList)

    }

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