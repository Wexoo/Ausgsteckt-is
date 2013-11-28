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

import java.util.List;

import net.ausgstecktis.R;
import net.ausgstecktis.DAL.ProxyFactory;
import net.ausgstecktis.entities.City;
import net.wexoo.organicdroid.adapter.AbstractBaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * CityListAdapter.java
 * 
 * @author wexoo
 */
public class CityListAdapter extends AbstractBaseAdapter<City> {

   public CityListAdapter(final Context context) {
      super(context);
   }

   @Override
   public View getView(final int position, View convertView, final ViewGroup viewGroup) {
      if (!entityList.isEmpty()) {
         final City entry = entityList.get(position);
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

   @Override
   protected List<City> fetchList() {
      return ProxyFactory.getProxy().getCityList();
   }
}