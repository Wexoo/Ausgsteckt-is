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

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import net.ausgstecktis.R;
import net.ausgstecktis.DAL.MigrationProxy;
import net.ausgstecktis.DAL.ProxyFactory;
import net.ausgstecktis.util.AbstractAlertDialogBuilder;
import net.ausgstecktis.util.AbstractAsyncTask;
import net.ausgstecktis.util.Log;
import net.ausgstecktis.util.UIUtils;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.WindowManager;

/**
 * SplashScreenActivity.java
 * 
 * @author wexoo
 */
public class SplashScreenActivity extends SuperActivity {

   private static final String TAG = SplashScreenActivity.class.getSimpleName();

   private static final int UPDATE_AVAILABLE = 1;
   private static final int NO_UPDATE_AVAILABLE = 0;
   private static final String INITIAL_DATABASE_DATE = "2011-12-31";

   private OnlineCheckAsyncTask onlineCheckAsyncTask;
   private MigrationAsyncTask migrationAsyncTask;
   private ProgressDialog migrationProgressDialog;

   private static Handler splashHandler;

   private Map<String, Integer> updateInfo;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      setContentView(R.layout.activity_splashscreen);
      HeurigenApp.mainContext = this;
      super.onCreate(savedInstanceState);

      buildSplashHandler();

      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

      if (PreferencesActivity.getStringPreference(R.string.last_database_migration_key, null).equals("")) {
         ProxyFactory.getProxy();

         migrationAsyncTask = new MigrationAsyncTask(true);
         migrationAsyncTask.execute();
      } else {
         onlineCheckAsyncTask = new OnlineCheckAsyncTask();
         onlineCheckAsyncTask.execute();
      }
   }

   private void buildSplashHandler() {
      splashHandler = new Handler() {

         @Override
         public void handleMessage(Message msg) {
            switch (msg.what){
               case UPDATE_AVAILABLE:

                  migrationProgressDialog.dismiss();

                  AlertDialog userCheckDialog = new AbstractAlertDialogBuilder(
                        SplashScreenActivity.this, buildUpdateAlertMessage()) {

                     @Override
                     protected void positiveButtonAction(Context context) {
                        migrationAsyncTask = new MigrationAsyncTask(false);
                        migrationAsyncTask.execute();
                     }

                     @Override
                     protected void negativeButtonAction(Context context) {
                        setLastDataUpdateReminderPreference();
                        UIUtils.showLongToast(getString(R.string.toast_new_update_declined));
                        switchToHomeActivity();
                     }
                  }.create();
                  userCheckDialog.getWindow().setGravity(Gravity.BOTTOM);
                  userCheckDialog.show();
                  break;

               case NO_UPDATE_AVAILABLE:
                  migrationProgressDialog.dismiss();

                  switchToHomeActivity();
                  break;
            }
            super.handleMessage(msg);
         }
      };
   }

   private String buildUpdateAlertMessage() {
      return getString(R.string.alert_new_updates_available,
            updateInfo.get(MigrationProxy.CREATED_HEURIGE_KEY),
            updateInfo.get(MigrationProxy.UPDATE_HEURIGE_KEY),
            updateInfo.get(MigrationProxy.CREATED_CALENDAR_KEY),
            updateInfo.get(MigrationProxy.UPDATED_CALENDAR_KEY));
   }

   private void setLastDataUpdateReminderPreference() {
      setLastDataUpdateReminderPreference(new Date());
   }

   private void setLastDataUpdateReminderPreference(Date lastReminderDate) {

      PreferencesActivity.setStringPreference(R.string.key_last_data_update_reminder,
            null, UIUtils.getDateAsString(lastReminderDate));
      Log.d(TAG, "last data update reminder set to: " + UIUtils.getDateAsString(lastReminderDate));
   }

   private void showProgressDialog(int stringResId) {
      migrationProgressDialog = ProgressDialog.show(this, "", getString(stringResId));
      migrationProgressDialog.getWindow().setGravity(Gravity.BOTTOM);
      migrationProgressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
   }

   /*
    * private float calulateDaysSinceLastDataUpdateReminder() {
    * String lastReminderDateString = PreferencesActivity.getStringPreference(R.string.key_last_data_update_reminder,
    * null);
    * if (!lastReminderDateString.equals("")) {
    * Log.d(TAG, "last dataupdate reminder date from preference: " + lastReminderDateString);
    * Date lastReminderDate = UIUtils.getStringAsDate(lastReminderDateString, UIUtils.DEFAULT_DATE_FORMATTER);
    * float resultDays = ((new Date().getTime() - lastReminderDate.getTime()) / (1000 * 60 * 60 * 24));
    * Log.d(TAG, "millisec since last dataupdate reminder: " + (new Date().getTime() - lastReminderDate.getTime()));
    * Log.d(TAG, "days since last dataupdate reminder: " + resultDays);
    * return resultDays;
    * }
    * return 0f;
    * }
    */

   private void updateDatabase(boolean migrateLocalDatabase) {
      HeurigenApp.CHECK_IN_PROGRESS = false;
      Log.d(TAG, "Check in progress set false!");

      HeurigenApp.MIGRATION_IN_PROGRESS = true;
      Log.d(TAG, getResources().getString(R.string.toast_migration_started));

      final Date start = new Date();

      if (migrateLocalDatabase)
         MigrationProxy.getInstance(this).createDataBase(migrateLocalDatabase);
      //         Log.d(TAG, "Migration temporarily disabled, see SplashScreenActivity (line 174) for details!");
      else {
         MigrationProxy.getInstance(this).getNewAndUpdatedCities();
         MigrationProxy.getInstance(this).getNewAndUpdatedHeurige();
         MigrationProxy.getInstance(this).getNewAndUpdatedCalendarData();
      }
      PreferencesActivity.setStringPreference(R.string.database_migration_duration_key, null,
            MigrationProxy.calculateElapsedTime(start));
   }

   private void sendMessageToHandler(int messageWhat) {
      Message updateAvailableMessage = new Message();
      updateAvailableMessage.what = messageWhat;
      splashHandler.sendMessage(updateAvailableMessage);
   }

   private void switchToHomeActivity() {
      Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
      startActivity(intent);
      finish();
   }

   private class OnlineCheckAsyncTask extends AbstractAsyncTask {

      private final String TAG = OnlineCheckAsyncTask.class.getSimpleName();

      @Override
      protected void onPreExecute() {
         showProgressDialog(R.string.progress_dialog_check_for_update);
      }

      @Override
      protected Void doInBackground(final Void... params) {

         HeurigenApp.CHECK_IN_PROGRESS = true;

         if (checkIfOnline() && checkIfWebServiceIsAvailable()) {
            Log.d(TAG, "ConnectionInfo -> connection exists");

            //if (HeurigenApp.CHECK_FOR_UPDATE && calulateDaysSinceLastDataUpdateReminder() >= 1.0) {

            updateInfo = MigrationProxy.getInstance().getUpdateInfo();

            boolean updateFound = false;
            for (Integer quantity : updateInfo.values())
               if (quantity > 0)
                  updateFound = true;

            if (!HeurigenApp.MIGRATION_IN_PROGRESS && updateFound) { //!newDatabaseVersion.equals(PreferencesActivity.getStringPreference(R.string.local_database_version_key, null))
               Log.d(TAG, "ConnectionInfo -> database update exists -> update");

               sendMessageToHandler(UPDATE_AVAILABLE); //UPDATE_AVAILABLE

               HeurigenApp.CHECK_FOR_UPDATE = false;
            } else {
               // no new update -> check again in 24h and go to HomeActivity
               setLastDataUpdateReminderPreference();
               sendMessageToHandler(NO_UPDATE_AVAILABLE);
            }
            //} else
            //sendMessageToHandler(NO_UPDATE_AVAILABLE);
         } else {
            switchAccessModeToOfflineIfIsnt();
            sendMessageToHandler(NO_UPDATE_AVAILABLE);
         }
         HeurigenApp.CHECK_IN_PROGRESS = false;
         Log.d(TAG, "Check in progress set false!");
         return null;
      }
   }

   private class MigrationAsyncTask extends AbstractAsyncTask {

      private final String TAG = OnlineCheckAsyncTask.class.getSimpleName();
      private final boolean migrateLocalDatabase;

      public MigrationAsyncTask(boolean migrateLocalDatabase) {
         this.migrateLocalDatabase = migrateLocalDatabase;
      }

      @Override
      protected void onPreExecute() {
         if (migrateLocalDatabase)
            showProgressDialog(R.string.progress_dialog_initial_setup_in_progress);
         else
            showProgressDialog(R.string.progress_dialog_update_in_progress);
      }

      @Override
      protected Void doInBackground(final Void... params) {
         if (migrateLocalDatabase)
            updateDatabase(migrateLocalDatabase);
         else
            updateDatabase(migrateLocalDatabase);
         return null;
      }

      @Override
      protected void onPostExecute(final Void result) {
         if (HeurigenApp.MIGRATION_IN_PROGRESS) {
            HeurigenApp.MIGRATION_IN_PROGRESS = false;

            Log.d(TAG, "Migration finished after "
                  + PreferencesActivity.getStringPreference(R.string.database_migration_duration_key, null));

            PreferencesActivity.setStringPreference(R.string.last_database_migration_key, null,
                  UIUtils.getDateAsString(new Date()));

            //if initial migration finished -> set last reminder update to yesterday to check once for online updates
            if (migrateLocalDatabase) {
               Calendar cal = Calendar.getInstance();
               cal.add(Calendar.DATE, -1);
               setLastDataUpdateReminderPreference(new Date(cal.getTimeInMillis()));
               PreferencesActivity.setStringPreference(R.string.last_database_update_key, null, INITIAL_DATABASE_DATE);
            } else
               PreferencesActivity.setStringPreference(R.string.last_database_update_key, null, INITIAL_DATABASE_DATE);
            //                     DALUtils.DEFAULT_DATE_FORMATTER.format(new Date()));

            checkOnlineStatusAndSwitchModeIfNecessary();
         }

         migrationProgressDialog.dismiss();
         switchToHomeActivity();
      }
   }
}