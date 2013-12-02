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
import net.ausgstecktis.adapter.CityListAdapter;
import net.ausgstecktis.entities.City;
import net.ausgstecktis.util.UIUtils;
import net.wexoo.organicdroid.concurrency.AbstractAsyncTask;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * TodayActivity.java
 * 
 * @author naikon, wexoo
 */
public class TodayActivity extends SuperActivity {

   private ListView list;
   private CityListAdapter cityAdapter;

   private AsyncTask<Void, Integer, Void> cityByDateAsyncTask;

   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      this.setContentView(R.layout.activity_today);
      setProgressBarIndeterminateVisibility(true);

      list = (ListView) findViewById(R.id.lv_search_results);
      list.setOnItemClickListener(new OnItemClickListener() {

         @Override
         public void onItemClick(final AdapterView<?> parent, final View view,
               final int position, final long rowId) {

            ProxyFactory.getProxy().setSelectedCity((City) parent.getItemAtPosition(position));

            final Bundle bundle = new Bundle();
            bundle.putString("param1", ProxyFactory.getProxy().getSelectedCity().getId().toString());
            bundle.putString("param2", ProxyFactory.getProxy().getSelectedCity().getName());

            TodayActivity.this.startActivity(new Intent(TodayActivity.this, TodaySearchActivity.class)
                  .putExtras(bundle));
         }
      });

      cityByDateAsyncTask = new GetCityByDateAsyncTask();
      cityByDateAsyncTask.execute();
   }

   public void onRefreshClick(final View v) {
      cityByDateAsyncTask = new GetCityByDateAsyncTask();
      cityByDateAsyncTask.execute();

      setProgressBarIndeterminateVisibility(true);
   }

   @Override
   protected void setSelectedCity(final AdapterContextMenuInfo info) {
      ProxyFactory.getProxy().setSelectedCity(cityAdapter.getItem(info.position));
   }

   @Override
   protected void cancelAllAsyncTasksOfActivity() {
      cancelAllAsynTasks(cityByDateAsyncTask);
   }

   /**
    * GetCityByDateAsyncTask.java
    */
   public class GetCityByDateAsyncTask extends AbstractAsyncTask {

      @Override
      protected void onPostExecute(final Void result) {

         if (!ProxyFactory.getProxy().getCityList().isEmpty()) {
            cityAdapter = new CityListAdapter(TodayActivity.this);

            list.setAdapter(cityAdapter);

            setProgressBarIndeterminateVisibility(false);

         } else {

            UIUtils.showShortToast(TodayActivity.this.getString(R.string.toast_no_data_reveived));
            finish();
         }
      }

      /**
       * Fetch list of cities where heurige are opened today.
       */
      @Override
      protected Void doInBackground(final Void... params) {
         ProxyFactory.getProxy().setCityByDate(new Date());
         return null;
      }
   }
}