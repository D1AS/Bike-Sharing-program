package com.example.bikeshare.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bikeshare.adapters.BicycleAdapter
import com.example.bikeshare.data.Bicycle
import com.example.bikeshare.databinding.FragmentBicycleBinding
import com.example.bikeshare.ui.MainActivity
import com.example.bikeshare.ui.viewmodels.BicycleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class BicycleFragment : Fragment() {

    private var _binding: FragmentBicycleBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<BicycleViewModel>()

    private lateinit var adapter: BicycleAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentBicycleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BicycleAdapter { updatedBicycle ->
            onBicycleStatusChanged(updatedBicycle)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        observeBicycles()
    }

    private fun onBicycleStatusChanged(updatedBicycle: Bicycle) {
        viewModel.updateBicycle(updatedBicycle)
        (activity as? MainActivity)?.updateMapMarker(updatedBicycle)
    }

    private fun observeBicycles() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.bicycles.collect { bicycles ->
                adapter.submitList(bicycles)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
