package br.ufpe.cin.if1001.rss.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import java.util.List;

import br.ufpe.cin.if1001.rss.R;

public class ConfigActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = spf.edit();

        ed.putString()
        String storeTime; //= spf.getString(getString(R.string.key_list_preference),"30 min");




    }

    public static class ConfigFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.config);
        }
    }
}
