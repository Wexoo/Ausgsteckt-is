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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import net.ausgstecktis.ui.HeurigenApp;
import net.wexoo.organicdroid.Log;
import net.wexoo.organicdroid.settings.AppEnvironment;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * RestClient for the OnlineService Supports GET and POST methods with
 * API-KEY support
 * 
 * @author naikon
 */
public class RestClient {

   /**
    * Access service methods for the API
    */
   public static final String GET_HEURIGE_BY_KEYWORD = "service/search/keyword/";
   public static final String GET_HEURIGE_BY_DATE_CITY = "today/city/id/";
   public static final String GET_CITY_BY_DATE = "today/cities";
   public static final String GET_NEAR_BY_HEURIGE_2 = "/lat/";
   public static final String GET_NEAR_BY_HEURIGE_1 = "service/surrounding/long/";
   public static final String GET_HEURIGE_BY_LOCATION_2 = "/location/";
   public static final String GET_HEURIGE_BY_LOCATION_1 = "service/searchlocation/date/";
   public static final String GET_HEURIGE_BY_DATE = "service/searchdate/date/";
   public static final String GET_TODAY_DISTRICTS = "map/todaydistricts";
   public static final String GET_HEURIGE_BY_DISTRICT = "map/heurigenbydistrict/id/";

   public static final String GET_DOWNLOAD_DB = "download/db";
   public static final String GET_UPDATE_INFO = "download/updateinfo/date/";
   public static final String GET_NEW_AND_UPDATED_CITIES = "download/newandupdatedcities/date/";
   public static final String GET_NEW_AND_UPDATED_HEURIGE = "download/newandupdatedheurige/date/";
   public static final String GET_NEW_AND_UPDATED_CALENDAR = "download/newandupdatedcalendars/date/";

   public static final String GET_CALENDAR = "service/calendar/id/";
   public static final String GET_AUTOCOMPLETE_KEYWORD = "service/autocompletekeyword";
   public static final String GET_AUTOCOMPLETE_LOCATION = "service/autocompletelocation";
   public static final String GET_MAP = "map/";
   public static final String GET_HEURIGER_BY_ID = "service/heuriger/id/";

   public static final String TAG = "RestClient";
   public static final int POST = 0;
   public static final int GET = 1;
   public static final int DB = 2;
   private static final int HTTP_PORT = 80;
   private final DefaultHttpClient httpClient;
   private final HttpContext localContext;
   private final String url;
   private String service;
   private String response;
   private HttpEntity httpEntity;
   private JSONArray recvObj;

   /**
    * The rest client, who is responsible the connection to the online API
    * 
    * @author naikon
    * @param url the URI for the API
    * @param service the service for the API method
    */
   public RestClient(final String service) {
      url = HeurigenApp.getSettings().apiUri();
      this.service = service;

      final HttpParams httpParams = new BasicHttpParams();
      HttpConnectionParams.setConnectionTimeout(httpParams, HeurigenApp.getSettings().socketTimeout());
      HttpConnectionParams.setSoTimeout(httpParams, HeurigenApp.getSettings().socketTimeout());
      HttpConnectionParams.setSocketBufferSize(httpParams, HeurigenApp.getSettings().socketBufferSize());

      final SchemeRegistry registry = new SchemeRegistry();
      registry.register(new Scheme("http", new PlainSocketFactory(), RestClient.HTTP_PORT));

      httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(httpParams, registry), httpParams);
      localContext = new BasicHttpContext();
   }

   /**
    * Converts a Inputstream to String
    * 
    * @author naikon
    * @param is the inputstream which is to convert
    * @throws IOException if an input or output exception occurred
    * @return the converted string
    */
   public String convertStreamToString(final InputStream is) throws IOException {
      if (is != null) {
         final Writer writer = new StringWriter();
         final char[] buffer = new char[1024];
         try {
            final Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1)
               writer.write(buffer, 0, n);
         } catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
         } finally {
            is.close();
         }
         return writer.toString();
      }
      return "";
   }

   /**
    * Sends the HTTP POST request to the url
    * 
    * @author naikon
    * @throws IOException if an input or output exception occurred
    */
   public void sendHttpPost() throws IOException {
      //TODO not already in use, for future request planned
   }

   /**
    * Sends the HTTP GET request to the url
    * Stores the response in the response string
    * 
    * @author naikon
    * @throws IOException if an input or output exception occurred
    */
   public void sendHttpGet() throws IOException {

      httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2109);

      final String restUrl = url + service;
      final HttpGet httpGet = new HttpGet(restUrl);

      Log.v(RestClient.TAG, "Setting httpGet headers");

      httpGet.setHeader("User-Agent", "Android/" + new AppEnvironment().getAndroidVersion());
      httpGet.setHeader("Accept",
            "text/html,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
      httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");
      httpGet.setHeader(HeurigenApp.getSettings().apiValue(), HeurigenApp.getSettings().apiKey());

      Log.d(RestClient.TAG, "Sending request to " + restUrl);

      final HttpResponse httpResponse = httpClient.execute(httpGet, localContext);

      if (httpResponse != null) {
         final StatusLine statusLine = httpResponse.getStatusLine();
         if (statusLine != null) {
            final String statusCode = Integer.toString(httpResponse.getStatusLine().getStatusCode());
            if (statusCode.startsWith("4") || statusCode.startsWith("5"))
               throw new IOException("Host returned error code " + statusCode);
         }

         response = EntityUtils.toString(httpResponse.getEntity());

         Log.d(RestClient.TAG, "HTTP " + (statusLine != null ? statusLine.getStatusCode() : "NoStatusLine#noCode")
               + " - Returning value:" + response.substring(0, Math.min(response.length(), 200)));
      } else
         Log.d(RestClient.TAG, "HTTP no Response!!");
   }

   /**
    * Sends the HTTP GET request to the url to download the db file
    * Saves the http entity response in the entity variable
    * 
    * @author naikon
    * @throws IOException if an input or output exception occurred
    */
   public void downloadDB() throws IOException {
      httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2109);

      final String restUrl = url + service;
      final HttpGet httpGet = new HttpGet(restUrl);

      Log.d(RestClient.TAG, "Setting httpGet headers");

      httpGet.setHeader("User-Agent", "Android/" + new AppEnvironment().getAndroidVersion());
      httpGet.setHeader("Accept",
            "text/html,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
      httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");
      httpGet.setHeader(HeurigenApp.getSettings().apiValue(), HeurigenApp.getSettings().apiKey());

      Log.d(RestClient.TAG, "Sending request to " + restUrl);

      final HttpResponse httpResponse = httpClient.execute(httpGet, localContext);

      if (httpResponse != null) {
         final StatusLine statusLine = httpResponse.getStatusLine();
         if (statusLine != null) {
            final String statusCode = Integer.toString(httpResponse.getStatusLine().getStatusCode());
            if (statusCode.startsWith("4") || statusCode.startsWith("5"))
               throw new IOException("Host returned error code " + statusCode);
         }

         httpEntity = httpResponse.getEntity();

      } else
         Log.d(RestClient.TAG, "HTTP no Response!!");
   }

   /**
    * Makes a request to the API with GET or POST or download DB
    * 
    * @author naikon
    * @param method this is a integer value (0 = POST; 1 = GET; 2 = DB)
    * @throws IllegalStateException
    *         signals that a method has been invoked at an illegal
    *         or inappropriate time. In other words, the Java environment
    *         or Java application is not in an appropriate state for the requested operation
    * @throws ClientProtocolException
    *         signals an error in the HTTP protocol
    * @throws IOException
    *         if an input or output exception occurred
    */
   public void callWebService(final int method) throws IllegalStateException, ClientProtocolException, IOException {
      switch (method){
         case GET:
            sendHttpGet();
            break;
         case POST:
            sendHttpPost();
            break;
         case DB:
            downloadDB();
      }
   }

   /**
    * Creates the JSON Array Object
    * 
    * @author naikon
    * @throws JSONException the JSONException is thrown by the JSON.org classes when things are amiss.
    */
   public void createObjectJson() throws JSONException {
      if (response != null) {
         recvObj = new JSONArray(response);
         Log.i(RestClient.TAG, "json object" + recvObj.toString() + "/json object");
      }
   }

   /**
    * Creates and return the JSON Array Object
    * 
    * @author naikon
    * @return JSONArray filled with data
    * @throws JSONException the JSONException is thrown by the JSON.org classes when things are amiss.
    */
   public JSONArray getObjectJson() throws JSONException {
      if (response != null)
         return recvObj = new JSONArray(response);
      return null;
   }

   public String getService() {
      return service;
   }

   public void setService(String service) {
      this.service = service;
   }

   public HttpEntity getHttpEntity() {
      return httpEntity;
   }
}