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

package net.ausgstecktis.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;

/**
 * Specifies all the different fields available for the anvironment data.
 * AppEnvironmentField.java
 * 
 * @author naikon
 * @since 1.0.0 Jul 10, 2011
 * @version 1.0.0 Jul 10, 2011
 */
public enum AppEnvironmentField {

   /**
    * Application version code. This is the incremental integer version code
    * used to differentiate versions on the android market.
    * 
    * @see PackageInfo#versionCode
    */
   APP_VERSION_CODE,
   /**
    * Application version name.
    * 
    * @see PackageInfo#versionName
    */
   APP_VERSION_NAME,
   /**
    * Application package name.
    * 
    * @see Context#getPackageName()
    */
   PACKAGE_NAME,
   /**
    * Base path of the application's private file folder.
    * 
    * @see Context#getFilesDir()
    */
   ANDROID_VERSION,
   /**
    * Android Build details.
    * 
    * @see Build
    */
   BUILD,
   /**
    * Device brand (manufacturer or carrier).
    * 
    * @see Build#BRAND
    */
   BRAND,
   /**
    * Device overall product code.
    * 
    * @see Build#PRODUCT
    */
   PRODUCT,
   /**
    * Estimation of the total device memory size based on filesystem stats.
    */
   TOTAL_MEM_SIZE,
   /**
    * Estimation of the available device memory size based on filesystem stats.
    */
   AVAILABLE_MEM_SIZE,
   /**
    * Contains key = value pairs defined by the application developer during
    * the application execution.
    */
}
