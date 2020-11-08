package com.dp.agro.ui.webcontainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dp.agro.databinding.FragmentWebContainerBinding

class WebContainerFragment : Fragment() {
    private lateinit var binding: FragmentWebContainerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWebContainerBinding.inflate(inflater, container, false)
        return binding.root
    }
}