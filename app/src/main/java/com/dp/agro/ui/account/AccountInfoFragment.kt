package com.dp.agro.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dp.agro.databinding.FragmentAccountInfoBinding

class AccountInfoFragment : Fragment() {
    private lateinit var binding: FragmentAccountInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountInfoBinding.inflate(inflater, container, false)
        return binding.root
    }
}