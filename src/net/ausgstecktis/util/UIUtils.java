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

import net.ausgstecktis.ui.HeurigenApp;
import net.ausgstecktis.ui.HomeActivity;
import net.ausgstecktis.ui.search.SearchActivity;
import net.wexoo.organicdroid.util.UIUtil;
import android.content.Context;

/**
 * Convenience collection for OrganicDroid methods
 * 
 * @author wexoo
 */
public class UIUtils {

   /**
    * Invoke "home" action, returning to {@link HomeActivity}.
    */
   public static void goHome(final Context context) {
      UIUtil.goToActivityAndClearTop(context, HomeActivity.class);
   }

   /**
    * Invoke "search" action, triggering a default search.
    */
   public static void onQuickSearchClick(final Context context) {
      UIUtil.goToActivityAndClearTop(context, SearchActivity.class);
   }

   /**
    * Shows short toast using the HeurigenApp.mainContext variable
    */
   public static void showShortToast(final String text) {
      UIUtil.showShortToast(HeurigenApp.mainContext, text);
   }

   /**
    * Shows long toast using the HeurigenApp.mainContext variable
    */
   public static void showLongToast(final String text) {
      UIUtil.showLongToast(HeurigenApp.mainContext, text);
   }
}