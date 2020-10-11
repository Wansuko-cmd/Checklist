package com.wsr.checklist.preference

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.wsr.checklist.R

class PreferenceFragment : PreferenceFragmentCompat(){
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)
    }
}