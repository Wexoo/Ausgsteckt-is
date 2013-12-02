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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import net.ausgstecktis.entities.City;
import net.ausgstecktis.entities.District;
import net.ausgstecktis.entities.Heuriger;
import net.ausgstecktis.entities.OpenTime;
import net.ausgstecktis.entities.OpeningCalendar;
import net.ausgstecktis.ui.SuperActivity;
import net.wexoo.organicdroid.Log;
import net.wexoo.organicdroid.convert.DateAndTimeConverter;
import net.wexoo.organicdroid.convert.NumberConverter;
import net.wexoo.organicdroid.util.DALUtil;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;

/**
 * Used to generate the URL's for the REST Service
 * OnlineProxy.java
 * 
 * @author naikon
 * @since 1.0.0 Jul 6, 2011
 */
public class OnlineProxy extends AbstractProxy {

   private static final String TAG = OnlineProxy.class.getSimpleName();
   private OfflineProxy offlineProxy;
   private RestClient currentRestClient;

   /**
    * {@inheritDoc}
    * 
    * @see net.ausgstecktis.DAL.AbstractProxy#getHeurigeByKeyword(java.lang.String)
    */

   @Override
   public ArrayList<Heuriger> getHeurigeByKeyword(String searchString) {
      try {
         searchString = URLEncoder.encode(searchString, "UTF-8");
      } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
         Log.e(TAG, "Failed to decode searchString");
      }
      searchString = searchString.replaceAll("\\+", "%20");
      return getHeurige(RestClient.GET_HEURIGE_BY_KEYWORD + replaceSpacesByHTMLChar(searchString));
   }

   private String replaceSpacesByHTMLChar(String searchString) {
      return searchString != null ? searchString.replace(" ", "%20").trim() : "";
   }

   /**
    * {@inheritDoc}
    * 
    * @see net.ausgstecktis.DAL.AbstractProxy#getHeurigeByDistrict(java.lang.Integer)
    */

   @Override
   public ArrayList<Heuriger> getHeurigeByDistrict(final Integer districtId) {
      return getHeurige(RestClient.GET_HEURIGE_BY_DISTRICT + districtId);
   }

   /**
    * {@inheritDoc}
    * 
    * @see net.ausgstecktis.DAL.AbstractProxy#getHeurigeByDate(java.util.Date)
    */

   @Override
   public ArrayList<Heuriger> getHeurigeByDate(final Date date) {
      return getHeurige(RestClient.GET_HEURIGE_BY_DATE + DateAndTimeConverter.getFileStringValueOfDate(date));
   }

   /**
    * {@inheritDoc}
    * 
    * @see net.ausgstecktis.DAL.AbstractProxy#getHeurigeByLocation(java.util.Date, java.lang.String)
    */

   @Override
   public ArrayList<Heuriger> getHeurigeByLocation(final Date date, String searchString) {
      try {
         searchString = URLEncoder.encode(searchString, "UTF-8");
      } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
         Log.e(TAG, "Failed to encode searchString");
      }
      searchString = searchString.replaceAll("\\+", "%20");
      return getHeurige(RestClient.GET_HEURIGE_BY_LOCATION_1
            + DateAndTimeConverter.getFileStringValueOfDate(date) + RestClient.GET_HEURIGE_BY_LOCATION_2
            + searchString);
   }

   /**
    * {@inheritDoc}
    * 
    * @see net.ausgstecktis.DAL.AbstractProxy#getNearbyHeurige(double, double)
    */

   @Override
   public ArrayList<Heuriger> getNearbyHeurige(final double latitude, final double longitude) {
      return getHeurige(RestClient.GET_NEAR_BY_HEURIGE_1 + latitude
            + RestClient.GET_NEAR_BY_HEURIGE_2 + longitude);
   }

   /**
    * {@inheritDoc}
    * 
    * @see net.ausgstecktis.DAL.AbstractProxy#getCityByDate(java.util.Date)
    */

   @Override
   public ArrayList<City> getCityByDate(final Date date) {
      return getCity(RestClient.GET_CITY_BY_DATE);
   }

   /**
    * {@inheritDoc}
    * 
    * @see net.ausgstecktis.DAL.AbstractProxy#getHeurigeByDateCity(java.util.Date, java.lang.String)
    */

   @Override
   public ArrayList<Heuriger> getHeurigeByDateCity(final Date date, final String idCity) {
      return getHeurigenByCity(RestClient.GET_HEURIGE_BY_DATE_CITY + idCity);
   }

   /**
    * {@inheritDoc}
    * 
    * @see net.ausgstecktis.DAL.AbstractProxy#getFavoriteHeurige()
    */

   @Override
   public ArrayList<Heuriger> getFavoriteHeurige() {
      return getOfflineProxy().getFavoriteHeurige();
   }

   /**
    * Gets the heurige.
    * 
    * @param service the method for the REST Server to build the URI for the API
    * @return the ArrayList with heurige
    * @author naikon
    * @since 1.0.0 Jul 6, 2011
    * @version 1.0.0 Jul 6, 2011
    */
   public ArrayList<Heuriger> getHeurige(final String service) {
      final ArrayList<Heuriger> heurige = new ArrayList<Heuriger>();

      prepareRestClientAndCallWebServiceWithGET(service);

      /* TODO parse json data, Find a more generic way to do this */
      try {
         if (currentRestClient.getObjectJson() != null) {
            final JSONArray jArray = currentRestClient.getObjectJson();

            for (int i = 0; i < jArray.length(); i++) {
               final JSONObject jsonData = jArray.getJSONObject(i);

               final Heuriger heuriger = OnlineProxy.parseHeurigerObjectFromJSONData(jsonData);

               if (!SuperActivity.checkIfMigrationOrCheckInProgress())
                  heuriger.setFavorite(getOfflineProxy().isHeurigerFavorite(heuriger.getId()));

               heurige.add(heuriger);
            }
         }
      } catch (final JSONException e) {
         Log.e(OnlineProxy.TAG, "Error parsing data " + e.toString());
      }
      return heurige;
   }

   /**
    * Get only the cities for the TodayActivity
    * 
    * @author naikon
    * @since 1.0.0 Jul 6, 2011
    * @version 1.0.0 Jul 6, 2011
    * @param service the method for the REST Server to build the URI for the API
    * @return the ArrayList with cities
    */
   public ArrayList<City> getCity(final String service) {
      final ArrayList<City> cityList = new ArrayList<City>();

      prepareRestClientAndCallWebServiceWithGET(service);

      // parse json data
      try {
         if (currentRestClient.getObjectJson() != null) {
            final JSONArray jArray = currentRestClient.getObjectJson();
            for (int i = 0; i < jArray.length(); i++) {
               final JSONObject jsonData = jArray.getJSONObject(i);

               final City city = new City(
                     NumberConverter.getIntegerValueOfString(jsonData.getString(City.ID_COLUMN)),
                     jsonData.getString(City.NAME_COLUMN),
                     NumberConverter.getIntegerValueOfString(jsonData.getString(City.ZIPCODE_COLUMN)),
                     NumberConverter.getIntegerValueOfString(jsonData.getString("amount")));

               cityList.add(city);
            }
         } else {
            //UIUtils.showShortToast("Connection time out");
         }
      } catch (final JSONException e) {
         Log.e(OnlineProxy.TAG, "Error parsing data " + e.toString());
      }
      return cityList;
   }

   /**
    * Get the heurigen for a city, used by TodayActivity
    * 
    * @author naikon
    * @since 1.0.0 Jul 6, 2011
    * @version 1.0.0 Jul 6, 2011
    * @param service the method for the REST Server to build the URO for the API
    * @return the ArrayList with cities
    */
   public ArrayList<Heuriger> getHeurigenByCity(final String service) {
      final ArrayList<Heuriger> heurige = new ArrayList<Heuriger>();

      prepareRestClientAndCallWebServiceWithGET(service);

      // parse json data
      try {
         if (currentRestClient.getObjectJson() != null) {
            final JSONArray jArray = currentRestClient.getObjectJson();
            for (int i = 0; i < jArray.length(); i++) {
               final JSONObject jsonData = jArray.getJSONObject(i);

               final Heuriger heuriger = parseHeurigerObjectFromJSONData(jsonData);

               if (!SuperActivity.checkIfMigrationOrCheckInProgress())
                  heuriger.setFavorite(getOfflineProxy().isHeurigerFavorite(heuriger.getId()));

               heurige.add(heuriger);
            }
         }
      } catch (final JSONException e) {
         Log.e(OnlineProxy.TAG, "Error parsing data " +
               e.toString());
      }
      return heurige;
   }

   /**
    * Get open times for a Heurigen.
    * 
    * @param heurigenId the heurigen id
    * @return the open dates by id
    * @see net.ausgstecktis.DAL.AbstractProxy#getOpenDatesById(java.lang.Integer)
    */

   @Override
   public ArrayList<OpeningCalendar> getOpenDatesById(final Integer heurigenId) {
      final ArrayList<OpeningCalendar> calendarList = new ArrayList<OpeningCalendar>();

      prepareRestClientAndCallWebServiceWithGET(RestClient.GET_CALENDAR + heurigenId.toString());

      try {
         if (currentRestClient.getObjectJson() != null) {
            final JSONArray jArray = currentRestClient.getObjectJson();
            for (int i = 0; i < jArray.length(); i++) {
               final JSONObject jsonData = jArray.getJSONObject(i);

               final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
               Date start = null;
               Date end = null;
               try {
                  end = formatter.parse(jsonData.getString(OpenTime.END_COLUMN));
                  start = formatter.parse(jsonData.getString(OpenTime.START_COLUMN));
               } catch (final ParseException e) {
                  e.printStackTrace();
               }

               calendarList.add(new OpeningCalendar(NumberConverter.getIntegerValueOfString(jsonData
                     .getString(OpeningCalendar.ID_COLUMN)), null, start, end));
            }
         }
      } catch (final JSONException e) {
         Log.e(OnlineProxy.TAG, "Error parsing data " + e.toString());
      }
      return calendarList;
   }

   @Override
   public boolean updateFavorite(final Heuriger heuriger) {
      return getOfflineProxy().updateFavorite(heuriger);
   }

   /**
    * Get the districts where heurige exist
    * 
    * @return the districts where heurige exist
    * @see net.ausgstecktis.DAL.AbstractProxy#getDistrictsWhereHeurigeExist()
    */

   @Override
   public ArrayList<District> getDistrictsWhereHeurigeExist() {
      final ArrayList<District> districtList = new ArrayList<District>();

      prepareRestClientAndCallWebServiceWithGET(RestClient.GET_TODAY_DISTRICTS);

      try {
         if (currentRestClient.getObjectJson() != null) {
            final JSONArray jArray = currentRestClient.getObjectJson();
            for (int i = 0; i < jArray.length(); i++) {
               final JSONObject jsonData = jArray.getJSONObject(i);

               final District district = new District(
                     NumberConverter.getIntegerValueOfString(jsonData.getString(District.ID_COLUMN)),
                     jsonData.getString(District.KBZ_COLUMN),
                     jsonData.getString(District.NAME_COLUMN));

               districtList.add(district);
            }
         } else {
            //UIUtils.showShortToast("Connection time out");
         }
      } catch (final JSONException e) {
         Log.e(OnlineProxy.TAG, "Error parsing data " + e.toString());
      }
      return districtList;
   }

   /**
    * @see net.ausgstecktis.DAL.AbstractProxy#getHeurigenById(java.lang.Integer)
    */

   @Override
   public Heuriger getHeurigenById(Integer id) {
      Heuriger heuriger = null;

      prepareRestClientAndCallWebServiceWithGET(RestClient.GET_HEURIGER_BY_ID + id);

      try {
         if (currentRestClient.getObjectJson() != null) {
            final JSONArray jArray = currentRestClient.getObjectJson();
            final JSONObject jsonData = jArray.getJSONObject(0);
            heuriger = parseHeurigerObjectFromJSONData(jsonData);

            if (SuperActivity.checkIfMigrationOrCheckInProgress())
               heuriger.setFavorite(getOfflineProxy().isHeurigerFavorite(heuriger.getId()));
         } else
            heuriger = getOfflineProxy().getHeurigenById(id);
      } catch (final JSONException e) {
         Log.e(OnlineProxy.TAG, "Error parsing data " + e.toString());

      }
      return heuriger;
   }

   public static Heuriger parseHeurigerObjectFromJSONData(final JSONObject jsonData) throws JSONException {
      City city = new City(NumberConverter.getIntegerValueOfString(jsonData.getString(Heuriger.CITY_COLUMN)),
            jsonData.getString("city"),
            NumberConverter.getIntegerValueOfString(jsonData.getString(City.ZIPCODE_COLUMN)));
      return parseHeurigerObjectFromJSONData(jsonData, city);
   }

   public static Heuriger parseHeurigerObjectFromJSONData(final JSONObject jsonData, City cityOfHeurigen)
      throws JSONException {
      return new Heuriger(
            NumberConverter.getIntegerValueOfString(jsonData.getString(Heuriger.ID_COLUMN)),
            jsonData.getString(Heuriger.NAME_COLUMN) != null ? jsonData.getString(Heuriger.NAME_COLUMN) : "",
            jsonData.getString(Heuriger.SORT_NAME_COLUMN) != null ? jsonData.getString(Heuriger.SORT_NAME_COLUMN) : "",
            jsonData.getString(Heuriger.STREET_COLUMN) != null ? jsonData.getString(Heuriger.STREET_COLUMN) : "",
            NumberConverter.getIntegerValueOfString(jsonData.getString(Heuriger.STREETNUMBER_COLUMN)),
            cityOfHeurigen,
            NumberConverter.getLongValueOfString(jsonData.getString(Heuriger.PHONE_COLUMN)),
            NumberConverter.getLongValueOfString(getNotNullStringFromJSONObject(jsonData, Heuriger.PHONE2_COLUMN)),
            NumberConverter.getLongValueOfString(jsonData.getString(Heuriger.FAX_COLUMN)),
            jsonData.getString(Heuriger.EMAIL_COLUMN) != null ? jsonData.getString(Heuriger.EMAIL_COLUMN) : "",
            jsonData.getString(Heuriger.WEBSITE_COLUMN) != null ? jsonData.getString(Heuriger.WEBSITE_COLUMN) : "",
            NumberConverter.getIntegerValueOfString(jsonData.getString(Heuriger.INDOOR_COLUMN)),
            NumberConverter.getIntegerValueOfString(jsonData.getString(Heuriger.OUTDOOR_COLUMN)),
            jsonData.getString(Heuriger.DESCRIPTION_COLUMN) != null ? jsonData.getString(Heuriger.DESCRIPTION_COLUMN)
                  : "",
            jsonData.getString(Heuriger.OPENING_COLUMN) != null ? jsonData.getString(Heuriger.OPENING_COLUMN) : "",
            NumberConverter.getDoubleValueOfString(jsonData.getString(Heuriger.LONGITUDE_COLUMN)),
            NumberConverter.getDoubleValueOfString(jsonData.getString(Heuriger.LATITUDE_COLUMN)),
            DALUtil.getBooleanValueOfString(getNotNullStringFromJSONObject(jsonData, Heuriger.TOP_COLUMN)),
            DALUtil.getBooleanValueOfString(getNotNullStringFromJSONObject(jsonData, Heuriger.FAVORITE_COLUMN)),
            NumberConverter.getDoubleValueOfString(getNotNullStringFromJSONObject(jsonData, "distance")));
   }

   public static String getNotNullStringFromJSONObject(JSONObject jsonObject, String columnKey) throws JSONException {
      return jsonObject.has(columnKey) ? jsonObject.getString(columnKey) : "";
   }

   private void prepareRestClientAndCallWebServiceWithGET(final String service) {
      if (currentRestClient == null)
         currentRestClient = new RestClient(service);
      if (!currentRestClient.getService().equals(service))
         currentRestClient.setService(service);
      try {
         currentRestClient.callWebService(RestClient.GET);
      } catch (final IllegalStateException e1) {
         e1.printStackTrace();
      } catch (final ClientProtocolException e1) {
         e1.printStackTrace();
      } catch (final IOException e1) {
         e1.printStackTrace();
      }
   }

   @SuppressWarnings("unused")
   private void prepareRestClientAndCallWebServiceWithPOST(final String service) {
      if (currentRestClient == null)
         currentRestClient = new RestClient(service);
      if (!currentRestClient.getService().equals(service))
         currentRestClient.setService(service);
      try {
         currentRestClient.callWebService(RestClient.POST);
      } catch (final IllegalStateException e1) {
         e1.printStackTrace();
      } catch (final ClientProtocolException e1) {
         e1.printStackTrace();
      } catch (final IOException e1) {
         e1.printStackTrace();
      }
   }

   /**
    * @see net.ausgstecktis.DAL.AbstractProxy#releaseResources()
    */
   @Override
   public void releaseResources() {
      // TODO: Close connection, cleanup resources, etc.
   }

   /**
    * @see net.ausgstecktis.DAL.AbstractProxy#getAutoCompleteKeywordsCursor(java.lang.String)
    */
   @Override
   public Cursor getAutoCompleteKeywordsCursor(String constraint) {
      return getOfflineProxy().getAutoCompleteKeywordsCursor(constraint);
   }

   /**
    * @see net.ausgstecktis.DAL.AbstractProxy#getAutoCompleteAreasCursor()
    */
   @Override
   public Cursor getAutoCompleteAreasCursor(String constraint) {
      return getOfflineProxy().getAutoCompleteAreasCursor(constraint);
   }

   public OfflineProxy getOfflineProxy() {
      if (offlineProxy == null)
         offlineProxy = new OfflineProxy();
      return offlineProxy;
   }

}