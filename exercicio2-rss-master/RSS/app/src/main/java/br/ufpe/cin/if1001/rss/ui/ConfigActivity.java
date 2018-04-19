package br.ufpe.cin.if1001.rss.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.List;

import br.ufpe.cin.if1001.rss.R;

public class ConfigActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);


       //getFragmentManager().beginTransaction().replace(android.R.id.content, new ConfigFragment()).commit();

    }

    public static class ConfigFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.config);
/*
            Preference submitPref = (Preference) findPreference("submitPref");
            submitPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Log.d("ListPref", "Resultado da seleção do list preference: " + preference.getKey());
                    getActivity().finish();
                    return true;
                }
            });
  */      }
    }

}
