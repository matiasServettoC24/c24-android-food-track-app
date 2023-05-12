package com.example.c24_android_food_track_app.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.c24_android_food_track_app.databinding.FragmentMenuBinding
import com.example.c24_android_food_track_app.ui.menu.adapters.MenuAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private var _adapter: MenuAdapter? = null

    private val binding get() = _binding!!
    private val adapter get() = _adapter!!

    private val menuViewModel by viewModels<MenuViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMenuBinding.inflate(inflater, container, false)

        _adapter = MenuAdapter(
            orderCallback = ::orderCallback,
            selectTimeSlotCallback = ::selectTimeCallback,
        )

        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            menuViewModel.loadDishes()
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { menuViewModel.uiState.collectLatest(::onUiStateUpdated) }
            }
        }

        return binding.root
    }

    private fun onUiStateUpdated(uiState: MenuUiState) {
        when (uiState) {
            is MenuUiState.DishSelection -> showDishes(uiState)
            MenuUiState.Error -> showError()
            MenuUiState.Loading -> showLoading()
            is MenuUiState.TimeSelection -> showTimeSlots(uiState)
        }
    }

    private fun showTimeSlots(uiState: MenuUiState.TimeSelection) {
        binding.loading.root.isVisible = false
        binding.error.root.isVisible = false

        binding.recyclerView.isVisible = true
        adapter.items = uiState.timeList
        adapter.notifyDataSetChanged()
    }

    private fun showLoading() {
        binding.recyclerView.isVisible = false
        binding.error.root.isVisible = false

        binding.loading.root.isVisible = true
    }

    private fun showError() {
        binding.recyclerView.isVisible = false
        binding.loading.root.isVisible = false

        binding.error.root.isVisible = true
    }

    private fun showDishes(uiState: MenuUiState.DishSelection) {
        binding.loading.root.isVisible = false
        binding.error.root.isVisible = false

        binding.recyclerView.isVisible = true
        adapter.items = uiState.dishList
        adapter.notifyDataSetChanged()
    }

    private fun orderCallback() {
        viewLifecycleOwner.lifecycleScope.launch {
            menuViewModel.loadTimeSlots()
        }
    }

    private fun selectTimeCallback() {
        viewLifecycleOwner.lifecycleScope.launch {
//            showResultScreen()
            Toast.makeText(context, "selectTimeCallback clicked", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}