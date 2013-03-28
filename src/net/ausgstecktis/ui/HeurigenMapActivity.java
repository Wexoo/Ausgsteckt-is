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
import net.ausgstecktis.DAL.ProxyFactory;
import net.ausgstecktis.entities.Heuriger;
import net.ausgstecktis.util.MyItemizedOverlay;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

/**
 * HeurigenMapActivity.java
 * 
 * @author naikon
 * @version Aug 27, 2011
 */
public class HeurigenMapActivity extends SuperMapActivity {

   @SuppressWarnings("unused")
   private static final String TAG = HeurigenMapActivity.class.getSimpleName();

   private MapView mapView;
   private List<Overlay> mapOverlays;
   private MyItemizedOverlay itemizedOverlay;
   private Heuriger heuriger;
   private Drawable drawable;
   private String heurigerNameOverlay;
   private String heurigerZipCodeOverlay;
   private String heurigerCityOverlay;
   private String heurigerStreetOverlay;
   private String heurigerStreetNumberOverlay;

   /**
    * {@inheritDoc}
    * 
    * @see net.ausgstecktis.ui.SuperMapActivity#onCreate(android.os.Bundle)
    */
   
   public void onCreate(final Bundle savedInstanceState) {

      super.onCreate(savedInstanceState);
      this.setContentView(R.layout.activity_heurigen_map);

      ((TextView) findViewById(R.id.title_text)).setText(getTitle());

      mapView = (MapView) findViewById(R.id.mapview);
      mapView.setBuiltInZoomControls(true);
      registerForContextMenu(mapView);

      mapOverlays = mapView.getOverlays();

      // get selected Heurigen
      heuriger = ProxyFactory.getProxy().getSelectedHeuriger();

      drawable = getResources().getDrawable(R.drawable.marker);
      itemizedOverlay = new MyItemizedOverlay(drawable, mapView) {

         
         protected void showContextMenu(final int index) {
            HeurigenMapActivity.this.openContextMenu(mapView);
         }
      };

      final GeoPoint point = new GeoPoint((int) (heuriger.getLongitude() * 1E6),
            (int) (heuriger.getLatitude() * 1E6));

      heurigerNameOverlay = heuriger.getName();
      heurigerZipCodeOverlay = heuriger.getCity().getZipCode().toString();
      heurigerCityOverlay = heuriger.getCity().getName();
      heurigerStreetOverlay = heuriger.getStreet();

      if (heuriger.getStreetNumber() != 0) {
         heurigerStreetNumberOverlay = heuriger.getStreetNumber().toString();
      } else {
         heurigerStreetNumberOverlay = "";
      }

      final OverlayItem overlayItem =
            new OverlayItem(point, heurigerNameOverlay, heurigerZipCodeOverlay + " "
                  + heurigerCityOverlay + ", " + heurigerStreetOverlay + " " + heurigerStreetNumberOverlay);
      itemizedOverlay.addOverlay(overlayItem);
      mapOverlays.add(itemizedOverlay);

      // correct position for marker shadow
      final int w = drawable.getIntrinsicWidth();
      final int h = drawable.getIntrinsicHeight();
      drawable.setBounds(-w / 2, -h, w / 2, 0);

      final MapController mc = mapView.getController();
      mc.animateTo(point);
      mc.setZoom(14); // The highest zoom level is 23
   }

   /**
    * {@inheritDoc}
    * 
    * @see net.ausgstecktis.ui.SuperMapActivity#isRouteDisplayed()
    */
   
   protected boolean isRouteDisplayed() {
      return false;
   }
}