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
import net.ausgstecktis.DAL.MigrationProxy;
import net.ausgstecktis.DAL.ProxyFactory;
import net.ausgstecktis.adapter.HeurigerAlphabetizedListAdapter;
import net.ausgstecktis.entities.Heuriger;
import net.ausgstecktis.ui.search.SearchActivity;
import net.wexoo.organicdroid.Log;
import net.wexoo.organicdroid.concurrency.AbstractAsyncTask;
import net.wexoo.organicdroid.convert.DateAndTimeConverter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * Displays the search results from SearchActivity
 * ResultActivity.java
 * 
 * @author naikon, wexoo
 * @since 1.0.0 Aug 5, 2011
 */
public class ResultActivity extends SuperActivity {

   private static final String TAG = ResultActivity.class.getSimpleName();
   private ListView list;
   private HeurigerAlphabetizedListAdapter adapter;
   private GetHeurigeAsyncTask heurigeAsyncTask;
   private String titleString = "";

   @Override
   public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      this.setContentView(R.layout.activity_results);
      setProgressBarIndeterminateVisibility(true);

      boolean searchKeyword = getIntent().getExtras().getBoolean("extra_search_keyword");
      String searchString = getIntent().getExtras().getString("extra_search_string");
      Date searchDate = (Date) getIntent().getExtras().get("extra_search_date");

      if (!searchString.equals(""))
         titleString = searchString;
      else if (searchDate != null)
         titleString = DateAndTimeConverter.getWholeMonthDateAsString(searchDate);
      else
         titleString = this.getString(R.string.search_all_heurigen);

      getSupportActionBar().setTitle(titleString);

      list = (ListView) findViewById(R.id.lv_search_results);
      list.setFastScrollEnabled(true);
      list.setOnItemClickListener(new OnItemClickListener() {

         @Override
         public void onItemClick(final AdapterView<?> parent, final View view,
               final int position, final long rowId) {
            ProxyFactory.getProxy().setSelectedHeuriger((Heuriger) parent.getItemAtPosition(position));
            startActivity(new Intent(ResultActivity.this, DetailActivity.class));
         }
      });
      registerForContextMenu(list);

      heurigeAsyncTask = new GetHeurigeAsyncTask(searchKeyword, searchString, searchDate);
      heurigeAsyncTask.execute();
   }

   @Override
   protected void setSelectedHeuriger(final AdapterContextMenuInfo info) {
      ProxyFactory.getProxy().setSelectedHeuriger(adapter.getItem(info.position));
   }

   @Override
   protected void cancelAllAsyncTasksOfActivity() {
      cancelAllAsynTasks(heurigeAsyncTask);
   }

   /**
    * Fetches heurige according to search conditions
    */
   public class GetHeurigeAsyncTask extends AbstractAsyncTask {

      boolean searchKeyword;
      String searchString;
      Date searchDate;

      public GetHeurigeAsyncTask(boolean searchKeyword, String searchString, Date searchDate) {
         this.searchKeyword = searchKeyword;
         this.searchString = searchString;
         this.searchDate = searchDate;
      }

      @Override
      protected Void doInBackground(final Void... params) {
         Date start = new Date();

         if (searchKeyword)
            ProxyFactory.getProxy().setHeurigeByKeyword(searchString);
         else
            ProxyFactory.getProxy().setHeurigeByLocation(searchDate, searchString);

         String elapsedTime = MigrationProxy.calculateElapsedTime(start);
         Log.d(TAG, "Fetch of Heurigen took: " + elapsedTime);

         return null;
      }

      @Override
      protected void onPostExecute(final Void result) {
         Date start = new Date();
         if (ProxyFactory.getProxy().getHeurigenList().isEmpty()) {
            SearchActivity.setNoResult(false);
            finish();
         } else {
            adapter = new HeurigerAlphabetizedListAdapter(getApplicationContext());
            list.setAdapter(adapter);
         }

         String elapsedTime = MigrationProxy.calculateElapsedTime(start);
         Log.d(TAG, "Adapter action took: " + elapsedTime);

         setProgressBarIndeterminateVisibility(false);
      }

   }

}