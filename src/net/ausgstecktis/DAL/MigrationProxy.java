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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.ausgstecktis.R;
import net.ausgstecktis.DAL.util.DALUtils;
import net.ausgstecktis.entities.City;
import net.ausgstecktis.entities.District;
import net.ausgstecktis.entities.Heuriger;
import net.ausgstecktis.entities.OpenDay;
import net.ausgstecktis.entities.OpenTime;
import net.ausgstecktis.entities.OpeningCalendar;
import net.ausgstecktis.entities.Region;
import net.ausgstecktis.ui.HeurigenApp;
import net.ausgstecktis.ui.PreferencesActivity;
import net.ausgstecktis.util.Log;
import net.ausgstecktis.util.UIUtils;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Environment;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.DatabaseConnection;

/**
 * MigrationProxy.java
 * 
 * @author wexoo
 */
public class MigrationProxy {

   private static final String TAG = MigrationProxy.class.getSimpleName();
   public static final String DB_PATH = Environment.getDataDirectory() + "/data/net.ausgstecktis/databases/";
   public static final String CREATED_HEURIGE_KEY = "c_heurige";
   public static final String UPDATE_HEURIGE_KEY = "u_heurige";
   public static final String CREATED_CALENDAR_KEY = "c_calendar";
   public static final String UPDATED_CALENDAR_KEY = "u_calendar";

   private static MigrationProxy instance;

   private static OrmLiteSqliteOpenHelper helper;
   private static OfflineProxy offlineProxy;
   private RestClient currentRestClient;

   private static ArrayList<Heuriger> favoriteHeurigeList;

   public static MigrationProxy getInstance() {
      return getInstance(HeurigenApp.mainContext);
   }

   /**
    * Gets the single instance of MigrationProxy.
    */
   public static MigrationProxy getInstance(Context context) {
      if (MigrationProxy.instance == null)
         MigrationProxy.instance = new MigrationProxy();

      if (helper == null)
         helper = OpenHelperManager.getHelper(HeurigenApp.mainContext);

      Log.d(TAG, "Helper fetched using current context!");
      return instance;
   }

   /**
    * @author wexoo
    */
   public boolean isLocalDatabasePresent() {
      try {
         final Dao<Heuriger, Integer> objectDao = helper.getDao(Heuriger.class);

         return objectDao.isTableExists()
               && !objectDao.queryRaw("select count(*) from " + Heuriger.TABLE_NAME).getResults().get(0)[0]
                     .equals("0");
      } catch (final SQLException e) {
         Log.e(TAG, e.getMessage());
         e.printStackTrace();
      }
      return false;
   }

   /**
    * Store old favorite heurige.
    */
   public void storeOldFavoriteHeurige() {
      favoriteHeurigeList = getOfflineProxy().getFavoriteHeurige();
   }

   /**
    * Creates an empty database on the system and rewrites it with your own database.
    * 
    * @author wexoo
    */
   public void createDataBase(final boolean migrateLocalDatabase) {

      /*
       * By calling this method an empty database will be created into the default
       * system path of your application so we will be able to overwrite that
       * database with our database.
       */
      helper.getReadableDatabase();

      try {
         boolean migrateFavorites = isLocalDatabasePresent();

         if (migrateFavorites)
            instance.storeOldFavoriteHeurige();

         if (migrateLocalDatabase)
            copyDataBase();
         else
            copyOnlineDataBase();

         if (migrateFavorites)
            instance.markFavoriteHeurigeInNewDatabase();
         //         helper.close();
      } catch (final IOException e) {
         throw new Error("Error copying database");
      }
   }

   /**
    * Copies your database from your local assets-folder to the just created
    * empty database in the system folder, from where it can be accessed and
    * handled. This is done by transfering bytestream.
    * 
    * @throws IOException if an input or output exception occurred
    * @author wexoo
    */
   private void copyDataBase() throws IOException {

      Log.d(TAG, "starting to copy database");

      // Open your local db as the input stream
      final InputStream myInput = HeurigenApp.mainContext.getAssets().open(
            HeurigenApp.getConfig().databaseName());

      // Path to the just created empty db
      final String outFileName = DB_PATH + HeurigenApp.getConfig().databaseName();

      // Open the empty db as the output stream
      final OutputStream myOutput = new FileOutputStream(outFileName);

      // transfer bytes from the inputfile to the outputfile
      final byte[] buffer = new byte[1024];
      int length;
      while ((length = myInput.read(buffer)) > 0)
         myOutput.write(buffer, 0, length);

      // Close the streams
      myOutput.flush();
      myOutput.close();
      myInput.close();
   }

   /**
    * Copies the online database version in the system folder
    * from where it can be accessed and handled.
    * This is done by transfering bytestream.
    * 
    * @throws IOException if the file/path is not read or writeable
    * @author naikon
    */
   private void copyOnlineDataBase() throws IOException {

      prepareRestClientAndCallWebService(RestClient.GET_DOWNLOAD_DB, RestClient.DB);

      //get http enity
      final HttpEntity entity = currentRestClient.getHttpEntity();

      // Path to the just created empty db
      final String outFileName = DB_PATH + HeurigenApp.getConfig().databaseName();

      // Open the empty db as the output stream
      final OutputStream output = new FileOutputStream(outFileName);

      final InputStream in = entity.getContent();

      final byte[] buffer = new byte[1024];
      int len1 = 0;
      while ((len1 = in.read(buffer)) > 0)
         output.write(buffer, 0, len1);
      output.close();
      in.close();
   }

   /**
    * Import favorite heurige to new database.
    */
   private void markFavoriteHeurigeInNewDatabase() {

      try {
         final Dao<Heuriger, Integer> objectDao = helper.getDao(Heuriger.class);

         for (final Heuriger heuriger : favoriteHeurigeList) {

            final UpdateBuilder<Heuriger, Integer> update = objectDao.updateBuilder();

            update.updateColumnValue(Heuriger.FAVORITE_COLUMN, heuriger.getFavorite());
            update.where().eq(Heuriger.ID_COLUMN, heuriger.getId());
            objectDao.update(update.prepare());
         }
         favoriteHeurigeList = null;

      } catch (final SQLException e) {
         Log.e(TAG, e.getMessage());
      }
   }

   public Map<String, Integer> getUpdateInfo() {

      prepareRestClientAndCallWebServiceWithLastUpdateDateAttached(RestClient.GET_UPDATE_INFO, RestClient.GET);

      try {
         if (currentRestClient.getObjectJson() != null) {
            final JSONArray jArray = currentRestClient.getObjectJson();
            Map<String, Integer> updateQuantities = new HashMap<String, Integer>(4);

            for (int i = 0; i < jArray.length(); i++) {
               final JSONObject jsonData = jArray.getJSONObject(i);

               updateQuantities.put(CREATED_HEURIGE_KEY,
                     DALUtils.getIntegerValueOfString(jsonData.getString(CREATED_HEURIGE_KEY)));
               updateQuantities.put(UPDATE_HEURIGE_KEY,
                     DALUtils.getIntegerValueOfString(jsonData.getString(UPDATE_HEURIGE_KEY)));
               updateQuantities.put(CREATED_CALENDAR_KEY,
                     DALUtils.getIntegerValueOfString(jsonData.getString(CREATED_CALENDAR_KEY)));
               updateQuantities.put(UPDATED_CALENDAR_KEY,
                     DALUtils.getIntegerValueOfString(jsonData.getString(UPDATED_CALENDAR_KEY)));

            }
            Log.d(TAG, CREATED_HEURIGE_KEY + ": " + updateQuantities.get(CREATED_HEURIGE_KEY));
            Log.d(TAG, UPDATE_HEURIGE_KEY + ": " + updateQuantities.get(UPDATE_HEURIGE_KEY));
            Log.d(TAG, CREATED_CALENDAR_KEY + ": " + updateQuantities.get(CREATED_CALENDAR_KEY));
            Log.d(TAG, UPDATED_CALENDAR_KEY + ": " + updateQuantities.get(UPDATED_CALENDAR_KEY));

            return updateQuantities;
         } else
            Log.e(TAG, "Returned objectJSON is null!");
      } catch (final JSONException e) {
         Log.e(TAG, "Error parsing data " + e.toString());
      }
      return null;
   }

   public void getNewAndUpdatedCities() {

      Log.d(TAG, "vor getHeurige offlineproxy zugriff");

      getOfflineProxy();

      Log.d(TAG, "nach getHeurige offlineproxy zugriff");

      prepareRestClientAndCallWebServiceWithLastUpdateDateAttached(RestClient.GET_NEW_AND_UPDATED_CITIES,
            RestClient.GET);

      try {
         if (currentRestClient.getObjectJson() != null) {
            final JSONArray jArray = currentRestClient.getObjectJson();

            for (int i = 0; i < jArray.length(); i++) {
               final JSONObject jsonData = jArray.getJSONObject(i);

               Region region = new Region(
                     DALUtils.getIntegerValueOfString(jsonData.getString("r_id")),
                     jsonData.getString("r_name"));

               final Dao<Region, Integer> regionDao = helper.getDao(Region.class);
               regionDao.createOrUpdate(region);

               District district = new District(
                     DALUtils.getIntegerValueOfString(jsonData.getString("d_id")),
                     jsonData.getString("d_kbz"),
                     jsonData.getString("d_name"));

               final Dao<District, Integer> districtDao = helper.getDao(District.class);
               districtDao.createOrUpdate(district);

               City city = new City(
                     DALUtils.getIntegerValueOfString(jsonData.getString("c_id")),
                     jsonData.getString("c_name"),
                     DALUtils.getIntegerValueOfString(jsonData.getString("c_zipcode")),
                     district,
                     region);

               final Dao<City, Integer> cityDao = helper.getDao(City.class);
               cityDao.createOrUpdate(city);
            }
         } else
            Log.e(TAG, "Returned objectJSON is null!");
      } catch (final JSONException e) {
         Log.e(TAG, "Error parsing data " + e.toString());
      } catch (final SQLException e) {
         Log.e(TAG, e.getMessage());
      }
   }

   public void getNewAndUpdatedHeurige() {

      prepareRestClientAndCallWebServiceWithLastUpdateDateAttached(RestClient.GET_NEW_AND_UPDATED_HEURIGE,
            RestClient.GET);

      try {
         if (currentRestClient.getObjectJson() != null) {
            final JSONArray jArray = currentRestClient.getObjectJson();

            Heuriger heuriger;

            for (int i = 0; i < jArray.length(); i++) {
               final JSONObject jsonData = jArray.getJSONObject(i);

               final Dao<City, Integer> cityDao = helper.getDao(City.class);
               Integer idOfCity =
                     DALUtils.getIntegerValueOfString(OnlineProxy.getNotNullStringFromJSONObject(jsonData,
                           Heuriger.CITY_COLUMN));
               City cityOfHeurigen =
                     cityDao.queryForId(idOfCity != 189 ? idOfCity : 188);

               heuriger = OnlineProxy.parseHeurigerObjectFromJSONData(jsonData, cityOfHeurigen);

               final Dao<Heuriger, Integer> heurigerDao = helper.getDao(Heuriger.class);
               Log.i(TAG, "heuriger with id " + heuriger.getId() + " is about to be inserted/updated!");

               if (heuriger.getName().equalsIgnoreCase("xxx"))
                  heurigerDao.delete(heuriger);
               else
                  heurigerDao.createOrUpdate(heuriger);

               Log.i(TAG, "heuriger with id " + heuriger.getId() + " saved!");
            }
         } else
            Log.e(TAG, "Returned objectJSON is null!");
      } catch (final JSONException e) {
         Log.e(TAG, "Error parsing data " + e.toString());
      } catch (final SQLException e) {
         Log.e(TAG, e.getMessage());
         e.printStackTrace();
      }
   }

   public void getNewAndUpdatedCalendarData() {

      prepareRestClientAndCallWebServiceWithLastUpdateDateAttached(RestClient.GET_NEW_AND_UPDATED_CALENDAR,
            RestClient.GET);

      Dao<OpenTime, Integer> openTimeDao = null;
      DatabaseConnection openTimeConn = null;
      boolean commitOpenTime = false;
      Dao<OpeningCalendar, Integer> calendarDao = null;
      DatabaseConnection calendarConn = null;
      boolean commitCalendar = false;
      Dao<OpenDay, Integer> openDayDao = null;
      DatabaseConnection openDayConn = null;

      try {
         if (currentRestClient.getObjectJson() != null) {
            final JSONArray jArray = currentRestClient.getObjectJson();

            openTimeDao = helper.getDao(OpenTime.class);
            openTimeConn = openTimeDao.startThreadConnection();
            openTimeConn.setAutoCommit(false);
            calendarDao = helper.getDao(OpeningCalendar.class);
            calendarConn = calendarDao.startThreadConnection();
            calendarConn.setAutoCommit(false);
            openDayDao = helper.getDao(OpenDay.class);
            openDayConn = openDayDao.startThreadConnection();
            openDayConn.setAutoCommit(false);
            //            openDayDao.setObjectCache(false);

            for (int i = 0; i < jArray.length(); i++) {
               final JSONObject jsonData = jArray.getJSONObject(i);

               OpenTime openTime = openTimeDao.queryForId(DALUtils.getIntegerValueOfString(jsonData.getString("id")));

               if (openTime == null) {
                  openTime = new OpenTime(
                        DALUtils.getIntegerValueOfString(jsonData.getString("id")),
                        DALUtils.getTimeValueOfString(jsonData.getString("ot_start")),
                        DALUtils.getTimeValueOfString(jsonData.getString("ot_end")));

                  openTimeDao.createOrUpdate(openTime);
                  commitOpenTime = true;
               }

               OpeningCalendar calendar =
                     calendarDao.queryForId(DALUtils.getIntegerValueOfString(jsonData.getString("c_id")));

               if (calendar == null) {
                  calendar = new OpeningCalendar(
                        DALUtils.getIntegerValueOfString(jsonData.getString("c_id")),
                        new Heuriger(DALUtils.getIntegerValueOfString(jsonData.getString("id_heuriger"))),
                        DALUtils.getDefaultDateValueOfString(jsonData.getString("c_start")),
                        DALUtils.getDefaultDateValueOfString(jsonData.getString("c_end")));

                  calendarDao.createOrUpdate(calendar);
                  commitCalendar = true;
               }

               OpenDay openDay = new OpenDay(
                     DALUtils.getIntegerValueOfString(jsonData.getString("od_id")),
                     calendar, openTime,
                     DALUtils.getIntegerValueOfString(jsonData.getString("day")));

               openDayDao.createOrUpdate(openDay);

               if (i % 20 == 0) {
                  if (commitOpenTime) {
                     openTimeDao.commit(openTimeConn);
                     commitOpenTime = false;
                  }
                  if (commitCalendar) {
                     calendarDao.commit(calendarConn);
                     commitCalendar = false;
                  }
                  openDayDao.commit(openDayConn);
               }
            }
         } else
            Log.e(TAG, "Returned objectJSON is null!");
      } catch (final JSONException e) {
         Log.e(TAG, "Error parsing data " + e.toString());
         e.printStackTrace();
      } catch (final SQLException e) {
         Log.e(TAG, e.getMessage());
         e.printStackTrace();
      } finally {
         try {
            openTimeDao.endThreadConnection(openTimeConn);
            calendarDao.endThreadConnection(calendarConn);
            openDayDao.endThreadConnection(openDayConn);
         } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
   }

   private void prepareRestClientAndCallWebServiceWithLastUpdateDateAttached(final String service, int requestType) {
      String lastUpdateString = PreferencesActivity.getStringPreference(R.string.last_database_update_key, null);

      prepareRestClientAndCallWebService(service + lastUpdateString, requestType);
   }

   private void prepareRestClientAndCallWebService(final String service, int requestType) {
      if (currentRestClient == null)
         currentRestClient = new RestClient(service);
      if (!currentRestClient.getService().equals(service))
         currentRestClient.setService(service);
      try {
         currentRestClient.callWebService(requestType);
      } catch (final IllegalStateException e1) {
         e1.printStackTrace();
      } catch (final ClientProtocolException e1) {
         e1.printStackTrace();
      } catch (final IOException e1) {
         e1.printStackTrace();
      }
   }

   /**
    * Calculate elapsed time of any action.
    * Save a a timestamp (eg. new Date()) before starting the action and call this method afterwards.
    * 
    * @param start the timestamp before the action started
    * @return elapsed time as string, format 'hh:mm:ss' or 'mm:ss' depending on duration was > 1 hour and with leading zeros
    */
   public static String calculateElapsedTime(final Date start) {
      final Date stop = new Date();
      Long diff = stop.getTime() - start.getTime();

      final long secondInMillis = 1000;
      final long minuteInMillis = secondInMillis * 60;
      final long hourInMillis = minuteInMillis * 60;

      final long elapsedHours = diff / hourInMillis;

      diff = diff % hourInMillis;
      final long elapsedMinutes = diff / minuteInMillis;

      diff = diff % minuteInMillis;
      final long elapsedSeconds = diff / secondInMillis;

      if (elapsedHours != 0)
         return UIUtils.addLeadingZeroToLong(elapsedHours) + ":"
               + UIUtils.addLeadingZeroToLong(elapsedMinutes) + ":"
               + UIUtils.addLeadingZeroToLong(elapsedSeconds);
      return UIUtils.addLeadingZeroToLong(elapsedMinutes) + ":"
            + UIUtils.addLeadingZeroToLong(elapsedSeconds);
   }

   public OfflineProxy getOfflineProxy() {
      if (offlineProxy == null)
         offlineProxy = new OfflineProxy();
      return offlineProxy;
   }
}