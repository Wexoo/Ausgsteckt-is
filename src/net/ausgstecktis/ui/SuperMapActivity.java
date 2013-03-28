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
import net.ausgstecktis.DAL.ProxyFactory;
import net.ausgstecktis.entities.Heuriger;
import net.ausgstecktis.util.AbstractAsyncTask;
import net.ausgstecktis.util.Log;
import net.ausgstecktis.util.UIUtils;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.google.android.maps.MapActivity;

/**
 * SuperMapActivity.java
 * 
 * @author wexoo, naikon
 * @version Aug 27, 2011
 */
public class SuperMapActivity extends MapActivity {

   private static final String TAG = SuperMapActivity.class.getSimpleName();
   private static boolean newFavorite;

   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      checkOnlineStatusAndSwitchModeIfNecessary();
   }

   public void onHomeClick(final View v) {
      UIUtils.goHome(this);
   }

   public void onQuickSearchClick(final View v) {
      UIUtils.onQuickSearchClick(this);
   }

   public void startAction(final View view) {
      doAction(view.getId());
   }

   /**
    * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
    */
   @Override
   public boolean onContextItemSelected(final MenuItem item) {
      item.getMenuInfo();

      doAction(item.getItemId());
      return super.onContextItemSelected(item);
   }

   /**
    * Start different actions for the currently selected heurigen.
    * 
    * @param idOfElement id of GUI element which is calling the action (eg. mi_show_details)
    */
   private void doAction(final Integer idOfElement) {
      switch (idOfElement){
         // Show detail activity
         case R.id.mi_show_details:
            startActivity(new Intent(SuperMapActivity.this, DetailActivity.class));
            break;
         // Call current Heurigen
         case R.id.mi_call:
         case R.id.tr_phone_row:
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+"
                  + ProxyFactory.getProxy().getSelectedHeuriger().getPhone())));
            break;
         // Show google maps
         case R.id.mi_show_on_map:
         case R.id.tr_city_row:
         case R.id.tr_street_row:
         case R.id.detail_btn_map:
            startActivity(new Intent(this, HeurigenMapActivity.class));
            // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"
            // + ProxyFactory.getProxy().getSelectedHeuriger().getLongitude() +
            // ","
            // + ProxyFactory.getProxy().getSelectedHeuriger().getLatitude() +
            // "?z=16")));
            break;
         // Start Google navigation
         case R.id.mi_start_navigation:
         case R.id.tr_navigation_row:
            Log.e(SuperMapActivity.TAG, ProxyFactory.getProxy().getSelectedHeuriger().getStreet() + " "
                  + ProxyFactory.getProxy().getSelectedHeuriger().getStreetNumber() + " "
                  + ProxyFactory.getProxy().getSelectedHeuriger().getCity().getZipCode() + " "
                  + ProxyFactory.getProxy().getSelectedHeuriger().getCity());
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q="
                  + ProxyFactory.getProxy().getSelectedHeuriger().getStreet() + " "
                  + ProxyFactory.getProxy().getSelectedHeuriger().getStreetNumber() + " "
                  + ProxyFactory.getProxy().getSelectedHeuriger().getCity().getZipCode() + " "
                  + ProxyFactory.getProxy().getSelectedHeuriger().getCity().getName())));
            break;
         // Show surroundings - NOT WORKING CURRENTLY
         // case R.id.mi_show_surroundings:
         // startActivity(new Intent(Intent.ACTION_VIEW,
         // Uri.parse("google.streetview:cbll=" + "40.783182,-73.95921")));
         // break;
         // Visit website
         case R.id.mi_visit_website:
         case R.id.tr_web_row:
            visitWebsite(ProxyFactory.getProxy().getSelectedHeuriger().getWebsite());
            break;
         // Visit our website
         case R.id.tv_visit_us:
         case R.id.tr_visit_us:
            visitWebsite(getText(R.string.tv_visit_us).toString());
            break;
         case R.id.tv_submit_new_heurigen:
            visitWebsite(getText(R.string.tv_submit_new_heurigen).toString());
            break;
         // Send mail
         case R.id.mi_send_mail:
         case R.id.tr_mail_row:
            sendMail(ProxyFactory.getProxy().getSelectedHeuriger().getMail());
            break;
         // Send mail to us
         case R.id.tv_mail_us:
         case R.id.tr_mail_us:
            sendMail(getText(R.string.tv_mail_us).toString());
            break;
         // Switch favorite status
         case R.id.cb_star_button:
         case R.id.mi_add_to_favorites:
            SuperMapActivity.newFavorite = !ProxyFactory.getProxy().getSelectedHeuriger().getFavorite();
            ProxyFactory.getProxy().getSelectedHeuriger().setFavorite(SuperMapActivity.newFavorite);
            ProxyFactory.getProxy().updateFavorite(ProxyFactory.getProxy().getSelectedHeuriger());

            if (SuperMapActivity.newFavorite)
               UIUtils.showShortToast(HeurigenApp.mainContext,
                     HeurigenApp.mainContext.getResources().getString(R.string.toast_favorite_added));
            else
               UIUtils.showShortToast(HeurigenApp.mainContext,
                     HeurigenApp.mainContext.getResources().getString(R.string.toast_favorite_removed));
            break;
      }
   }

   /**
    * Visit website.
    * 
    * @param website the website
    */
   private void visitWebsite(final String website) {
      startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(website)));
   }

   /**
    * Send mail.
    * 
    * @param mailAddress the mail address
    */
   protected void sendMail(final String mailAddress) {
      startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + mailAddress)));
   }

   /**
    * Sets the selected heuriger.
    * 
    * @param info the new selected heuriger
    */
   protected void setSelectedHeuriger(final AdapterContextMenuInfo info) {
      ProxyFactory.getProxy().setSelectedHeuriger(new Heuriger());
   }

   /**
    * {@inheritDoc}
    * 
    * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
    */

   @Override
   public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenuInfo menuInfo) {
      switch (v.getId()){
         case R.id.lv_search_results:
         case R.id.mapview:
            getMenuInflater().inflate(R.menu.context_menu_search_list, menu);
            if (v.getId() == R.id.lv_search_results)
               setSelectedHeuriger((AdapterContextMenuInfo) menuInfo);

            if (SuperActivity.checkIfMigrationOrCheckInProgress())
               menu.removeItem(R.id.mi_add_to_favorites);
            else {
               newFavorite = !ProxyFactory.getProxy().getSelectedHeuriger().getFavorite();
               menu.findItem(R.id.mi_add_to_favorites).setTitle(getResources().getString(newFavorite ?
                     R.string.mi_add_to_favorites : R.string.mi_remove_from_favorites));
            }

            if (v.getId() == R.id.lv_search_results)
               menu.removeItem(R.id.mi_show_details);
            if (ProxyFactory.getProxy().getSelectedHeuriger().getPhone() == null)
               menu.removeItem(R.id.mi_call);
            if (ProxyFactory.getProxy().getSelectedHeuriger().getMail().equals(""))
               menu.removeItem(R.id.mi_send_mail);
            if (ProxyFactory.getProxy().getSelectedHeuriger().getWebsite().equals(""))
               menu.removeItem(R.id.mi_visit_website);
            if (v.getId() == R.id.mapview)
               menu.removeItem(R.id.mi_show_on_map);
            break;
      }
      super.onCreateContextMenu(menu, v, menuInfo);
   }

   protected void checkOnlineStatusAndSwitchModeIfNecessary() {
      if (checkIfOnline())
         switchAccessModeToOnlineIfIsnt();
      else
         switchAccessModeToOfflineIfIsnt();
   }

   protected boolean checkIfOnline() {
      return ((HeurigenApp) getApplication()).isOnline();
   }

   public static boolean checkIfMigrationOrCheckInProgress() {
      return HeurigenApp.MIGRATION_IN_PROGRESS || HeurigenApp.CHECK_IN_PROGRESS;
   }

   protected boolean isAccessModeOnline() {
      return PreferencesActivity.getStringPreference(R.string.lp_access_mode_key, null).equals("online");
   }

   protected boolean isAccessModeOffline() {
      return PreferencesActivity.getStringPreference(R.string.lp_access_mode_key, null).equals("offline");
   }

   protected void switchAccessModeToOnlineIfIsnt() {
      if (!isAccessModeOnline()) {
         Log.d(TAG, "ConnectionInfo -> connection exists -> switch to online mode after onlinecheck");
         PreferencesActivity.setStringPreference(R.string.lp_access_mode_key, null, "online");
         ProxyFactory.ACCESS_MODE_CHANGED = true;
      }
   }

   protected void switchAccessModeToOfflineIfIsnt() {
      if (!isAccessModeOffline()) {
         Log.d(TAG, "ConnectionInfo -> no connection -> switch to offline mode");
         PreferencesActivity.setStringPreference(R.string.lp_access_mode_key, null, "offline");
         ProxyFactory.ACCESS_MODE_CHANGED = true;
      }
   }

   protected void cancelAllAsyncTasksOfActivity() {
   }

   @Override
   public void onBackPressed() {
      cancelAllAsyncTasksOfActivity();
      super.onBackPressed();
   }

   @Override
   protected void onStop() {
      cancelAllAsyncTasksOfActivity();
      super.onStop();
   }

   protected void cancelAllAsyncTasks(AbstractAsyncTask... asyncTasks) {
      for (AbstractAsyncTask asyncTask : asyncTasks)
         cancelAsyncTask(asyncTask);
   }

   protected void cancelAsyncTask(AbstractAsyncTask asyncTask) {
      if (asyncTask != null && !asyncTask.isCancelled())
         asyncTask.cancel(true);
   }

   /**
    * {@inheritDoc}
    * 
    * @see com.google.android.maps.MapActivity#isRouteDisplayed()
    */

   @Override
   protected boolean isRouteDisplayed() {
      return false;
   }
}
