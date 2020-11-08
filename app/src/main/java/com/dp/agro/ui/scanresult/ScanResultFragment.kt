package com.dp.agro.ui.scanresult

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dp.agro.databinding.FragmentScanResultBinding

class ScanResultFragment : Fragment() {
    private lateinit var binding: FragmentScanResultBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScanResultBinding.inflate(inflater, container, false)
        return binding.root
    }
}