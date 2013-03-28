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

package net.ausgstecktis.adapter;

import java.util.ArrayList;

import net.ausgstecktis.R;
import net.ausgstecktis.DAL.ProxyFactory;
import net.ausgstecktis.entities.City;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * CityListAdapter.java
 * Used to displays the cities in a list view
 * 
 * @author naikon
 * @version Aug 26, 2011
 */
public class CityListAdapter extends BaseAdapter {

   private final Context context;

   private ArrayList<City> cityList;

   public CityListAdapter(final Context context) {
      this.context = context;
      cityList = ProxyFactory.getProxy().getCityList();
   }

   @Override
   public void notifyDataSetChanged() {
      cityList = ProxyFactory.getProxy().getCityList();
      super.notifyDataSetChanged();
   }

   public int getCount() {
      return cityList.size();
   }

   public City getItem(final int position) {
      return cityList.get(position);
   }

   public long getItemId(final int position) {
      return position;
   }

   public View getView(final int position, View convertView, final ViewGroup viewGroup) {
      if (!cityList.isEmpty()) {
         final City entry = cityList.get(position);
         if (convertView == null) {
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_row_city, null);
         }

         final TextView tvCity = (TextView) convertView.findViewById(R.id.tv_list_view_city);
         tvCity.setText(entry.getName());

         final TextView tvZipcode = (TextView) convertView.findViewById(R.id.tv_list_view_zipcode);
         tvZipcode.setText(context.getString(R.string.tv_zipcode) + " " + entry.getZipCode().toString());

         final TextView tvCount = (TextView) convertView.findViewById(R.id.tv_list_view_count);
         tvCount.setText(context.getString(R.string.tv_amount) + " " + entry.getAmount().toString());
      }
      return convertView;
   }
}