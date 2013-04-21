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
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * Sets the list view for the SlideMenu
 * 
 * @author wexoo
 */
public class SlideMenuView extends RelativeLayout {

   public SlideMenuView(final Context context) {
      super(context);

      RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            android.view.ViewGroup.LayoutParams.MATCH_PARENT);

      setLayoutParams(rlp);

      ListView listView = new ListView(context);

      // Defining the layout parameters of the ListView
      RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

      // Setting the parameters on the TextView
      listView.setLayoutParams(lp);

      // Adding the TextView to the RelativeLayout as a child
      addView(listView);

      listView.setAdapter(new SlideListAdapter(context, buildMenuItemList()));
   }

   private ArrayList<MenuItemBean> buildMenuItemList() {
      ArrayList<MenuItemBean> menuItems = new ArrayList<MenuItemBean>();
      menuItems.add(new MenuItemBean(R.drawable.home_btn_today, R.string.title_today, R.integer.title_today));
      menuItems.add(new MenuItemBean(R.drawable.home_btn_surronding, R.string.title_surrounding,
            R.integer.title_surrounding));
      menuItems.add(new MenuItemBean(R.drawable.home_btn_search, R.string.title_search, R.integer.title_search));
      menuItems.add(new MenuItemBean(R.drawable.home_btn_map, R.string.title_map, R.integer.title_map));
      menuItems.add(new MenuItemBean(R.drawable.home_btn_favorite, R.string.title_favorite, R.integer.title_favorite));
      menuItems.add(new MenuItemBean(R.drawable.home_btn_info, R.string.title_info, R.integer.title_info));
      menuItems.add(new MenuItemBean(R.drawable.home_btn_donate, R.string.title_donate, R.integer.title_donate));

      return menuItems;
   }
}
