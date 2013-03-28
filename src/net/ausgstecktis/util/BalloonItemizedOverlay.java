/***
 * Copyright (c) 2010 readyState Software Ltd
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

import java.util.List;

import net.ausgstecktis.R;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

/**
 * An abstract extension of ItemizedOverlay for displaying an information
 * balloon upon screen-tap of each marker overlay.
 * 
 * @author Jeff Gilfelt
 */

public abstract class BalloonItemizedOverlay<Item extends OverlayItem> extends ItemizedOverlay<Item> {

   private final MapView mapView;
   private View clickRegion;
   private BalloonOverlayView<Item> balloonView;
   private final MapController mc;

   private int viewOffset;
   private Item currentFocussedItem;
   private int currentFocussedIndex;

   /**
    * Create a new BalloonItemizedOverlay
    * 
    * @param defaultMarker
    *        - A bounded Drawable to be drawn on the map for each item in
    *        the overlay.
    * @param mapView
    *        - The view upon which the overlay items are to be drawn.
    */
   public BalloonItemizedOverlay(final Drawable defaultMarker, final MapView mapView) {
      super(defaultMarker);
      this.mapView = mapView;
      this.viewOffset = 0;
      this.mc = mapView.getController();
   }

   /**
    * Set the horizontal distance between the marker and the bottom of the
    * information balloon. The default is 0 which works well for center bounded
    * markers. If your marker is center-bottom bounded, call this before adding
    * overlay items to ensure the balloon hovers exactly above the marker.
    * 
    * @param pixels
    *        - The padding between the center point and the bottom of the
    *        information balloon.
    */
   public void setBalloonBottomOffset(final int pixels) {
      this.viewOffset = pixels;
   }

   /**
    * Gets the balloon bottom offset.
    * 
    * @return the balloon bottom offset
    */
   public int getBalloonBottomOffset() {
      return this.viewOffset;
   }

   /**
    * Override this method to handle a "tap" on a balloon. By default, does
    * nothing and returns false.
    * 
    * @param index
    *        - The index of the item whose balloon is tapped.
    * @param item
    *        - The item whose balloon is tapped.
    * @return true if you handled the tap, otherwise false.
    */
   protected boolean onBalloonTap(final int index, final Item item) {
      return false;
   }

   /*
    * (non-Javadoc)
    * @see com.google.android.maps.ItemizedOverlay#onTap(int)
    */

   @Override
   protected final boolean onTap(final int index) {

      this.currentFocussedIndex = index;
      this.currentFocussedItem = createItem(index);

      boolean isRecycled;
      if (this.balloonView == null) {
         this.balloonView = this.createBalloonOverlayView();
         this.clickRegion = this.balloonView.findViewById(R.id.balloon_inner_layout);
         this.clickRegion.setOnTouchListener(this.createBalloonTouchListener());
         isRecycled = false;
      } else
         isRecycled = true;

      this.balloonView.setVisibility(View.GONE);

      final List<Overlay> mapOverlays = this.mapView.getOverlays();
      if (mapOverlays.size() > 1)
         this.hideOtherBalloons(mapOverlays);

      this.balloonView.setData(this.currentFocussedItem);

      final GeoPoint point = this.currentFocussedItem.getPoint();
      final MapView.LayoutParams params = new MapView.LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, point,
            MapView.LayoutParams.BOTTOM_CENTER);
      params.mode = MapView.LayoutParams.MODE_MAP;

      this.balloonView.setVisibility(View.VISIBLE);

      if (isRecycled)
         this.balloonView.setLayoutParams(params);
      else
         this.mapView.addView(this.balloonView, params);

      this.mc.animateTo(point);

      return true;
   }

   /**
    * Creates the balloon view. Override to create a sub-classed view that can
    * populate additional sub-views.
    * 
    * @return the balloon overlay view
    */
   protected BalloonOverlayView<Item> createBalloonOverlayView() {
      return new BalloonOverlayView<Item>(this.getMapView().getContext(), this.getBalloonBottomOffset());
   }

   /**
    * Expose map view to subclasses. Helps with creation of balloon views.
    * 
    * @return the map view
    */
   protected MapView getMapView() {
      return this.mapView;
   }

   /**
    * Sets the visibility of this overlay's balloon view to GONE.
    */
   protected void hideBalloon() {
      if (this.balloonView != null)
         this.balloonView.setVisibility(View.GONE);
   }

   /**
    * Hides the balloon view for any other BalloonItemizedOverlay instances
    * that might be present on the MapView.
    * 
    * @param overlays
    *        - list of overlays (including this) on the MapView.
    */
   private void hideOtherBalloons(final List<Overlay> overlays) {

      for (final Overlay overlay : overlays)
         if (overlay instanceof BalloonItemizedOverlay<?> && overlay != this)
            ((BalloonItemizedOverlay<?>) overlay).hideBalloon();

   }

   /**
    * Sets the onTouchListener for the balloon being displayed, calling the
    * overridden {@link #onBalloonTap} method.
    * 
    * @return the on touch listener
    */
   private OnTouchListener createBalloonTouchListener() {
      return new OnTouchListener() {

         public boolean onTouch(final View v, final MotionEvent event) {

            final View l = ((View) v.getParent()).findViewById(R.id.balloon_main_layout);
            final Drawable d = l.getBackground();

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
               final int[] states = {android.R.attr.state_pressed};
               if (d.setState(states))
                  d.invalidateSelf();
               return true;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
               final int newStates[] = {};
               if (d.setState(newStates))
                  d.invalidateSelf();
               // call overridden method
               BalloonItemizedOverlay.this.onBalloonTap(BalloonItemizedOverlay.this.currentFocussedIndex,
                     BalloonItemizedOverlay.this.currentFocussedItem);
               return true;
            } else
               return false;
         }
      };
   }
}