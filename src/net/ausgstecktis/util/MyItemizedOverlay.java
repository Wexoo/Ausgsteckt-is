/***
 * Copyright (C) 2011  naikon, wexoo
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package net.ausgstecktis.util;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

/**
 * The Class MyItemizedOverlay.
 * 
 * @author naikon
 * @version Aug 27, 2011
 */
public abstract class MyItemizedOverlay extends BalloonItemizedOverlay<OverlayItem> {

   /** The m_overlays. */
   private final ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

   /** The c. */
   @SuppressWarnings("unused")
   private final Context c;

   /**
    * Instantiates a new my itemized overlay.
    * 
    * @param defaultMarker the default marker
    * @param mapView the map view
    */
   public MyItemizedOverlay(final Drawable defaultMarker, final MapView mapView) {
      super(ItemizedOverlay.boundCenter(defaultMarker), mapView);
      this.c = mapView.getContext();
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
    * @see net.ausgstecktis.util.BalloonItemizedOverlay#onBalloonTap(int, com.google.android.maps.OverlayItem)
    */
   
   protected boolean onBalloonTap(final int index, final OverlayItem item) {
      this.showContextMenu(index);
      return true;
   }

   /**
    * Show context menu.
    * 
    * @param index the index
    */
   protected abstract void showContextMenu(int index);
}