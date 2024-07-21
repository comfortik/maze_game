package com.example.bleservice.features.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.bleservice.databinding.FragmentMainBinding
import com.example.bleservice.features.main.adapter.DeviceAdapter
import com.example.bleservice.features.main.presentation.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: DeviceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = DeviceAdapter()
        binding.recyclerView.adapter = adapter
        viewModel.devices.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }

        binding.buttonStart.setOnClickListener {
            viewModel.scanDevices()
        }
    }
}
