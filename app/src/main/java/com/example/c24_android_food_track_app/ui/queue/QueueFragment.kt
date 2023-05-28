package com.example.c24_android_food_track_app.ui.queue

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
import com.example.c24_android_food_track_app.domain.LoadingViewEntity
import com.example.c24_android_food_track_app.domain.admin.ErrorViewEntity
import com.example.c24_android_food_track_app.domain.admin.NonAuthAdminViewEntity
import com.example.c24_android_food_track_app.ui.queue.adapters.AdminAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class QueueFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private var _adapter: AdminAdapter? = null
    private var _viewModel: QueueViewModel? = null

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
        _viewModel = ViewModelProvider(this).get(QueueViewModel::class.java)
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
        launch { viewModel.uiState.collectLatest(::onUiStateChanged) }
    }

    private fun onUiStateChanged(uiState: QueueUiState) {
        when (uiState) {
            QueueUiState.ErrorLoadingQueue -> errorViews()
            QueueUiState.ErrorNonAuthorized -> nonAuthErrorViews()
            QueueUiState.LoadingQueue -> showLoadingViews()
            is QueueUiState.Queue -> showQueueViews(uiState)
        }
    }

    private fun errorViews() {
        adapter.items = listOf(ErrorViewEntity)
        adapter.notifyDataSetChanged()
    }

    private fun nonAuthErrorViews() {
        adapter.items = listOf(NonAuthAdminViewEntity)
        adapter.notifyDataSetChanged()
    }

    private fun showLoadingViews() {
        adapter.items = listOf(LoadingViewEntity)
        adapter.notifyDataSetChanged()
    }

    private fun showQueueViews(uiState: QueueUiState.Queue) {
        adapter.items = uiState.orders
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _adapter = null
    }
}