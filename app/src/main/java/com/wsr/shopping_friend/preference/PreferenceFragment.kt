package com.wsr.shopping_friend.preference

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.wsr.shopping_friend.R

class PreferenceFragment : PreferenceFragmentCompat(){
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)
    }
}