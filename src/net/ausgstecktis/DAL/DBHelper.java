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

package net.ausgstecktis.DAL;

import java.sql.SQLException;

import net.ausgstecktis.entities.City;
import net.ausgstecktis.entities.DayExcluded;
import net.ausgstecktis.entities.DayIncluded;
import net.ausgstecktis.entities.District;
import net.ausgstecktis.entities.Heuriger;
import net.ausgstecktis.entities.OpenDay;
import net.ausgstecktis.entities.OpenTime;
import net.ausgstecktis.entities.OpeningCalendar;
import net.ausgstecktis.entities.Region;
import net.ausgstecktis.ui.HeurigenApp;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * The Class DBHelper.
 * 
 * @author naikon
 * @version Aug 27, 2011
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {

   @SuppressWarnings("unused")
   private static final String TAG = DBHelper.class.getSimpleName();

   /**
    * Instantiates a new dB helper.
    * 
    * @param context the context
    */
   public DBHelper(final Context context) {
      super(context, HeurigenApp.getConfig().databaseName(), null, HeurigenApp.getConfig().databaseVersion());
   }

   /**
    * Instantiates a new dB helper.
    */
   public DBHelper() {
      super(HeurigenApp.mainContext, HeurigenApp.getConfig().databaseName(), null,
            HeurigenApp.getConfig().databaseVersion());
   }

   /**
    * Called at the time to create the DB. The create DB statement
    * 
    * @param db the SQLite database
    * @param source the source
    * @author wexoo
    * @since 1.0.0 Jul 6, 2011
    */
   
   public void onCreate(final SQLiteDatabase db, final ConnectionSource source) {
      try {
         TableUtils.createTable(source, Heuriger.class);
         TableUtils.createTable(source, Region.class);
         TableUtils.createTable(source, City.class);
         TableUtils.createTable(source, District.class);
         TableUtils.createTable(source, OpeningCalendar.class);
         TableUtils.createTable(source, DayExcluded.class);
         TableUtils.createTable(source, DayIncluded.class);
         TableUtils.createTable(source, OpenDay.class);
         TableUtils.createTable(source, OpenTime.class);
      } catch (final SQLException e) {
         e.printStackTrace();
      }
   }

   /**
    * Drop and create table.
    * 
    * @param entityClass the entity class
    */
   public void dropAndCreateTable(final Class<?> entityClass) {
      try {
         TableUtils.dropTable(getConnectionSource(), entityClass, true);
         TableUtils.createTable(getConnectionSource(), entityClass);
      } catch (final SQLException e) {
         e.printStackTrace();
      }
   }

   /**
    * Invoked if a DB upgrade (version change) has been detected.
    * 
    * @param db the SQLiteDatabase
    * @param arg1 the arg1
    * @param oldVersion the last version of the database
    * @param newVersion the new or current version of the database
    * @author wexoo
    * @since 1.0.0 Jul 6, 2011
    */
   
   public void onUpgrade(final SQLiteDatabase db, final ConnectionSource arg1,
         final int oldVersion, final int newVersion) {
      Toast.makeText(HeurigenApp.mainContext, "New SQLite Version!\nPrevious: " + oldVersion + "\nNew:"
            + newVersion, Toast.LENGTH_LONG).show();
   }
}