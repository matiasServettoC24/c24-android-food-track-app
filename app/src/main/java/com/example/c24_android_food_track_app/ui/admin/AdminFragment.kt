package com.example.c24_android_food_track_app.ui.admin

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class AdminFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private var _adapter: AdminAdapter? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val adapter get() = _adapter!!

    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel =
            ViewModelProvider(this).get(AdminViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        _adapter = AdminAdapter(viewModel::onOrderReady)
        binding.recyclerView.adapter = adapter

        viewModel.viewEntities.observe(viewLifecycleOwner) {
            adapter.items = it
            adapter.notifyDataSetChanged()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loadOrders()
            }
        }

        return root
    }

    private fun getOrders() {
        db.collection("Users")
            .whereEqualTo("status", "ordered")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w("test_tag_admin_frag", "Listen failed.", e)
                    return@addSnapshotListener
                }
                val emails = ArrayList<String>()
                for (doc in value!!) {
                    doc.getString("email")?.let {
                        emails.add(it)
                    }
                }
                Log.d("test_tag_admin_frag", "Current emails in list: $emails")
            }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _adapter = null
    }
}