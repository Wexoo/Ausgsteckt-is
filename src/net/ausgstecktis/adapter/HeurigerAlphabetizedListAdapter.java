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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.ausgstecktis.R;
import net.ausgstecktis.DAL.ProxyFactory;
import net.ausgstecktis.entities.Heuriger;
import net.ausgstecktis.ui.HeurigenApp;
import net.ausgstecktis.util.Log;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

/**
 * Displays the heurigen in a list view.
 * 
 * @author wexoo
 * @since 1.0.0 Aug 5, 2011
 */

public class HeurigerAlphabetizedListAdapter extends ArrayAdapter<Heuriger> implements SectionIndexer {

   private static String TAG = HeurigerAlphabetizedListAdapter.class.getSimpleName();

   private final Context context;

   private List<Heuriger> heurigenList;
   private HashMap<String, Integer> azIndexer;
   private String[] sections;

   public HeurigerAlphabetizedListAdapter() {
      this(HeurigenApp.mainContext);
   }

   public HeurigerAlphabetizedListAdapter(Context context) {
      this(context, ProxyFactory.getProxy().getHeurigenList());
   }

   public HeurigerAlphabetizedListAdapter(Context context, List<Heuriger> objects) {
      super(context, R.layout.list_sectioner, objects);
      this.context = context;
      heurigenList = objects;
      azIndexer = new HashMap<String, Integer>();

      int size = heurigenList.size();
      for (int i = size - 1; i >= 0; i--) {
         String element = heurigenList.get(i).getSortName();

         azIndexer.put(element.substring(0, 1), i);
      }

      Set<String> keys = azIndexer.keySet();

      Iterator<String> it = keys.iterator();
      ArrayList<String> keyList = new ArrayList<String>();

      while (it.hasNext()) {
         String key = it.next();
         keyList.add(key);
      }
      Collections.sort(keyList);

      sections = keyList.toArray(new String[keyList.size()]);
   }

   public int getPositionForSection(int section) {
      String letter = sections[section];
      return azIndexer.get(letter);
   }

   public int getSectionForPosition(int position) {
      Log.v(TAG, "section for position");
      return 0;
   }

   public Object[] getSections() {
      return sections;
   }

   //   /**
   //    * @see android.widget.BaseAdapter#notifyDataSetChanged()
   //    */
   //   
   //   public void notifyDataSetChanged() {
   //      //      heurigenList = ProxyFactory.getProxy().getHeurigenList();
   //      super.notifyDataSetChanged();
   //   }

   @Override
   public View getView(final int position, View convertView, final ViewGroup viewGroup) {
      if (!heurigenList.isEmpty()) {
         final Heuriger entry = heurigenList.get(position);
         if (convertView == null) {
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_row, null);
         }

         if (null != entry.getCity()) {
            final TextView tvContact = (TextView) convertView.findViewById(R.id.tv_list_view_name);
            tvContact.setText(entry.getName());

            final TextView tvCity = (TextView) convertView.findViewById(R.id.tv_list_view_city);
            tvCity.setText(" " + entry.getCity().getZipCode().toString() + " " + entry.getCity().getName().toString());

            if (entry.getStreetNumber().toString().equals("0")) {
               final TextView tvStreet = (TextView) convertView.findViewById(R.id.tv_list_view_street);
               tvStreet.setText(" " + entry.getStreet().toString());
            } else {
               final TextView tvStreet = (TextView) convertView.findViewById(R.id.tv_list_view_street);
               tvStreet.setText(" " + entry.getStreet().toString() + " " + entry.getStreetNumber().toString());
            }
         }
      }
      return convertView;
   }
}