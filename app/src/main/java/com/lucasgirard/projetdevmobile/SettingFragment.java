package com.lucasgirard.projetdevmobile;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

public class SettingFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}