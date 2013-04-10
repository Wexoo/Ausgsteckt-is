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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.ausgstecktis.ui.HeurigenApp;
import net.ausgstecktis.ui.HomeActivity;
import net.ausgstecktis.ui.search.SearchActivity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * @author wexoo
 * @version Aug 27, 2011
 */
public class UIUtils {

   public static final String TAG = UIUtils.class.getSimpleName();

   public static final SimpleDateFormat DEFAULT_DATE_FORMATTER = new SimpleDateFormat("dd.MMM yyyy HH:mm");
   public static final SimpleDateFormat DATE_WITHOUT_TIME_FORMATTER = new SimpleDateFormat("dd. MM. yyyy");
   public static final SimpleDateFormat DATE_WRITTEN_OUT_MONTH_FORMATTER = new SimpleDateFormat("dd. MMMM yyyy");

   /**
    * Invoke "home" action, returning to {@link HomeActivity}.
    */
   public static void goHome(final Context context) {
      final Intent intent = new Intent(context, HomeActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      context.startActivity(intent);
   }

   /**
    * Invoke "search" action, triggering a default search.
    */
   public static void onQuickSearchClick(final Context context) {
      final Intent intent = new Intent(context, SearchActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      context.startActivity(intent);
   }

   /**
    * Shows short toast using the HeurigenApp.mainContext variable
    */
   public static void showShortToast(final String text) {
      showShortToast(HeurigenApp.mainContext, text);
   }

   /**
    * Shows short toast using custom context
    */
   public static void showShortToast(final Context context, final String text) {
      Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
   }

   /**
    * Shows long toast using the HeurigenApp.mainContext variable
    */
   public static void showLongToast(final String text) {
      showLongToast(HeurigenApp.mainContext, text);
   }

   /**
    * Shows long toast using custom context
    */
   public static void showLongToast(final Context context, final String text) {
      Toast.makeText(context, text, Toast.LENGTH_LONG).show();
   }

   public static String getDateAsString(final Date date) {
      return DEFAULT_DATE_FORMATTER.format(date);
   }

   public static String getDateWithoutTimeAsString(final Date date) {
      return DATE_WITHOUT_TIME_FORMATTER.format(date);
   }

   public static String getDateWrittenOutMonthAsString(final Date date) {
      return DATE_WRITTEN_OUT_MONTH_FORMATTER.format(date);
   }

   /**
    * Gets the string as date.
    * 
    * @param dateString the date string
    * @return the string as date
    */
   public static Date getStringAsDate(final String dateString) {
      return getStringAsDate(dateString, new SimpleDateFormat("dd. MMMM yyyy HH:mm"));
   }

   public static Date getStringAsDate(final String dateString, final SimpleDateFormat format) {
      try {
         return format.parse(dateString);
      } catch (final ParseException e) {
         Log.e(UIUtils.TAG, e.getMessage());
         e.printStackTrace();
      }
      return null;
   }

   public static String addLeadingZeroToLong(final Long value) {
      return value < 10 ? "0" + value : value.toString();
   }

}
