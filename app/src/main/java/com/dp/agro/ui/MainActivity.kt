package com.dp.agro.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dp.agro.databinding.ActivityMainBinding
import com.dp.agro.utils.allPermissionsGranted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (allPermissionsGranted()) {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
        } else {
            startActivity(Intent(this, OnBoardingActivity::class.java))
            finish()
        }
    }
}