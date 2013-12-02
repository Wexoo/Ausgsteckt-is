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

import net.wexoo.organicdroid.base.BaseApplication;
import net.wexoo.organicdroid.settings.Settings;

/**
 * HeurigenApp.java
 * Main Class for the app
 * Sets also specific settings as annotations for the app
 * 
 * @author wexoo, naikon
 */
@Settings(
      apiUri = "http://dev.ausgstecktis.net/app/",
      apiValue = "apikey",
      apiKey = "foo",
      databaseName = "heurigen-app",
      databaseVersion = 1)
public class HeurigenApp extends BaseApplication {

   public static boolean MIGRATION_IN_PROGRESS = false;
   public static boolean CHECK_IN_PROGRESS = false;
   public static boolean CHECK_FOR_UPDATE = true;
}