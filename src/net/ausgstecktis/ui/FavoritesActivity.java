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
import net.ausgstecktis.adapter.HeurigerListAdapter;
import net.ausgstecktis.entities.Heuriger;
import net.ausgstecktis.util.AbstractAsyncTask;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

/**
 * The Class FavoritesActivity.
 * 
 * @author naikon
 * @version Aug 27, 2011
 */
public class FavoritesActivity extends SuperActivity {

   private ListView list;
   private HeurigerListAdapter adapter;
   private ProgressBar progressBar;
   private ImageView refreshIcon;
   private LoadFavoritesAsyncTask favoritesAsyncTask;

   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      this.setContentView(R.layout.activity_favorites);
      super.onCreate(savedInstanceState);

      list = (ListView) findViewById(R.id.lv_search_results);
      list.setOnItemClickListener(new OnItemClickListener() {

         public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long rowId) {
            ProxyFactory.getProxy().setSelectedHeuriger((Heuriger) parent.getItemAtPosition(position));

            startActivity(new Intent(FavoritesActivity.this, DetailActivity.class));
         }
      });
      registerForContextMenu(list);

      ProxyFactory.getProxy().getHeurigenList().clear();
   }

   @Override
   protected void onResume() {
      super.onResume();

      favoritesAsyncTask = new LoadFavoritesAsyncTask();
      favoritesAsyncTask.execute();

      progressBar = (ProgressBar) findViewById(R.id.title_refresh_progress);
      progressBar.setVisibility(View.VISIBLE);

      refreshIcon = (ImageView) findViewById(R.id.btn_title_refresh);
      refreshIcon.setVisibility(View.GONE);
   }

   public void onRefreshClick(final View v) {

      favoritesAsyncTask = new LoadFavoritesAsyncTask();
      favoritesAsyncTask.execute();

      progressBar = (ProgressBar) findViewById(R.id.title_refresh_progress);
      progressBar.setVisibility(View.VISIBLE);

      refreshIcon = (ImageView) findViewById(R.id.btn_title_refresh);
      refreshIcon.setVisibility(View.GONE);
   }

   /**
    * {@inheritDoc}
    * 
    * @see net.ausgstecktis.ui.SuperActivity#setSelectedHeuriger(android.widget.AdapterView.AdapterContextMenuInfo)
    */
   @Override
   protected void setSelectedHeuriger(final AdapterContextMenuInfo info) {
      ProxyFactory.getProxy().setSelectedHeuriger(adapter.getItem(info.position));
   }

   /**
    * {@inheritDoc}
    * 
    * @see net.ausgstecktis.ui.SuperActivity#onContextItemSelected(android.view.MenuItem)
    */
   @Override
   public boolean onContextItemSelected(final MenuItem item) {
      final boolean returnValue = super.onContextItemSelected(item);
      onResume();
      return returnValue;
   }

   @Override
   protected void cancelAllAsyncTasksOfActivity() {
      cancelAllAsynTasks(favoritesAsyncTask);
   }

   public class LoadFavoritesAsyncTask extends AbstractAsyncTask {

      /**
       * {@inheritDoc}
       * 
       * @see android.os.AsyncTask#doInBackground(Params[])
       */
      @Override
      protected Void doInBackground(final Void... params) {
         ProxyFactory.getProxy().setFavoriteHeurige();
         return null;
      }

      /**
       * {@inheritDoc}
       * 
       * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
       */
      @Override
      protected void onPostExecute(final Void result) {
         adapter = new HeurigerListAdapter(FavoritesActivity.this);
         list.setAdapter(adapter);

         progressBar.setVisibility(View.GONE);
         refreshIcon.setVisibility(View.VISIBLE);
      }
   }
}