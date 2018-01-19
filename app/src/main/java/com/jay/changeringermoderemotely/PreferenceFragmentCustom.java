package com.jay.changeringermoderemotely;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;

/**
 * Created by jaypatel on 03/11/17.
 * The Preference Fragment which shows the Preferences as a List and handles the Dialogs for the
 * Preferences.
 *
 * @author jaypatel
 */
public class PreferenceFragmentCustom extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        // Load the Preferences from the XML file
        addPreferencesFromResource(R.xml.app_preferences);

        SwitchPreferenceCompat preferenceGroup = (SwitchPreferenceCompat) findPreference("profile_change_mode");
        preferenceGroup.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferences pref = getContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("canChangeProfile", (Boolean) newValue);
                editor.apply();
                return true;
            }
        });
    }
}
