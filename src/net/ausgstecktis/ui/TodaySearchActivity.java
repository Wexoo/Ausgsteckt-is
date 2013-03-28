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

import java.util.Date;

import net.ausgstecktis.R;
import net.ausgstecktis.DAL.ProxyFactory;
import net.ausgstecktis.adapter.HeurigerListAdapter;
import net.ausgstecktis.entities.Heuriger;
import net.ausgstecktis.util.AbstractAsyncTask;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * TodaySearchActivity.java
 * 
 * @author naikon
 * @version Aug 27, 2011
 */
public class TodaySearchActivity extends SuperActivity {

   private ListView list;
   private HeurigerListAdapter adapter;
   private ProgressBar progressBar;
   private ImageView refreshIcon;
   private String cityId;

   private GetHeurigeByDateAndCityAsyncTask heurigeByDateAndCityAsyncTask;

   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      this.setContentView(R.layout.activity_today);
      super.onCreate(savedInstanceState);

      cityId = getIntent().getExtras().getString("param1");

      ((TextView) findViewById(R.id.title_text)).setText(getIntent().getExtras().getString("param2"));

      list = (ListView) findViewById(R.id.lv_search_results);
      list.setOnItemClickListener(new OnItemClickListener() {

         public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long rowId) {
            ProxyFactory.getProxy().setSelectedHeuriger((Heuriger) parent.getItemAtPosition(position));
            TodaySearchActivity.this.startActivity(new Intent(TodaySearchActivity.this, DetailActivity.class));
         }
      });
      registerForContextMenu(list);

      heurigeByDateAndCityAsyncTask = new GetHeurigeByDateAndCityAsyncTask();
      heurigeByDateAndCityAsyncTask.execute();

      progressBar = (ProgressBar) findViewById(R.id.title_refresh_progress);
      progressBar.setVisibility(View.VISIBLE);

      refreshIcon = (ImageView) findViewById(R.id.btn_title_refresh);
      refreshIcon.setVisibility(View.GONE);
   }

   /**
    * Executed when the refresh button in the title bar is clicked
    */
   public void onRefreshClick(final View v) {
      progressBar = (ProgressBar) findViewById(R.id.title_refresh_progress);
      progressBar.setVisibility(View.VISIBLE);

      refreshIcon = (ImageView) findViewById(R.id.btn_title_refresh);
      refreshIcon.setVisibility(View.GONE);

      heurigeByDateAndCityAsyncTask = new GetHeurigeByDateAndCityAsyncTask();
      heurigeByDateAndCityAsyncTask.execute();
   }

   @Override
   protected void setSelectedHeuriger(final AdapterContextMenuInfo info) {
      ProxyFactory.getProxy().setSelectedHeuriger(adapter.getItem(info.position));
   }

   @Override
   protected void setSelectedCity(final AdapterContextMenuInfo info) {
      ProxyFactory.getProxy().setSelectedHeuriger(adapter.getItem(info.position));
   }

   @Override
   protected void cancelAllAsyncTasksOfActivity() {
      cancelAllAsynTasks(heurigeByDateAndCityAsyncTask);
   }

   /**
    * GetHeurigeByDateAndCityAsyncTask.java
    */
   private class GetHeurigeByDateAndCityAsyncTask extends AbstractAsyncTask {

      @Override
      protected void onPostExecute(final Void result) {
         /** show the list only if a city is available **/
         if (!ProxyFactory.getProxy().getCityList().isEmpty()) {

            adapter = new HeurigerListAdapter(TodaySearchActivity.this);
            list.setAdapter(adapter);

            progressBar.setVisibility(View.GONE);
            refreshIcon.setVisibility(View.VISIBLE);
         }
      }

      /**
       * Fetch heurige of selected city
       */
      @Override
      protected Void doInBackground(final Void... params) {
         ProxyFactory.getProxy().setHeurigeByDateCity(new Date(), cityId);
         return null;
      }
   }
}
