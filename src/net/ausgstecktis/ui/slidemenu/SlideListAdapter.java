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
package net.ausgstecktis.ui.slidemenu;

import java.util.ArrayList;

import net.ausgstecktis.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

/**
 * @author wexoo
 */
public class SlideListAdapter extends BaseAdapter {

   private ArrayList<MenuItemBean> menuItems;
   private static LayoutInflater inflater = null;

   public SlideListAdapter(Context context, ArrayList<MenuItemBean> menuItems) {
      this.menuItems = menuItems;

      inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
   }

   @Override
   public int getCount() {
      return menuItems.size();
   }

   @Override
   public Object getItem(int pos) {
      return pos;
   }

   @Override
   public long getItemId(int pos) {
      return pos;
   }

   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
      if (convertView == null)
         convertView = inflater.inflate(R.layout.slide_menu_item, null);

      MenuItemBean menuItem = menuItems.get(position);

      Button menuItemIcon = (Button) convertView.findViewById(R.id.ib_slide_menu_item_icon);
      menuItemIcon.setCompoundDrawablesWithIntrinsicBounds(menuItem.getImageDrawable(), 0, 0, 0);
      menuItemIcon.setText(menuItem.getTitle());
      menuItemIcon.setTag(menuItem.getTagId());

      return convertView;
   }
}
