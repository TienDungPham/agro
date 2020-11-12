package com.dp.agro

import android.content.SharedPreferences
import android.os.StrictMode
import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AgroApplication : MultiDexApplication(), SharedPrefSingleton {
    override fun onCreate() {
        //if (BuildConfig.DEBUG) {
        //    enableStrictMode()
        //}
        super.onCreate()
    }

    private fun enableStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .build()
        )
    }

    private var _sharedPrefs: SharedPreferences? = null
    override fun getSharedPrefs(): SharedPreferences {
        synchronized(this) {
            if (_sharedPrefs == null) {
                _sharedPrefs = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
            }
            return _sharedPrefs!!
        }
    }
}

interface SharedPrefSingleton {
    fun getSharedPrefs(): SharedPreferences
}