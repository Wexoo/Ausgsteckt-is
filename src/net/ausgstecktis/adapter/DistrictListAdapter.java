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
import net.ausgstecktis.entities.District;
import net.wexoo.organicdroid.adapter.AbstractBaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * DistrictListAdapter.java
 * 
 * @author naikon, wexoo
 */
public class DistrictListAdapter extends AbstractBaseAdapter<District> {

   public DistrictListAdapter(final Context context, final List<District> districtList) {
      super(context, districtList);
   }

   @Override
   public View getView(final int position, View convertView, final ViewGroup viewGroup) {
      if (!entityList.isEmpty()) {
         final District entry = entityList.get(position);
         if (convertView == null) {
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_row_district, null);
         }

         final TextView tvCity = (TextView) convertView.findViewById(R.id.tv_list_view_name);
         tvCity.setText(entry.getName());
      }
      return convertView;
   }

   @Override
   protected List<District> fetchList() {
      return ProxyFactory.getProxy().getDistrictsWhereHeurigeExist();
   }
}