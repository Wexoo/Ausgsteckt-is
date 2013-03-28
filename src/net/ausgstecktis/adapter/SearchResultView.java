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

import net.ausgstecktis.R;
import net.ausgstecktis.DAL.ProxyFactory;
import net.ausgstecktis.entities.Heuriger;
import net.ausgstecktis.ui.DetailActivity;
import net.ausgstecktis.ui.SuperActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

/**
 * Sets the list view for the ResultActivity
 * 
 * @author naikon
 * @since 1.0.0 Aug 5, 2011
 */
public class SearchResultView extends ViewSwitcher {

   private final LayoutInflater inflater;
   private final SearchResultAlphabetizedAdapter resultScreenAdapter;

   /**
    * Creates the List view for the ResultActivity.
    * 
    * @param context the context
    * @param cursor the cursor
    * @param titleString the title string
    * @author naikon
    * @since 1.0.0 Aug 5, 2011
    * @version 1.0.0 Aug 5, 2011
    */
   public SearchResultView(final SuperActivity context, final Cursor cursor, final String titleString) {
      super(context);

      inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      final LinearLayout resultScreen = (LinearLayout) inflater.inflate(R.layout.activity_results, null);
      final ListView resultScreenListView = (ListView) resultScreen.findViewById(R.id.lv_search_results);

      ((TextView) resultScreen.findViewById(R.id.title_text)).setText(titleString);
      final ProgressBar progressBar = (ProgressBar) resultScreen.findViewById(R.id.title_refresh_progress);
      final ImageView searchIcon = (ImageView) resultScreen.findViewById(R.id.btn_title_search);
      progressBar.setVisibility(View.GONE);
      searchIcon.setVisibility(View.VISIBLE);

      resultScreenAdapter = new SearchResultAlphabetizedAdapter(context, R.layout.list_row, cursor, new String[] {
            SearchResultDbHelper.NAME_COLUMN,
            SearchResultDbHelper.CITY_COLUMN,
            SearchResultDbHelper.STREET_COLUMN},
            new int[] {R.id.tv_list_view_name, R.id.tv_list_view_city, R.id.tv_list_view_street});
      resultScreenListView.setAdapter(resultScreenAdapter);

      resultScreenListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
