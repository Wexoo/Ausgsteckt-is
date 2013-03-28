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

import java.math.BigDecimal;
import java.util.ArrayList;

import net.ausgstecktis.R;
import net.ausgstecktis.DAL.ProxyFactory;
import net.ausgstecktis.entities.Heuriger;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * The Class SurrondingListAdapter.
 * Used to displays the heurigen with distance in a list view
 * 
 * @author naikon
 * @version Aug 26, 2011
 */
public class SurrondingListAdapter extends BaseAdapter {

   private final Context context;
   private ArrayList<Heuriger> heurigenList;

   /**
    * Instantiates a new surronding list adapter.
    * 
    * @param context the context
    */
   public SurrondingListAdapter(final Context context) {
      this.context = context;
      this.heurigenList = ProxyFactory.getProxy().getHeurigenList();
   }

   /**
    * {@inheritDoc}
    * 
    * @see android.widget.BaseAdapter#notifyDataSetChanged()
    */
   
   public void notifyDataSetChanged() {
      this.heurigenList = ProxyFactory.getProxy().getHeurigenList();
      super.notifyDataSetChanged();
   }

   /**
    * {@inheritDoc}
    * 
    * @see android.widget.Adapter#getCount()
    */
   
   public int getCount() {
      return this.heurigenList.size();
   }

   /**
    * {@inheritDoc}
    * 
    * @see android.widget.Adapter#getItem(int)
    */
   
   public Heuriger getItem(final int position) {
      return this.heurigenList.get(position);
   }

   /**
    * {@inheritDoc}
    * 
    * @see android.widget.Adapter#getItemId(int)
    */
   
   public long getItemId(final int position) {
      return position;
   }

   /**
    * {@inheritDoc}
    * 
    * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
    */
   
   public View getView(final int position, View convertView, final ViewGroup viewGroup) {
      if (!this.heurigenList.isEmpty()) {
         final Heuriger entry = this.heurigenList.get(position);
         if (convertView == null) {
            final LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_row_surrounding, null);
         }
         final TextView tvContact = (TextView) convertView.findViewById(R.id.tv_list_view_name);
         tvContact.setText(entry.getName());

         final TextView tvCity = (TextView) convertView.findViewById(R.id.tv_list_view_city);
         tvCity.setText(" " + entry.getCity().getZipCode().toString() + " " + entry.getCity().getName().toString());

         final TextView tvDistance = (TextView) convertView.findViewById(R.id.tv_list_view_distance);
         tvDistance.setText(this.roundTwoDecimals(entry.getDistance().doubleValue()) + " km");

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

   /**
    * Round two decimals.
    * 
    * @param d the d
    * @return the double
    */
   private double roundTwoDecimals(double d) {
      BigDecimal bd = new BigDecimal(d);
      bd = bd.setScale(2, BigDecimal.ROUND_UP);
      d = bd.doubleValue();
      return d;
   }
}