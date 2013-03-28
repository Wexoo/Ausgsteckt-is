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

import net.ausgstecktis.entities.Heuriger;
import net.ausgstecktis.util.Log;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * A database helper to create the db table with heurigen
 * SearchResultDbHelper.java
 * 
 * @author naikon
 * @since 1.0.0 Aug 5, 2011
 * @version 1.0.0 Aug 5, 2011
 */
public class SearchResultDbHelper extends SQLiteOpenHelper {

   public static final String TABLE_NAME_HEURIGER = "heuriger";
   public static final String ID_COLUMN = "id";
   public static final String ID = "_id";
   public static final String NAME_COLUMN = "name";
   public static final String SORT_NAME_COLUMN = "sort_name";
   public static final String STREET_COLUMN = "street";
   public static final String STREETNUMBER_COLUMN = "streetnumber";
   public static final String CITY_COLUMN = "id_city";
   public static final String ZIPCODE_COLUMN = "zipcode";
   private static final String DATABASE_NAME = "heurigen_tmp.db";
   private static final int DATABASE_VERSION = 1;

   /**
    * Instantiates a new search result db helper.
    * 
    * @param context the context
    */
   public SearchResultDbHelper(final Context context) {
      super(context, SearchResultDbHelper.DATABASE_NAME, null, SearchResultDbHelper.DATABASE_VERSION);
   }

   /**
    * {@inheritDoc}
    * 
    * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
    */

   @Override
   public void onCreate(final SQLiteDatabase db) {
      db.execSQL("create table " + SearchResultDbHelper.TABLE_NAME_HEURIGER + " (" +
            BaseColumns._ID + " integer primary key autoincrement,"
            + SearchResultDbHelper.ID_COLUMN + " text not null,"
            + SearchResultDbHelper.NAME_COLUMN + " text not null,"
            + SearchResultDbHelper.SORT_NAME_COLUMN + " text not null,"
            + SearchResultDbHelper.STREET_COLUMN + " text,"
            + SearchResultDbHelper.STREETNUMBER_COLUMN + " text,"
            + SearchResultDbHelper.CITY_COLUMN + " text,"
            + SearchResultDbHelper.ZIPCODE_COLUMN + " text,"
            + "unique (" + SearchResultDbHelper.NAME_COLUMN + ") on conflict replace)");
   }

   /**
    * {@inheritDoc}
    * 
    * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
    */

   @Override
   public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
      Log.d("", "---------> drop table");
      db.execSQL("drop table " + SearchResultDbHelper.TABLE_NAME_HEURIGER);
      onCreate(db);
   }

   /**
    * Insert data.
    * 
    * @param db the db
    * @param heurigen the heurigen
    * @return the int
    */
   public int insertData(final SQLiteDatabase db, final List<Heuriger> heurigen) {
      int numInserted = 0;
      db.beginTransaction();
      try {
         final int length = heurigen.size();
         for (int i = 0; i < length; i++) {
            final Heuriger a = heurigen.get(i);
            Log.d("", "--------->" + SearchResultDbHelper.ID_COLUMN + ": " + a.getId());
            final ContentValues values = new ContentValues();
            values.put(SearchResultDbHelper.ID_COLUMN, a.getId());
            values.put(SearchResultDbHelper.NAME_COLUMN, a.getName());
            values.put(SearchResultDbHelper.SORT_NAME_COLUMN, a.getName()); //TODO fix sorting
            final String streetnumber =
                  (a.getStreetNumber().toString().equals("0") ? "" : a.getStreetNumber().toString());
            values.put(SearchResultDbHelper.STREET_COLUMN, a.getStreet() + " " + streetnumber);
            values.put(SearchResultDbHelper.CITY_COLUMN, a.getCity().getZipCode() + " " + a.getCity().getName());
            final long effec = db.insert(SearchResultDbHelper.TABLE_NAME_HEURIGER, null, values);
            values.put(SearchResultDbHelper.ID, effec);
            Log.d("DbHelper", "--->insert: " + effec);
         }
         db.setTransactionSuccessful();
         numInserted = length;
      } catch (final Exception e) {
         e.printStackTrace();
      } finally {
         db.endTransaction();
      }
      return numInserted;
   }

}