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

package net.ausgstecktis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.ausgstecktis.util.AppEnvironmentField;

/**
 * Provide configuration elemets to the. The only mandatory
 * configuration item is the {@link #formUri()} parameter which is the url to the API to receive data from the
 * onlineservice.
 * ApplicationSetting.java
 * 
 * @author naikon
 * @since 1.0.0 Jul 9, 2011
 * @version 1.0.0 Jul 9, 2011
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Settings {

   /**
    * The uri for the server API.
    * 
    * @author naikon
    * @since 1.0.0 Jul 9, 2011
    * @version 1.0.0 Jul 9, 2011
    * @return URI of a the server to which to calls the api.
    */
   String apiUri() default "";

   /**
    * The socket timeout value.
    * 
    * @author naikon
    * @since 1.0.0 Jul 9, 2011
    * @version 1.0.0 Jul 9, 2011
    * @return Value in milliseconds for network operations timeout (default 15000ms).
    */
   int socketTimeout() default 15000;

   /**
    * The socket buffer size.
    * 
    * @author naikon
    * @since 1.0.0 Jul 9, 2011
    * @version 1.0.0 Jul 9, 2011
    * @return the socketBufferSize value (default 8192bytes).
    */
   int socketBufferSize() default 8192;

   /**
    * The API value
    * 
    * @author naikon
    * @since 1.0.0 Jul 9, 2011
    * @version 1.0.0 Jul 9, 2011
    * @return the API value string
    */
   String apiValue() default "";

   /**
    * The API key
    * 
    * @author naikon
    * @since 1.0.0 Jul 9, 2011
    * @version 1.0.0 Jul 9, 2011
    * @return the API key
    */
   String apiKey() default "";

   /**
    * The database version
    * 
    * @author naikon
    * @since 1.0.0 Jul 9, 2011
    * @version 1.0.0 Jul 9, 2011
    * @return the database version as int
    */
   int databaseVersion() default 1;

   /**
    * The database name
    * 
    * @author naikon
    * @since 1.0.0 Jul 9, 2011
    * @version 1.0.0 Jul 9, 2011
    * @return the database name
    */
   String databaseName() default "heurigen-app";

   /**
    * Custom environment fields.
    * 
    * @author naikon
    * @since 1.0.0 Jul 10, 2011
    * @version 1.0.0 Jul 10, 2011
    * @return EnvironmentField Array listing the fields to be included in the environment reader.
    */
   AppEnvironmentField[] customEnvironmentContent() default {};

   /**
    * Sets the default GPS update interval in ms
    * 
    * @author naikon
    * @since 1.0.0 Jul 22, 2011
    * @version 1.0.0 Jul 22, 2011
    * @return the default GPS update interval in ms
    */
   int defaultGpsUpdateInterval() default 3000;

   /**
    * Sets the default GPS update distance in meters
    * 
    * @author naikon
    * @since 1.0.0 Jul 22, 2011
    * @version 1.0.0 Jul 22, 2011
    * @return the default GPS update distance meters
    */
   int defaultGpsUpdatedistance() default 10;
}