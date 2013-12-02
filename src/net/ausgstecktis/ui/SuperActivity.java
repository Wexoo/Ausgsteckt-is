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
import net.ausgstecktis.DAL.DBHelper;
import net.ausgstecktis.DAL.ProxyFactory;
import net.ausgstecktis.entities.City;
import net.ausgstecktis.entities.District;
import net.ausgstecktis.entities.Heuriger;
import net.ausgstecktis.ui.search.SearchActivity;
import net.ausgstecktis.ui.slidemenu.SlideMenuView;
import net.ausgstecktis.util.UIUtils;
import net.wexoo.organicdroid.Log;
import net.wexoo.organicdroid.base.BaseActivity;
import net.wexoo.organicdroid.base.BaseApplication;
import net.wexoo.organicdroidormlite.base.BaseOrmLiteActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.actionbarsherlock.ActionBarSherlock.OnActionModeFinishedListener;
import com.actionbarsherlock.ActionBarSherlock.OnActionModeStartedListener;
import com.actionbarsherlock.ActionBarSherlock.OnCreatePanelMenuListener;
import com.actionbarsherlock.ActionBarSherlock.OnMenuItemSelectedListener;
import com.actionbarsherlock.ActionBarSherlock.OnPreparePanelListener;

/**
 * SuperActivity.java
 * 
 * @author wexoo
 */
@SuppressLint("InlinedApi")
public class SuperActivity extends BaseOrmLiteActivity<DBHelper> implements OnCreatePanelMenuListener,
			OnPreparePanelListener, OnMenuItemSelectedListener, OnActionModeStartedListener, OnActionModeFinishedListener {
	
	private static final String TAG = SuperActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		checkOnlineStatusAndSwitchModeIfNecessary();
	}
	
	@Override
	public void onHomeClick(final View v) {
		UIUtils.goHome(this);
	}
	
	@Override
	public void onQuickSearchClick(final View v) {
		UIUtils.onQuickSearchClick(this);
	}
	
	@Override
	protected SlideMenuView createSlideMenuView() {
		return new SlideMenuView(this);
	}
	
	/**
	 * Start different actions for the currently selected heurigen.
	 * 
	 * @param idOfElement id of GUI element which is calling the action (eg. mi_show_details)
	 */
	@Override
	protected void doAction(final Integer idOfElement) {
		switch (idOfElement) {
		// Show detail activity
			case R.id.mi_show_details:
				startActivity(new Intent(this, DetailActivity.class));
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
				Log.e(SuperActivity.TAG, ProxyFactory.getProxy().getSelectedHeuriger().getStreet() + " "
							+ ProxyFactory.getProxy().getSelectedHeuriger().getStreetNumber() + " "
							+ ProxyFactory.getProxy().getSelectedHeuriger().getCity().getZipCode() + " "
							+ ProxyFactory.getProxy().getSelectedHeuriger().getCity());
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q="
							+ ProxyFactory.getProxy().getSelectedHeuriger().getStreet() + " "
							+ ProxyFactory.getProxy().getSelectedHeuriger().getStreetNumber() + " "
							+ ProxyFactory.getProxy().getSelectedHeuriger().getCity().getZipCode() + " "
							+ ProxyFactory.getProxy().getSelectedHeuriger().getCity().getName())));
				break;
			// Show surroundings - ONLY WORKS IF STREET VIEW IS INSTALLED
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
				visitWebsite(getString(R.string.tv_visit_us));
				break;
			case R.id.home_btn_donate:
				visitWebsite(getString(R.string.link_paypal_donate));
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
				BaseActivity.newFavorite = !ProxyFactory.getProxy().getSelectedHeuriger().getFavorite();
				ProxyFactory.getProxy().getSelectedHeuriger().setFavorite(BaseActivity.newFavorite);
				ProxyFactory.getProxy().updateFavorite(ProxyFactory.getProxy().getSelectedHeuriger());
				
				if (BaseActivity.newFavorite) {
					UIUtils.showShortToast(BaseApplication.mainContext.getResources().getString(R.string.toast_favorite_added));
				} else {
					UIUtils.showShortToast(BaseApplication.mainContext.getResources().getString(R.string.toast_favorite_removed));
				}
		}
	}
	
	/**
	 * Navigate to different menus - also used from sliding menu
	 */
	@Override
	public void onIconClick(final View v) {
		switch (Integer.parseInt(v.getTag().toString())) {
		
		// Today
			case 1:
				startActivity(new Intent(this, TodayActivity.class));
				break;
			// Surroundings
			case 2:
				if (checkIfOnline()) {
					startActivity(new Intent(this, SurroundingsActivity.class));
				} else {
					showMobileNetworkIntent();
				}
				break;
			// Search
			case 3:
				startActivity(new Intent(this, SearchActivity.class));
				break;
			// Map
			case 4:
				if (checkIfOnline()) {
					startActivity(new Intent(this, MapActivity.class));
				} else {
					showMobileNetworkIntent();
				}
				break;
			// Favorites
			case 5:
				startActivity(new Intent(this, FavoritesActivity.class));
				break;
			// Info
			case 6:
				// used to quickly export database to external memory for testing
				// ExportDatabaseFileTask exportTask = new ExportDatabaseFileTask();
				// exportTask.execute();
				
				// startActivity(new Intent(this, InfoActivity.class));
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q="
							+ PreferencesActivity.getStringPreference(this, R.string.etp_home_street_and_number_key) + " "
							+ PreferencesActivity.getStringPreference(this, R.string.etp_home_zipcode_and_city_key))));
				break;
		}
		super.onIconClick(v);
	}
	
	/**
	 * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu, android.view.View,
	 *      android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenuInfo menuInfo) {
		switch (v.getId()) {
			case R.id.lv_search_results:
				getMenuInflater().inflate(R.menu.context_menu_search_list, menu);
				setSelectedHeuriger((AdapterContextMenuInfo) menuInfo);
				
				if (SuperActivity.checkIfMigrationOrCheckInProgress()) {
					menu.removeItem(R.id.mi_add_to_favorites);
				} else {
					newFavorite = !ProxyFactory.getProxy().getSelectedHeuriger().getFavorite();
					menu.findItem(R.id.mi_add_to_favorites).setTitle(
								getResources()
											.getString(newFavorite ? R.string.mi_add_to_favorites : R.string.mi_remove_from_favorites));
				}
				
				if (v.getId() == R.id.lv_search_results) {
					menu.removeItem(R.id.mi_show_details);
				}
				if (ProxyFactory.getProxy().getSelectedHeuriger().getPhone() == null) {
					menu.removeItem(R.id.mi_call);
				}
				if (ProxyFactory.getProxy().getSelectedHeuriger().getMail().equals("")) {
					menu.removeItem(R.id.mi_send_mail);
				}
				if (ProxyFactory.getProxy().getSelectedHeuriger().getWebsite().equals("")) {
					menu.removeItem(R.id.mi_visit_website);
				}
				break;
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	protected boolean getTestResultOfWebService() {
		return !ProxyFactory.getProxy().getHeurigeByKeyword(Heuriger.KEYWORDS.NEUNTEUFEL).isEmpty();
	}
	
	public static boolean checkIfMigrationOrCheckInProgress() {
		return HeurigenApp.MIGRATION_IN_PROGRESS || HeurigenApp.CHECK_IN_PROGRESS;
	}
	
	protected void setSelectedHeuriger(final AdapterContextMenuInfo info) {
		ProxyFactory.getProxy().setSelectedHeuriger(new Heuriger());
	}
	
	protected void setSelectedCity(final AdapterContextMenuInfo info) {
		ProxyFactory.getProxy().setSelectedCity(new City());
	}
	
	protected void setSelectedDistrict(final AdapterContextMenuInfo info) {
		ProxyFactory.getProxy().setSelectedDistrict(new District());
	}
}