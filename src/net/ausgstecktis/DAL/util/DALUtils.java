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

package net.ausgstecktis.DAL.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * DALUtils.java
 * 
 * @author wexoo
 */
public class DALUtils {

   public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
   public static final SimpleDateFormat DEFAULT_DATE_FORMATTER =
         new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault());
   public static final String GERMAN_DATE_FORMAT = "dd.MM yyyy";
   public static final SimpleDateFormat GERMAN_DATE_FORMATTER = new SimpleDateFormat(GERMAN_DATE_FORMAT,
         Locale.getDefault());
   public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
   public static final SimpleDateFormat DEFAULT_TIME_FORMATTER = new SimpleDateFormat(DEFAULT_TIME_FORMAT,
         Locale.getDefault());

   public static Long getLongValueOfString(final String value) {
      try {
         return checkStringForNull(value) ? 0L : Long.valueOf(value.replaceAll(
               "[^\\d]", "").replaceAll(" ", ""));
      } catch (final NumberFormatException e) {
         e.printStackTrace();
      }
      return null;
   }

   public static boolean getBooleanValueOfString(final String value) {
      try {
         return value != null && value.equals("1") ? true : false;
      } catch (final Exception e) {
         e.printStackTrace();
      }
      return false;
   }

   public static Integer getIntegerValueOfString(final String value) {
      try {
         return checkStringForNull(value) ? 0 : Integer.valueOf(value);
      } catch (final Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   public static Double getDoubleValueOfString(final String value) {
      try {
         return checkStringForNull(value) ? 0.0 : Double.valueOf(value);
      } catch (final NumberFormatException e) {
         e.printStackTrace();
      }
      return null;
   }

   public static Date getDefaultDateValueOfString(final String value) {
      try {
         return checkStringForNull(value) ? null : DALUtils.DEFAULT_DATE_FORMATTER
               .parse(value);
      } catch (final ParseException e) {
         e.printStackTrace();
      }
      return null;
   }

   public static Date getTimeValueOfString(final String value) {
      try {
         return checkStringForNull(value) ? null : DALUtils.DEFAULT_TIME_FORMATTER
               .parse(value);
      } catch (final ParseException e) {
         e.printStackTrace();
      }
      return null;
   }

   private static boolean checkStringForNull(final String value) {
      return value == null || value.equals("") || value.equals("null");
   }

   public static String getStringValueOfDefaultDate(final Date value) {
      return value == null ? null : DALUtils.DEFAULT_DATE_FORMATTER.format(value);
   }
}