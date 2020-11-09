package com.dp.agro.ui.setting

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.CheckBoxPreference
import androidx.preference.PreferenceFragmentCompat
import com.dp.agro.R
import com.dp.agro.SharedPrefSingleton
import com.dp.agro.utils.changeTheme

class SettingPrefFragment : PreferenceFragmentCompat() {
    private lateinit var themePrefs: List<CheckBoxPreference>
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting_screen, rootKey)
        sharedPrefs = (requireActivity().application as SharedPrefSingleton).getSharedPrefs()

        themePrefs = listOf(
            findPreference<CheckBoxPreference>("theme_system")!!.also {
                it.setOnPreferenceClickListener { clickedPref ->
                    changeThemeSetting("SYSTEM_THEME", (clickedPref as CheckBoxPreference))
                    true
                }
            },
            findPreference<CheckBoxPreference>("theme_light")!!.also {
                it.setOnPreferenceClickListener { clickedPref ->
                    changeThemeSetting("LIGHT_THEME", (clickedPref as CheckBoxPreference))
                    true
                }
            },
            findPreference<CheckBoxPreference>("theme_dark")!!.also {
                it.setOnPreferenceClickListener { clickedPref ->
                    changeThemeSetting("DARK_THEME", (clickedPref as CheckBoxPreference))
                    true
                }
            }
        )

        when (sharedPrefs.getString("Theme", "SYSTEM_THEME")!!) {
            "SYSTEM_THEME" -> themePrefs[0].isChecked = true
            "LIGHT_THEME" -> themePrefs[1].isChecked = true
            "DARK_THEME" -> themePrefs[2].isChecked = true
        }
    }

    private fun changeThemeSetting(theme: String, clickedPref: CheckBoxPreference) {
        themePrefs.forEach { themePref -> themePref.isChecked = false }
        clickedPref.isChecked = true
        sharedPrefs.edit().putString("Theme", theme).apply()
        changeTheme(theme)
    }
}