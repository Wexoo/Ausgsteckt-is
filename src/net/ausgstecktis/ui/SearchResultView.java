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

package net.ausgstecktis.ui;

import net.ausgstecktis.R;
import net.ausgstecktis.DAL.ProxyFactory;
import net.ausgstecktis.adapter.SearchResultAlphabetizedAdapter;
import net.ausgstecktis.adapter.SearchResultDbHelper;
import net.ausgstecktis.entities.Heuriger;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ViewSwitcher;

/**
 * Sets the list view for the ResultActivity
 * 
 * @author naikon, wexoo
 */
public class SearchResultView extends ViewSwitcher {

   private final LayoutInflater inflater;
   private final SearchResultAlphabetizedAdapter resultScreenAdapter;

   public SearchResultView(final SuperActivity context, final Cursor cursor, final String titleString) {
      super(context);

      inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      final LinearLayout resultScreen = (LinearLayout) inflater.inflate(R.layout.activity_results, null);
      final ListView resultScreenListView = (ListView) resultScreen.findViewById(R.id.lv_search_results);

      //      ((TextView) resultScreen.findViewById(R.id.title_text)).setText(titleString);

      resultScreenAdapter = new SearchResultAlphabetizedAdapter(context, R.layout.list_row, cursor, new String[] {
            SearchResultDbHelper.NAME_COLUMN,
            SearchResultDbHelper.CITY_COLUMN,
            SearchResultDbHelper.STREET_COLUMN},
            new int[] {R.id.tv_list_view_name, R.id.tv_list_view_city, R.id.tv_list_view_street});
      resultScreenListView.setAdapter(resultScreenAdapter);

      resultScreenListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

         @Override
         public void onItemClick(final AdapterView<?> parent, final View arg1, final int position, final long arg3) {
            final Cursor c = ((Cursor) resultScreenAdapter.getItem(position));

            Heuriger heuriger;

            heuriger = ProxyFactory.getProxy().getHeurigenList()
                  .get(Integer.parseInt(c.getString(c.getColumnIndex(SearchResultDbHelper.ID))) - 1);

            ProxyFactory.getProxy().setSelectedHeuriger(heuriger);

            final Intent intent = new Intent(context, DetailActivity.class);
            context.startActivity(intent);
         }
      });

      this.addView(resultScreen);
      this.setOutAnimation(context, android.R.anim.slide_out_right);
      this.setInAnimation(context, android.R.anim.slide_in_left);
   }
}
