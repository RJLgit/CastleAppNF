package com.example.android.castleappnf;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
//Settings Fragment class which defines the relevent XML file for the settings
public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_castles);
    }
}
