/***
 * Copyright (C) 2011  naikon, wexoo
 * android@geekosphere.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.ausgstecktis.ui;

import java.util.List;

import net.ausgstecktis.R;
import net.ausgstecktis.DAL.ExportDatabaseFileTask;
import net.ausgstecktis.DAL.ProxyFactory;
import net.ausgstecktis.util.UIUtils;
import net.wexoo.organicdroid.Log;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.View;

/**
 * @author wexoo
 */
@SuppressLint("NewApi")
public class PreferencesActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

   private static final String TAG = PreferencesActivity.class.getSimpleName();

   private static String LP_ACCESS_MODE_KEY;
   //   private static String P_EXPORT_DB_KEY;

   private ListPreference accessModePreference;
   private static SharedPreferences sharedPrefs;

   //   private Preference exportDbPreference;

   public static SharedPreferences getSharedPrefs() {
      if (PreferencesActivity.sharedPrefs == null)
         PreferencesActivity.sharedPrefs = PreferenceManager.getDefaultSharedPreferences(HeurigenApp.mainContext);
      return PreferencesActivity.sharedPrefs;
   }

   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
   }

   @Override
   public void onBuildHeaders(List<Header> target) {
      loadHeadersFromResource(R.xml.preference_headers, target);
   }

   @Override
   protected void onResume() {
      super.onResume();

      PreferencesActivity.getSharedPrefs().registerOnSharedPreferenceChangeListener(this);
   }

   @Override
   protected void onPause() {
      super.onPause();

      PreferencesActivity.getSharedPrefs().unregisterOnSharedPreferenceChangeListener(this);
   }

   /**
    * Basic Preferences
    */
   public static class GeneralPrefs extends PreferenceFragment {
      @Override
      public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);

         // Make sure default values are applied.  In a real app, you would
         // want this in a shared function that is used to retrieve the
         // SharedPreferences wherever they are needed.
         //           PreferenceManager.setDefaultValues(getActivity(),
         //                   R.xml.advanced_preferences, false);

         // Load the preferences from an XML resource
         addPreferencesFromResource(R.xml.general_prefs);
      }
   }

   /**
    * Prefs for TakeMeHome - Feature
    */
   public static class TakeMeHomePrefs extends PreferenceFragment {
      @Override
      public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);

         // Load the preferences from an XML resource
         addPreferencesFromResource(R.xml.take_me_home_prefs);
      }
   }

   /**
    * Credit and links to partners and used frameworks
    */
   public static class PoweredByFragment extends PreferenceFragment {
      @Override
      public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);

         // Load the preferences from an XML resource
         addPreferencesFromResource(R.xml.powered_by_links);
      }
   }

   /**
    * Some stats of the app
    */
   public static class AppInfoFragment extends PreferenceFragment {
      @Override
      public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);

         // Load the preferences from an XML resource
         addPreferencesFromResource(R.xml.app_info);
      }

      @Override
      public void onViewCreated(View view, Bundle savedInstanceState) {
         updateAppInfoViews(this);
      }

      public void updateAppInfoViews(PreferenceFragment fragment) {
         if (!PreferencesActivity.getStringPreference(R.string.last_database_migration_key, null).equals(""))
            fragment.findPreference(getString(R.string.info_latest_update))
                  .setSummary(PreferencesActivity.getStringPreference(R.string.last_database_migration_key, null));
         //         else
         //            ((TextView) findViewById(R.id.tv_local_database_version)).setText(R.string.info_no_database);

         if (!PreferencesActivity.getStringPreference(R.string.database_migration_duration_key, null).equals(""))
            fragment.findPreference(getString(R.string.duration_last_update))
                  .setSummary(
                        PreferencesActivity.getStringPreference(R.string.database_migration_duration_key, null) + " "
                              + getText(R.string.time_entity));
         //         else
         //            ((TextView) findViewById(R.id.tv_duration_last_update)).setText(R.string.info_no_database);
      }

   }

   public static String getStringPreference(final Context appContext, final Integer keyCode) {
      return getStringPreference(appContext, keyCode, null);
   }

   public static String getStringPreference(final Integer keyCode) {
      return getStringPreference(keyCode, null);
   }

   public static String getStringPreference(final Integer keyCode, final String key) {
      return PreferencesActivity.getStringPreference(HeurigenApp.mainContext, keyCode, key);
   }

   public static String getStringPreference(final Context appContext, final Integer keyCode, String key) {
      String value = "";

      try {
         if (keyCode != null)
            key = appContext.getString(keyCode);

         value = PreferencesActivity.getSharedPrefs().getString(key, "");
      } catch (final Exception e) {
         Log.e(PreferencesActivity.TAG, "Couldn't fetch string preference for keys: " + keyCode + " / " + key);
         return "";
      }
      return value;
   }

   public static void setStringPreference(final Integer keyCode, final String key, final String value) {
      PreferencesActivity.setStringPreference(HeurigenApp.mainContext, keyCode, key, value);
   }

   public static void setStringPreference(final Context appContext, final Integer keyCode, String key,
         final String value) {
      if (keyCode != null)
         key = appContext.getString(keyCode);
      final Editor e = PreferencesActivity.getSharedPrefs().edit();
      e.putString(key, value);
      e.commit();
   }

   @Override
   public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
      if (key.equals(PreferencesActivity.LP_ACCESS_MODE_KEY)) {
         Log.d(PreferencesActivity.TAG, "access mode changed - onSharedPreferenceChanged");
         ProxyFactory.ACCESS_MODE_CHANGED = true;
         updateSummary(PreferencesActivity.LP_ACCESS_MODE_KEY);
      }
   }

   private void updateSummary(final String key) {
      if (key.equals(PreferencesActivity.LP_ACCESS_MODE_KEY))
         accessModePreference.setSummary(getResources().getString(R.string.lp_access_mode_sum)
               + " " + accessModePreference.getEntry().toString());
   }

   protected void exportDatabase() {
      if (((HeurigenApp) getApplication()).isExternalStorageAvailable())
         new ExportDatabaseFileTask().execute();
      else
         UIUtils.showShortToast("External storage is not available, unable to export data.");
   }

   //   @Override
   //   public boolean onKeyDown(final int keyCode, final KeyEvent event) {
   //      if ((keyCode == KeyEvent.KEYCODE_BACK)) {
   //         startActivity(new Intent(this, HomeActivity.class));
   //         return true;
   //      }
   //      return super.onKeyDown(keyCode, event);
   //   }
}