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

package net.ausgstecktis.ui.search;

import java.util.Date;

import net.ausgstecktis.R;
import net.ausgstecktis.DAL.ProxyFactory;
import net.ausgstecktis.entities.Heuriger;
import net.ausgstecktis.ui.DetailActivity;
import net.ausgstecktis.ui.ResultActivity;
import net.ausgstecktis.ui.SuperActivity;
import net.wexoo.organicdroid.adapter.AutoCompleteCursorAdapter;
import net.wexoo.organicdroid.util.UIUtil;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;

/**
 * SearchActivity.java
 * 
 * @author naikon, wexoo
 */
public class SearchActivity extends SuperActivity {

   @SuppressWarnings("unused")
   private static final String TAG = SearchActivity.class.getSimpleName();
   private DatePicker searchDatePicker;
   private static boolean noResult = true;
   private String searchString;
   private Date searchDate;

   @Override
   public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      this.setContentView(R.layout.activity_search);

      final Button buttonAll = (Button) findViewById(R.id.b_search_all_button);
      buttonAll.getBackground().setColorFilter(new LightingColorFilter(0xFF297508, 0xFF000000));

      final Button buttonLocation = (Button) findViewById(R.id.b_search_location_button);
      buttonLocation.getBackground().setColorFilter(new LightingColorFilter(0xFF297508, 0xFF000000));

      searchDatePicker = (DatePicker) findViewById(R.id.search_date_picker);
   }

   @Override
   protected void onResume() {
      super.onResume();

      buildAutoCompleteKeywordsTextView();
      buildAutoCompleteAreasTextView();

      if (!SearchActivity.noResult)
         UIUtil.showLongToast(SearchActivity.this, getResources().getString(R.string.tv_no_result));

      noResult = true;
   }

   private void buildAutoCompleteAreasTextView() {
      Cursor areasCursor = ProxyFactory.getProxy().getAutoCompleteAreasCursor(null);
      startManagingCursor(areasCursor);

      AutoCompleteCursorAdapter areasAdapter = new AutoCompleteCursorAdapter(this, areasCursor) {

         @Override
         protected Cursor returnAutoCompleteCursor(String constraint) {
            return ProxyFactory.getProxy().getAutoCompleteAreasCursor(constraint);
         }

         @Override
         protected String createDisplayValue(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndex("name"));
         }
      };

      AutoCompleteTextView areasTextView = (AutoCompleteTextView) findViewById(R.id.actv_search_location);
      areasTextView.setAdapter(areasAdapter);
      areasTextView.setOnItemClickListener(new OnItemClickListener() {

         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            startSearch(false);
         }
      });
   }

   private void buildAutoCompleteKeywordsTextView() {
      Cursor keywordsCursor = ProxyFactory.getProxy().getAutoCompleteKeywordsCursor("");
      startManagingCursor(keywordsCursor);

      AutoCompleteCursorAdapter adapter = new AutoCompleteCursorAdapter(this,
            keywordsCursor) {

         @Override
         protected Cursor returnAutoCompleteCursor(String constraint) {
            return ProxyFactory.getProxy().getAutoCompleteKeywordsCursor(constraint);
         }

         @Override
         protected String createDisplayValue(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndex(Heuriger.NAME_COLUMN));
         }
      };

      AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.actv_search);
      textView.setAdapter(adapter);
      textView.setOnItemClickListener(new OnItemClickListener() {

         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ProxyFactory.getProxy().setSelectedHeurigenById((int) id);

            startActivity(new Intent(SearchActivity.this, DetailActivity.class));
         }
      });
   }

   public void startSearch(final View view) {

      switch (view.getId()){
         case R.id.b_search_all_button:
            startSearch(true);
            break;
         case R.id.b_search_location_button:
            startSearch(false);
            break;
         default:
            break;
      }
   }

   private void startSearch(boolean searchKeyword) {
      if (searchKeyword)
         searchString = ((AutoCompleteTextView) findViewById(R.id.actv_search)).getText().toString().trim();
      else
         searchString = ((AutoCompleteTextView) findViewById(R.id.actv_search_location)).getText().toString().trim();

      updateSearchDate();

      Intent resultActivityIntent = new Intent(this, ResultActivity.class);
      resultActivityIntent.putExtra("extra_search_keyword", searchKeyword);
      resultActivityIntent.putExtra("extra_search_string", searchString);
      resultActivityIntent.putExtra("extra_search_date", searchDate);

      startActivity(resultActivityIntent);
   }

   private void updateSearchDate() {
      if (((CheckBox) findViewById(R.id.include_date_in_search)).isChecked())
         searchDate = new Date(searchDatePicker.getYear() - 1900, searchDatePicker.getMonth(),
               searchDatePicker.getDayOfMonth());
      else
         searchDate = null;
   }

   public static void setNoResult(final boolean noResult) {
      SearchActivity.noResult = noResult;
   }
}