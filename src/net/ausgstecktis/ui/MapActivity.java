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

import java.util.ArrayList;
import java.util.List;

import net.ausgstecktis.R;
import net.ausgstecktis.DAL.ProxyFactory;
import net.ausgstecktis.adapter.DistrictListAdapter;
import net.ausgstecktis.entities.District;
import net.ausgstecktis.entities.Heuriger;
import net.ausgstecktis.util.UIUtils;
import net.wexoo.organicdroid.concurrency.AbstractAsyncTask;
import net.wexoo.organicdroid.ui.map.MyItemizedOverlay;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

/**
 * MapActivity.java
 * 
 * @author wexoo, naikon
 */
public class MapActivity extends SuperMapActivity {

   @SuppressWarnings("unused")
   private static final String TAG = MapActivity.class.getSimpleName();

   private MapView mapView;
   private List<Overlay> mapOverlays;
   private MyItemizedOverlay itemizedOverlay;
   private Drawable drawable;
   private DistrictListAdapter districtAdapter;
   private ListView list;

   /** AsyncTask names */
   private GetHeurigeByDistrictAsyncTask heurigeAsyncTask;
   private FindDistrictsAsyncTask districtsAsyncTask;

   private Button slideHandleButton;
   private SlidingDrawer slidingDrawer;

   private String heurigerNameOverlay;
   private String heurigerZipCodeOverlay;
   private String heurigerCityOverlay;
   private String heurigerStreetOverlay;
   private String heurigerStreetNumberOverlay;

   @Override
   public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      this.setContentView(R.layout.activity_map);
      setProgressBarIndeterminateVisibility(true);

      slideHandleButton = (Button) findViewById(R.id.sd_city_button);
      slidingDrawer = (SlidingDrawer) findViewById(R.id.sd_district_select);

      slidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {

         @Override
         public void onDrawerOpened() {
            slideHandleButton.setBackgroundResource(android.R.drawable.arrow_down_float);
         }
      });

      slidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {

         @Override
         public void onDrawerClosed() {
            slideHandleButton.setBackgroundResource(android.R.drawable.arrow_up_float);
         }
      });

      handleSlidingDrawerApperance();

      //List view for sliding drawer including ClickListener!
      list = (ListView) findViewById(R.id.sd_district_content_lv);
      list.setOnItemClickListener(new OnItemClickListener() {

         @Override
         public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long rowId) {

            PreferencesActivity.setStringPreference(R.string.key_district_name_for_map, null,
                  ((District) parent.getItemAtPosition(position)).getName());

            PreferencesActivity.setStringPreference(R.string.key_district_id_for_map, null,
                  ((District) parent.getItemAtPosition(position)).getId().toString());

            handleSlidingDrawerApperance();

            executeGetHeurigeByDistrictAsyncTask();

         }
      });

      districtsAsyncTask = new FindDistrictsAsyncTask();
      districtsAsyncTask.execute();

      if (!PreferencesActivity.getStringPreference(R.string.key_district_id_for_map, null).equals(""))
         executeGetHeurigeByDistrictAsyncTask();
   }

   /**
    * Execute get heurige by district async task.
    */
   private void executeGetHeurigeByDistrictAsyncTask() {
      cancelAsyncTask(heurigeAsyncTask);

      heurigeAsyncTask = new GetHeurigeByDistrictAsyncTask();
      heurigeAsyncTask.execute();
   }

   /**
    * Update markers.
    */
   private void updateMarkers() {
      mapView = (MapView) findViewById(R.id.mapview);
      registerForContextMenu(mapView);

      if (mapOverlays == null)
         mapOverlays = mapView.getOverlays();
      else
         mapOverlays.clear();

      drawable = getResources().getDrawable(R.drawable.marker);
      itemizedOverlay = new MyItemizedOverlay(drawable, mapView) {

         @Override
         protected void showContextMenu(final int index) {
            ProxyFactory.getProxy().setSelectedHeuriger(ProxyFactory.getProxy().getHeurigenList().get(index));
            MapActivity.this.openContextMenu(mapView);
         }
      };
      GeoPoint point = null;

      final ArrayList<Heuriger> list = ProxyFactory.getProxy().getHeurigenList();
      for (final Heuriger heuriger : list) {

         point = new GeoPoint((int) (heuriger.getLongitude() * 1E6), (int) (heuriger.getLatitude() * 1E6));

         heurigerNameOverlay = heuriger.getName();
         heurigerZipCodeOverlay = heuriger.getCity().getZipCode().toString();
         heurigerCityOverlay = heuriger.getCity().getName();
         heurigerStreetOverlay = heuriger.getStreet();

         if (heuriger.getStreetNumber() != 0)
            heurigerStreetNumberOverlay = heuriger.getStreetNumber().toString();
         else
            heurigerStreetNumberOverlay = "";

         final OverlayItem overlayItem =
               new OverlayItem(point, heurigerNameOverlay, heurigerZipCodeOverlay + " " + heurigerCityOverlay + ", "
                     + heurigerStreetOverlay + " "
                     + heurigerStreetNumberOverlay);
         itemizedOverlay.addOverlay(overlayItem);
      }

      // correct position for marker shadow
      final int w = drawable.getIntrinsicWidth();
      final int h = drawable.getIntrinsicHeight();
      drawable.setBounds(-w / 2, -h, w / 2, 0);

      mapOverlays.add(itemizedOverlay);
      final MapController mc = mapView.getController();

      if (point != null)
         mc.animateTo(point);
      mc.setZoom(13); // The highest zoom level is 23
   }

   @Override
   protected boolean isRouteDisplayed() {
      return false;
   }

   /**
    * Handle sliding drawer apperance.
    */
   private void handleSlidingDrawerApperance() {
      if (!PreferencesActivity.getStringPreference(R.string.key_district_name_for_map, null).equals("")) {

         ((TextView) findViewById(R.id.tv_district_handle)).setText(this.getString(R.string.tv_district) + " "
               + PreferencesActivity.getStringPreference(R.string.key_district_name_for_map, null));

         if (slidingDrawer.isOpened())
            slidingDrawer.close();
      } else
         slidingDrawer.open();
   }

   @Override
   protected void cancelAllAsyncTasksOfActivity() {
      super.cancelAllAsyncTasks(districtsAsyncTask, heurigeAsyncTask);
   }

   public class FindDistrictsAsyncTask extends AbstractAsyncTask {

      private ArrayList<District> districts = new ArrayList<District>();

      @Override
      protected void onPreExecute() {
         setProgressBarIndeterminateVisibility(true);
      }

      @Override
      protected void onPostExecute(final Void result) {

         if (!districts.isEmpty()) {
            districtAdapter = new DistrictListAdapter(MapActivity.this, districts);
            list.setAdapter(districtAdapter);
         } else {
            UIUtils.showShortToast(MapActivity.this.getString(R.string.toast_no_data_reveived));
            finish();
         }

         setProgressBarIndeterminateVisibility(false);
      }

      @Override
      protected Void doInBackground(final Void... params) {
         districts = ProxyFactory.getProxy().getDistrictsWhereHeurigeExist();
         return null;
      }
   }

   public class GetHeurigeByDistrictAsyncTask extends AbstractAsyncTask {

      @Override
      protected void onPreExecute() {
         setProgressBarIndeterminateVisibility(true);
      }

      @Override
      protected void onPostExecute(final Void result) {
         updateMarkers();

         setProgressBarIndeterminateVisibility(false);
      }

      @Override
      protected Void doInBackground(final Void... params) {
         if (!ProxyFactory.getProxy().getHeurigenList().isEmpty())
            ProxyFactory.getProxy().clearHeurigenList();
         ProxyFactory.getProxy().setHeurigeByDistrict(
               Integer.valueOf(PreferencesActivity.getStringPreference(R.string.key_district_id_for_map, null)));
         return null;
      }
   }

   public void onRefreshClick(final View v) {
      setProgressBarIndeterminateVisibility(true);

      new GetHeurigeByDistrictAsyncTask().execute();
   }
}
