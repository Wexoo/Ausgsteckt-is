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

import java.util.List;

import net.ausgstecktis.R;
import net.ausgstecktis.DAL.ProxyFactory;
import net.ausgstecktis.entities.Heuriger;
import net.wexoo.organicdroid.adapter.AbstractBaseAdapter;
import net.wexoo.organicdroid.convert.NumberConverter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * SurrondingListAdapter.java
 * Used to displays the heurigen with distance to current position in a list view
 * 
 * @author naikon, wexoo
 */
public class SurrondingListAdapter extends AbstractBaseAdapter<Heuriger> {

   public SurrondingListAdapter(final Context context) {
      super(context);
   }

   @Override
   protected List<Heuriger> fetchList() {
      return ProxyFactory.getProxy().getHeurigenList();
   }

   @Override
   public View getView(final int position, View convertView, final ViewGroup viewGroup) {
      if (!entityList.isEmpty()) {
         final Heuriger entry = entityList.get(position);
         if (convertView == null) {
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_row_surrounding, null);
         }
         final TextView tvContact = (TextView) convertView.findViewById(R.id.tv_list_view_name);
         tvContact.setText(entry.getName());

         final TextView tvCity = (TextView) convertView.findViewById(R.id.tv_list_view_city);
         tvCity.setText(" " + entry.getCity().getZipCode().toString() + " " + entry.getCity().getName().toString());

         final TextView tvDistance = (TextView) convertView.findViewById(R.id.tv_list_view_distance);
         tvDistance.setText(NumberConverter.roundTwoDecimals(entry.getDistance().doubleValue()) + " km");

         if (entry.getStreetNumber().toString().equals("0")) {
            final TextView tvStreet = (TextView) convertView.findViewById(R.id.tv_list_view_street);
            tvStreet.setText(" " + entry.getStreet().toString());
         } else {
            final TextView tvStreet = (TextView) convertView.findViewById(R.id.tv_list_view_street);
            tvStreet.setText(" " + entry.getStreet().toString() + " " + entry.getStreetNumber().toString());
         }
      }
      return convertView;
   }
}