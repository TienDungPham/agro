package com.dp.agro.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.dp.agro.R
import com.dp.agro.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        binding.toolbar.setupWithNavController(findNavController())
        fragmentManager
            ?.beginTransaction()
            ?.replace(R.id.setting_container, SettingPrefFragment())
            ?.commit()
        return binding.root
    }
}