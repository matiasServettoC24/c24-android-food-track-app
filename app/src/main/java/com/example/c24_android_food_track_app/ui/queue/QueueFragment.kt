package com.example.c24_android_food_track_app.ui.queue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.c24_android_food_track_app.ui.queue.composables.NonAuthErrorView
import com.example.c24_android_food_track_app.ui.queue.composables.QueueErrorView
import com.example.c24_android_food_track_app.ui.queue.composables.QueueView
import com.example.c24_android_food_track_app.ui.shared.composables.LoadingView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class QueueFragment : Fragment() {

    private var _viewModel: QueueViewModel? = null
    private val viewModel get() = _viewModel!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewModel = ViewModelProvider(this)[QueueViewModel::class.java]

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                repeatOnLifecycle()
            }
        }

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                LoadingView(loadingMessage = "Loading Queue...")
            }
        }
    }

    private fun CoroutineScope.repeatOnLifecycle() {
        launch { viewModel.uiState.collectLatest(::onUiStateChanged) }
    }

    private fun onUiStateChanged(uiState: QueueUiState) = when (uiState) {
        QueueUiState.ErrorLoadingQueue -> showErrorViews()
        QueueUiState.ErrorNonAuthorized -> showNonAuthErrorViews()
        QueueUiState.LoadingQueue -> showLoadingViews()
        is QueueUiState.Queue -> showQueueViews(uiState)
    }

    private fun showErrorViews() {
        updateContent { QueueErrorView() }
    }

    private fun showNonAuthErrorViews() {
        updateContent { NonAuthErrorView() }
    }

    private fun showLoadingViews() {
        updateContent { LoadingView(loadingMessage = "Loading Queue...") }
    }

    private fun showQueueViews(uiState: QueueUiState.Queue) {
        updateContent {
            QueueView(
                uiState.orders,
                viewModel::onOrderReady,
                viewModel::onOrderPickedUp,
                viewModel::deleteOrder
            )
        }
    }

    private fun updateContent(content: @Composable () -> Unit) {
        val composeView = view as? ComposeView ?: return
        composeView.setContent {
            MaterialTheme {
                content()
            }
        }
    }
}