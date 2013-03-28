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
import net.ausgstecktis.adapter.SurrondingListAdapter;
import net.ausgstecktis.entities.Heuriger;
import net.ausgstecktis.util.AbstractAsyncTask;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

/**
 * SurroundingsActivity.java
 * 
 * @author naikon
 * @version Aug 27, 2011
 */
public class SurroundingsActivity extends SuperActivity {

   private ListView list;
   private SurrondingListAdapter adapter;
   private ProgressBar progressBar;
   private ImageView refreshIcon;
   private Location lastLocation = null;
   private LocationListener locationListener;
   private LocationManager locationManager;
   private MyGPSListener gpsListener;
   private boolean isGPSFix;
   private long locationMillis;
   private double latitude;
   private double longitude;
   private boolean firstFix = false;

   private AsyncTask<Void, Integer, Void> nearbyHeurigeAsyncTask;

   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      this.setContentView(R.layout.activity_surroundings);
      super.onCreate(savedInstanceState);

      locationListener = new MyLocationListener();
      locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
      locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, HeurigenApp.getConfig()
            .defaultGpsUpdateInterval(), HeurigenApp.getConfig().defaultGpsUpdatedistance(), locationListener);

      lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

      gpsListener = new MyGPSListener();
      locationManager.addGpsStatusListener(gpsListener);

      if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
         createGpsDisabledAlert();

      progressBar = (ProgressBar) findViewById(R.id.title_refresh_progress);
      progressBar.setVisibility(View.VISIBLE);

      refreshIcon = (ImageView) findViewById(R.id.btn_title_refresh);
      refreshIcon.setVisibility(View.GONE);

      list = (ListView) findViewById(R.id.lv_search_results);
      list.setOnItemClickListener(new OnItemClickListener() {

         public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long rowId) {
            ProxyFactory.getProxy().setSelectedHeuriger((Heuriger) parent.getItemAtPosition(position));
            SurroundingsActivity.this.startActivity(new Intent(SurroundingsActivity.this, DetailActivity.class));
         }
      });
      registerForContextMenu(list);
   }

   @Override
   protected void onStop() {
      locationManager.removeUpdates(locationListener);
      super.onStop();
   }

   @Override
   public void onResume() {
      locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, HeurigenApp.getConfig()
            .defaultGpsUpdateInterval(), HeurigenApp.getConfig().defaultGpsUpdatedistance(),
            locationListener);
      super.onResume();
   }

   @Override
   protected void setSelectedHeuriger(final AdapterContextMenuInfo info) {
      ProxyFactory.getProxy().setSelectedHeuriger(adapter.getItem(info.position));
   }

   public void onRefreshClick(final View v) {
      nearbyHeurigeAsyncTask = new GetNearbyHeurigeAsyncTask();
      nearbyHeurigeAsyncTask.execute();

      progressBar = (ProgressBar) findViewById(R.id.title_refresh_progress);
      progressBar.setVisibility(View.VISIBLE);

      refreshIcon = (ImageView) findViewById(R.id.btn_title_refresh);
      refreshIcon.setVisibility(View.GONE);
   }

   /**
    * Creates message dialog if gps is disabled.
    */
   private void createGpsDisabledAlert() {
      final AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setMessage(this.getString(R.string.gps_dialog_message))
            .setCancelable(false)
            .setPositiveButton(this.getString(R.string.gps_dialog_enable),
                  new DialogInterface.OnClickListener() {

                     public void onClick(final DialogInterface dialog, final int id) {
                        SurroundingsActivity.this.showGpsOptions();
                     }
                  });
      builder.setNegativeButton(this.getString(R.string.gps_dialog_disable),
            new DialogInterface.OnClickListener() {

               public void onClick(final DialogInterface dialog, final int id) {
                  dialog.cancel();
                  SurroundingsActivity.this.finish();
               }
            });
      final AlertDialog alert = builder.create();
      alert.show();
   }

   /**
    * Show gps options.
    */
   private void showGpsOptions() {
      final Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
      startActivity(gpsOptionsIntent);
   }

   @Override
   protected void cancelAllAsyncTasksOfActivity() {
      cancelAllAsynTasks(nearbyHeurigeAsyncTask);
   }

   /**
    * The listener interface for receiving myLocation events.
    * The class that is interested in processing a myLocation
    * event implements this interface, and the object created
    * with that class is registered with a component using the
    * component's <code>addMyLocationListener<code> method. When
    * the myLocation event occurs, that object's appropriate
    * method is invoked.
    * 
    * @see MyLocationEvent
    */
   private final class MyLocationListener implements LocationListener {

      public void onLocationChanged(final Location locFromGps) {
         locationMillis = SystemClock.elapsedRealtime();
         latitude = locFromGps.getLatitude();
         longitude = locFromGps.getLongitude();
      }

      public void onStatusChanged(final String provider, final int status, final Bundle extras) {

         /** called when the status of the GPS provider changes **/
         switch (status){
            case LocationProvider.OUT_OF_SERVICE:
               if (lastLocation == null || lastLocation.getProvider().equals(provider))
                  // statusString = "No Service";
                  // Toast.makeText(getApplicationContext(),"no service",Toast.LENGTH_SHORT).show();
                  lastLocation = null;
               break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
               if (lastLocation == null || lastLocation.getProvider().equals(provider)) {
                  // statusString = "no fix";
                  // Toast.makeText(getApplicationContext(),"no fix",Toast.LENGTH_SHORT).show();
               }
               break;
            case LocationProvider.AVAILABLE:
               // statusString = "fix";
               // Toast.makeText(getApplicationContext(),"fix",Toast.LENGTH_SHORT).show();
               break;
         }
      }

      public void onProviderEnabled(String provider) {
      }

      public void onProviderDisabled(String provider) {
      }
   }

   /**
    * MyGPSListener.java
    * This class is waiting for the gps fix.
    * If a gps fix is available the background task will be executed and calls the rest api
    * 
    * @author naikon
    */
   private class MyGPSListener implements GpsStatus.Listener {

      public void onGpsStatusChanged(final int event) {
         switch (event){
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
               if (lastLocation != null)
                  isGPSFix = (SystemClock.elapsedRealtime() - locationMillis) < HeurigenApp.getConfig()
                        .defaultGpsUpdateInterval();

               if (isGPSFix) { // A fix has been acquired.
                  // Do something.
               } else { // The fix has been lost.
                  // Do something.
               }
               break;
            case GpsStatus.GPS_EVENT_FIRST_FIX:
               isGPSFix = true;
               if (!firstFix) {
                  Handler handler = new Handler();
                  handler.postDelayed(new Runnable() {

                     public void run() {
                        nearbyHeurigeAsyncTask = new GetNearbyHeurigeAsyncTask();
                        nearbyHeurigeAsyncTask.execute();
                     }
                  }, 2000);
                  firstFix = true;
               }
               break;
         }
      }
   }

   public class GetNearbyHeurigeAsyncTask extends AbstractAsyncTask {

      @Override
      protected void onPostExecute(final Void result) {
         adapter = new SurrondingListAdapter(SurroundingsActivity.this);
         list.setAdapter(adapter);

         progressBar.setVisibility(View.GONE);
         refreshIcon.setVisibility(View.VISIBLE);
         firstFix = true;
      }

      @Override
      protected Void doInBackground(final Void... params) {
         ProxyFactory.getProxy().setNearbyHeurige(latitude, longitude);
         return null;
      }
   }
}