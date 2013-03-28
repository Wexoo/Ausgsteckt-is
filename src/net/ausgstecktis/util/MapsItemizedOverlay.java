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

package net.ausgstecktis.util;

import java.util.ArrayList;

import net.ausgstecktis.R;
import android.content.Context;
import android.widget.Toast;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/**
 * The Class MapsItemizedOverlay.
 * 
 * @author wexoo
 * @version Aug 27, 2011
 */
public class MapsItemizedOverlay extends ItemizedOverlay<OverlayItem> {

   /** The m overlays. */
   private final ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

   /** The m context. */
   private final Context mContext;

   /**
    * Instantiates a new maps itemized overlay.
    * 
    * @param context the context
    */
   public MapsItemizedOverlay(final Context context) {
      super(ItemizedOverlay.boundCenterBottom(context.getResources().getDrawable(R.drawable.marker)));
      this.mContext = context;
   }

   /**
    * Adds the overlay.
    * 
    * @param overlay the overlay
    */
   public void addOverlay(final OverlayItem overlay) {
      this.mOverlays.add(overlay);
      this.populate();
   }

   /**
    * {@inheritDoc}
    * 
    * @see com.google.android.maps.ItemizedOverlay#createItem(int)
    */
   
   protected OverlayItem createItem(final int i) {
      return this.mOverlays.get(i);
   }

   /**
    * {@inheritDoc}
    * 
    * @see com.google.android.maps.ItemizedOverlay#size()
    */
   
   public int size() {
      return this.mOverlays.size();
   }

   /**
    * {@inheritDoc}
    * 
    * @see com.google.android.maps.ItemizedOverlay#onTap(int)
    */
   
   protected boolean onTap(final int index) {
      final OverlayItem item = this.mOverlays.get(index);

      // Intent i = new Intent(Intent.ACTION_VIEW,
      // Uri.parse("google.navigation:q=" + item.getPoint().getLatitudeE6() /
      // 1E6 + ","
      // + item.getPoint().getLongitudeE6() / 1E6));
      // mContext.startActivity(i);

      Toast.makeText(this.mContext, item.getTitle() + "\n" + item.getSnippet(), Toast.LENGTH_SHORT).show();

      // AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
      // dialog.setTitle(item.getTitle());
      // dialog.setMessage(item.getSnippet());
      // dialog.show();
      return true;
   }

   /**
    * Provides Google Maps Information in a Toast Message when finger is lifted
    * from touch screen
    */
   // 
   // public boolean onTouchEvent(MotionEvent event, MapView mapView) {
   // // ---when user lifts his finger---
   // if (event.getAction() == MotionEvent.ACTION_UP) {
   // GeoPoint p = mapView.getProjection().fromPixels(
   // (int) event.getX(),
   // (int) event.getY());
   //
   // Geocoder geoCoder = new Geocoder(mContext, Locale.getDefault());
   // try {
   // List<Address> addresses = geoCoder.getFromLocation(
   // p.getLatitudeE6() / 1E6,
   // p.getLongitudeE6() / 1E6, 1);
   //
   // String add = "";
   // if (addresses.size() > 0) {
   // for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++) {
   // add += addresses.get(0).getAddressLine(i) + "\n";
   // }
   // }
   //
   // // Toast.makeText(mContext, add, Toast.LENGTH_SHORT).show();
   // } catch (IOException e) {
   // e.printStackTrace();
   // }
   // return true;
   // } else {
   // return false;
   // }
   // }
}
