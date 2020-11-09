package com.dp.agro.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dp.agro.SharedPrefSingleton
import com.dp.agro.utils.allPermissionsGranted
import com.dp.agro.utils.changeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPref = (application as SharedPrefSingleton).getSharedPrefs()
        changeTheme(sharedPref.getString("Theme", "SYSTEM_THEME")!!)

        val isFirstStartup = sharedPref.getBoolean("isFirstStartup", true)
        if (isFirstStartup || !allPermissionsGranted()) {
            if (isFirstStartup) sharedPref.edit().putBoolean("isFirstStartup", false).apply()
            startActivity(Intent(this@LauncherActivity, OnBoardingActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this@LauncherActivity, MainActivity::class.java))
            finish()
        }
    }
}