package com.example.c24_android_food_track_app.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.c24_android_food_track_app.R
import com.example.c24_android_food_track_app.databinding.FragmentLoginBinding
import com.example.c24_android_food_track_app.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            etEmail.setText("matias.servetto@check24.de")
            etPassword.setText("000000000")
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString()
                if (password.isEmpty() && email.isEmpty()) {
                    binding.password.error = "*Required"
                    binding.emailTextField.error = "*Required"
                } else if (email.isEmpty()) {
                    binding.emailTextField.error = "*Required"
                } else if (password.isEmpty()) {
                    binding.password.error = "*Required"
                } else viewModel.login(email, password)

            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.login.collect {
                        when (it) {
                            is Resource.Loading -> {
                                binding.btnLogin.visibility = View.INVISIBLE
                                binding.loginProgressBar.visibility = View.VISIBLE
                            }
                            is Resource.Success -> {
                                binding.loginProgressBar.visibility = View.GONE
                                findNavController().navigate(R.id.action_navigation_home_to_navigation_dashboard)
                            }
                            is Resource.Error -> {
                                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                                binding.btnLogin.visibility = View.VISIBLE
                                binding.loginProgressBar.visibility = View.GONE
                            }
                            else -> Unit
                        }
                    }
                }
            }
        }
    }

}