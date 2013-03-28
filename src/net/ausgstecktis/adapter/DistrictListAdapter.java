/***
 * Copyright (C) 2011 naikon, wexoo android@geekosphere.org
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package net.ausgstecktis.adapter;

import java.util.ArrayList;

import net.ausgstecktis.R;
import net.ausgstecktis.DAL.ProxyFactory;
import net.ausgstecktis.entities.District;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * DistrictListAdapter.java
 * Used to displays the districts in a list view
 * 
 * @author naikon
 * @version Aug 26, 2011
 */
public class DistrictListAdapter extends BaseAdapter {

   private final Context context;

   private ArrayList<District> districtList;

   public DistrictListAdapter(final Context context, final ArrayList<District> districtList) {
      this.context = context;
      this.districtList = districtList;
   }

   @Override
   public void notifyDataSetChanged() {
      districtList = ProxyFactory.getProxy().getDistrictsWhereHeurigeExist();
      super.notifyDataSetChanged();
   }

   public int getCount() {
      return districtList.size();
   }

   public District getItem(final int position) {
      return districtList.get(position);
   }

   public long getItemId(final int position) {
      return position;
   }

   public View getView(final int position, View convertView, final ViewGroup viewGroup) {
      if (!districtList.isEmpty()) {
         final District entry = districtList.get(position);
         if (convertView == null) {
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_row_district, null);
         }

         final TextView tvCity = (TextView) convertView.findViewById(R.id.tv_list_view_name);
         tvCity.setText(entry.getName());
      }
      return convertView;
   }
}