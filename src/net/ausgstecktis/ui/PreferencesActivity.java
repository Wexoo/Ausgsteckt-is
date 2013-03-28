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

import net.ausgstecktis.R;
import net.ausgstecktis.DAL.ExportDatabaseFileTask;
import net.ausgstecktis.DAL.ProxyFactory;
import net.ausgstecktis.util.Log;
import net.ausgstecktis.util.UIUtils;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.KeyEvent;

/**
 * The Class PreferencesActivity.
 * 
 * @author wexoo
 * @version Aug 27, 2011
 */
public class PreferencesActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

   /** The Constant TAG. */
   private static final String TAG = PreferencesActivity.class.getSimpleName();

   private static String LP_ACCESS_MODE_KEY;
   private static String P_EXPORT_DB_KEY;

   private ListPreference accessModePreference;
   private static SharedPreferences sharedPrefs;
   private Preference exportDbPreference;

   /**
    * Gets the shared prefs.
    * 
    * @return the shared prefs
    */
   public static SharedPreferences getSharedPrefs() {
      if (PreferencesActivity.sharedPrefs == null)
         PreferencesActivity.sharedPrefs = PreferenceManager.getDefaultSharedPreferences(HeurigenApp.mainContext);
      return PreferencesActivity.sharedPrefs;
   }

   /**
    * {@inheritDoc}
    * 
    * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
    */

   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.preferences);

      PreferencesActivity.LP_ACCESS_MODE_KEY = this.getString(R.string.lp_access_mode_key);
      accessModePreference = (ListPreference) findPreference(PreferencesActivity.LP_ACCESS_MODE_KEY);

      PreferencesActivity.P_EXPORT_DB_KEY = this.getString(R.string.p_export_db_to_sd_key);
      exportDbPreference = findPreference(PreferencesActivity.P_EXPORT_DB_KEY);
      exportDbPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

         @Override
         public boolean onPreferenceClick(final Preference preference) {
            PreferencesActivity.this.exportDatabase();
            return true;
         }
      });
   }

   /**
    * {@inheritDoc}
    * 
    * @see android.app.Activity#onResume()
    */

   @Override
   protected void onResume() {
      super.onResume();

      updateSummary(PreferencesActivity.LP_ACCESS_MODE_KEY);

      PreferencesActivity.getSharedPrefs().registerOnSharedPreferenceChangeListener(this);
   }

   /**
    * {@inheritDoc}
    * 
    * @see android.app.Activity#onPause()
    */

   @Override
   protected void onPause() {
      super.onPause();

      PreferencesActivity.getSharedPrefs().unregisterOnSharedPreferenceChangeListener(this);
   }

   /**
    * Gets the string preference.
    * 
    * @param keyCode the key code
    * @param key the key
    * @return the string preference
    */
   public static String getStringPreference(final Integer keyCode, final String key) {
      return PreferencesActivity.getStringPreference(HeurigenApp.mainContext, keyCode, key);
   }

   /**
    * Gets the string preference.
    * 
    * @param appContext the app context
    * @param keyCode the key code
    * @param key the key
    * @return the string preference
    */
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

   /**
    * Sets the string preference.
    * 
    * @param keyCode the key code
    * @param key the key
    * @param value the value
    */
   public static void setStringPreference(final Integer keyCode, final String key, final String value) {
      PreferencesActivity.setStringPreference(HeurigenApp.mainContext, keyCode, key, value);
   }

   /**
    * Sets the string preference.
    */
   public static void setStringPreference(final Context appContext, final Integer keyCode, String key,
         final String value) {
      if (keyCode != null)
         key = appContext.getString(keyCode);
      final Editor e = PreferencesActivity.getSharedPrefs().edit();
      e.putString(key, value);
      e.commit();
   }

   /**
    * {@inheritDoc}
    * 
    * @see android.content.SharedPreferences.OnSharedPreferenceChangeListener#onSharedPreferenceChanged(android.content.SharedPreferences, java.lang.String)
    */

   @Override
   public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
      if (key.equals(PreferencesActivity.LP_ACCESS_MODE_KEY)) {
         Log.d(PreferencesActivity.TAG, "access mode changed - onSharedPreferenceChanged");
         ProxyFactory.ACCESS_MODE_CHANGED = true;
         updateSummary(PreferencesActivity.LP_ACCESS_MODE_KEY);
      }
   }

   /**
    * Update summary.
    * 
    * @param key the key
    */
   private void updateSummary(final String key) {
      if (key.equals(PreferencesActivity.LP_ACCESS_MODE_KEY))
         accessModePreference.setSummary(getResources().getString(R.string.lp_access_mode_sum)
               + " " + accessModePreference.getEntry().toString());
   }

   /**
    * Export database.
    */
   protected void exportDatabase() {
      if (((HeurigenApp) getApplication()).isExternalStorageAvailable())
         new ExportDatabaseFileTask().execute();
      else
         UIUtils.showShortToast("External storage is not available, unable to export data.");
   }

   /**
    * {@inheritDoc}
    * 
    * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
    */

   @Override
   public boolean onKeyDown(final int keyCode, final KeyEvent event) {
      if ((keyCode == KeyEvent.KEYCODE_BACK)) {
         startActivity(new Intent(this, HomeActivity.class));
         return true;
      }
      return super.onKeyDown(keyCode, event);
   }
}