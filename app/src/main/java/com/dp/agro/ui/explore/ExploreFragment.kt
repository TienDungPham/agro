package com.dp.agro.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dp.agro.databinding.FragmentExploreBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreFragment : Fragment() {
    private lateinit var binding: FragmentExploreBinding
    private val viewModel: ExploreViewModel by viewModels()

    private lateinit var adapter: ExploreAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExploreBinding.inflate(inflater, container, false)
        binding.toolbar.setupWithNavController(findNavController())
        adapter = ExploreAdapter {
            findNavController().navigate(ExploreFragmentDirections.actionExloreDesToScanResultDes(it))
        }
        binding.recyclerExplore.adapter = adapter
        binding.recyclerExplore.layoutManager = LinearLayoutManager(requireContext())

        viewModel.diseases.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.findAllDiseases()
        return binding.root
    }
}