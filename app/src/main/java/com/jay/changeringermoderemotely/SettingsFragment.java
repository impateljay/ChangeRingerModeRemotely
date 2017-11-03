package com.jay.changeringermoderemotely;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

/**
 * Created by jaypatel on 03/11/17.
 */

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the Preferences from the XML file
        addPreferencesFromResource(R.xml.app_preferences);
    }
}
