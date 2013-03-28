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

import net.ausgstecktis.R;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.maps.OverlayItem;

/**
 * A view representing a MapView marker information balloon.
 * <p>
 * This class has a number of Android resource dependencies:
 * <ul>
 * <li>drawable/balloon_overlay_bg_selector.xml</li>
 * <li>drawable/balloon_overlay_close.png</li>
 * <li>drawable/balloon_overlay_focused.9.png</li>
 * <li>drawable/balloon_overlay_unfocused.9.png</li>
 * <li>layout/balloon_map_overlay.xml</li>
 * </ul>
 * </p>
 * 
 * @author Jeff Gilfelt
 */
public class BalloonOverlayView<Item extends OverlayItem> extends FrameLayout {

   /** The layout. */
   private final LinearLayout layout;

   /** The title. */
   private final TextView title;

   /** The snippet. */
   private final TextView snippet;

   /**
    * Create a new BalloonOverlayView.
    * 
    * @param context
    *        - The activity context.
    * @param balloonBottomOffset
    *        - The bottom padding (in pixels) to be applied when rendering
    *        this view.
    */
   public BalloonOverlayView(final Context context, final int balloonBottomOffset) {

      super(context);

      this.setPadding(10, 0, 10, balloonBottomOffset);
      this.layout = new LinearLayout(context);
      this.layout.setVisibility(View.VISIBLE);

      final LayoutInflater inflater = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      final View v = inflater.inflate(R.layout.balloon_overlay, this.layout);
      this.title = (TextView) v.findViewById(R.id.balloon_item_title);
      this.snippet = (TextView) v.findViewById(R.id.balloon_item_snippet);

      final ImageView close = (ImageView) v.findViewById(R.id.close_img_button);
      close.setOnClickListener(new OnClickListener() {

         
         public void onClick(final View v) {
            BalloonOverlayView.this.layout.setVisibility(View.GONE);
         }
      });

      final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
      params.gravity = Gravity.NO_GRAVITY;

      this.addView(this.layout, params);

   }

   /**
    * Sets the view data from a given overlay item.
    * 
    * @param item
    *        - The overlay item containing the relevant view data (title
    *        and snippet).
    */
   public void setData(final Item item) {

      this.layout.setVisibility(View.VISIBLE);
      if (item.getTitle() != null) {
         this.title.setVisibility(View.VISIBLE);
         this.title.setText(item.getTitle());
      } else {
         this.title.setVisibility(View.GONE);
      }
      if (item.getSnippet() != null) {
         this.snippet.setVisibility(View.VISIBLE);
         this.snippet.setText(item.getSnippet());
      } else {
         this.snippet.setVisibility(View.GONE);
      }

   }

}
