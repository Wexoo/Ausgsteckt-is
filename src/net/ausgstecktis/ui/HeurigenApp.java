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

import static net.ausgstecktis.util.AppEnvironmentField.APP_VERSION_CODE;
import static net.ausgstecktis.util.AppEnvironmentField.APP_VERSION_NAME;
import static net.ausgstecktis.util.AppEnvironmentField.PACKAGE_NAME;
import net.ausgstecktis.annotation.Settings;
import net.ausgstecktis.util.AppEnvironmentField;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

/**
 * The Class HeurigenApp.
 * Main Class for the app
 * Sets also specific settings as annotations for the app
 * 
 * @author wexoo, naikon
 */
@Settings(
      apiUri = "http://dev.ausgstecktis.net/app/",
      apiValue = "apikey",
      apiKey = "foo")
public class HeurigenApp extends Application {

   /** The main context. */
   public static Context mainContext;
   public static boolean MIGRATION_IN_PROGRESS = false;
   public static boolean CHECK_IN_PROGRESS = false;
   public static boolean CHECK_FOR_UPDATE = true;

   private static Application mApplication;
   private static Settings mApplicationSetting;

   /**
    * <p>
    * Initialize HeurigenApp for a given Application. The call to this method should be placed as soon as possible in the {@link Application#onCreate()} method.
    * </p>
    * 
    * @param app
    *        Your Application class.
    */

   public static void init(final Application app) {
      HeurigenApp.mApplication = app;
      HeurigenApp.mApplicationSetting = HeurigenApp.mApplication.getClass().getAnnotation(Settings.class);
   }

   /**
    * Provides the configuration settings.
    * 
    * @return HeurigenApp {@link Settings} configuration instance.
    */
   public static Settings getConfig() {
      return HeurigenApp.mApplicationSetting;
   }

   @Override
   public void onCreate() {
      // The following line triggers the initialization of this app itself
      HeurigenApp.init(HeurigenApp.this);

      // The following line triggers the initialization of ACRA
      //      ACRA.init(this);
      super.onCreate();
   }

   /**
    * Check for phone's internet access.
    * 
    * @return whether phone is connected or connecting to the internet or not
    */
   public boolean isOnline() {
      final ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
      final NetworkInfo netInfo = cm.getActiveNetworkInfo();
      if (netInfo != null && netInfo.isConnectedOrConnecting())
         return true;
      return false;
   }

   /**
    * Check for sd card,... availability
    * 
    * @return whether sd card or other external storage is available
    */
   public boolean isExternalStorageAvailable() {
      return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
   }

   /**
    * Default list of {@link AppEnvironmentField}s to be read from the environment. You can set your own list with {@link net.ausgstecktis.annotation.Settings#customEnvironmentContent()}.
    */
   public static final AppEnvironmentField[] DEFAULT_ENVIRONMENT_FIELDS = {APP_VERSION_CODE, APP_VERSION_NAME,
         PACKAGE_NAME};

}