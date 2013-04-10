/***
 * Copyright (C) 2011 naikon, wexoo android@geekosphere.org
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package net.ausgstecktis.ui;

import net.ausgstecktis.R;
import net.ausgstecktis.ui.search.SearchActivity;
import net.ausgstecktis.util.IntentAlertDialogBuilder;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Displays the dashboard to the users.
 * 
 * @author naikon, wexoo
 * @version Aug 27, 2011
 */
public class HomeActivity extends SuperActivity {

   @SuppressWarnings("unused")
   private static final String TAG = HomeActivity.class.getSimpleName();

   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      setContentView(R.layout.activity_home);
      super.onCreate(savedInstanceState);

      HeurigenApp.mainContext = this;
   }

   public void onIconClick(final View v) {
      switch (v.getId()){

      // Today
         case R.id.home_btn_today:
            startActivity(new Intent(this, TodayActivity.class));
            break;
         // Surroundings
         case R.id.home_btn_surronding:
            if (checkIfOnline())
               startActivity(new Intent(this, SurroundingsActivity.class));
            else
               showMobileNetworkIntent();
            break;
         // Search
         case R.id.home_btn_search:
            startActivity(new Intent(this, SearchActivity.class));
            break;
         // Map
         case R.id.home_btn_map:
            if (checkIfOnline())
               startActivity(new Intent(this, MapActivity.class));
            else
               showMobileNetworkIntent();
            break;
         // Favorites
         case R.id.home_btn_favorite:
            startActivity(new Intent(this, FavoritesActivity.class));
            break;
         // Info
         case R.id.home_btn_info:
            // used to quickly export database to external memory for testing
            //            ExportDatabaseFileTask exportTask = new ExportDatabaseFileTask();
            //            exportTask.execute();
            startActivity(new Intent(this, InfoActivity.class));
            break;
      }
   }

   private void showMobileNetworkIntent() {
      // Prepare mobile network intent
      Intent mobileNetworkIntent = new Intent(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS);

      ComponentName cName = new ComponentName("com.android.phone", "com.android.phone.Settings");

      // Build and show AlertDialog
      new IntentAlertDialogBuilder(
            this,
            mobileNetworkIntent.setComponent(cName),
            new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS),
            null, R.string.alert_no_local_data,
            R.string.alert_mobile_network_prefs,
            R.string.alert_wifi_prefs, R.string.alert_return, true)
            .showAlertDialog();
   }

   /**
    * Suspend app.
    */
   private void suspendApp() {
      moveTaskToBack(true);
   }

   @Override
   public void onBackPressed() {
      suspendApp();
      super.onBackPressed();
   }
}