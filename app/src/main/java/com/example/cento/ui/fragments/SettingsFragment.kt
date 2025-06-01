package com.example.cento.ui.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.cento.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}