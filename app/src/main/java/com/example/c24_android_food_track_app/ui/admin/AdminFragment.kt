package com.example.c24_android_food_track_app.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.c24_android_food_track_app.databinding.FragmentNotificationsBinding
import com.example.c24_android_food_track_app.ui.admin.adapters.AdminAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AdminFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private var _adapter: AdminAdapter? = null
    private var _viewModel: AdminViewModel? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val adapter get() = _adapter!!
    private val viewModel get() = _viewModel!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewModel = ViewModelProvider(this).get(AdminViewModel::class.java)
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        _adapter = AdminAdapter(viewModel::onOrderReady, viewModel::onOrderPickedUp, viewModel::deleteOrder)
        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                repeatOnLifecycle()
            }
        }

        return root
    }

    private fun CoroutineScope.repeatOnLifecycle() {
        launch {
            viewModel.viewEntities.collectLatest {
                adapter.items = it
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _adapter = null
    }
}