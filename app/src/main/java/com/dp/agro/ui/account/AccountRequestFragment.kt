package com.dp.agro.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dp.agro.databinding.FragmentAccountRequestBinding

class AccountRequestFragment : Fragment() {
    private lateinit var binding: FragmentAccountRequestBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountRequestBinding.inflate(inflater, container, false)
        return binding.root
    }
}