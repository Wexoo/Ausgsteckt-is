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

import net.ausgstecktis.R;
import net.ausgstecktis.ui.PreferencesActivity;
import net.wexoo.organicdroid.Log;

/**
 * A factory for creating Proxy objects.
 * 
 * @author wexoo
 */
public class ProxyFactory {

   private static final String TAG = ProxyFactory.class.getSimpleName();

   /** Dynamic configuration value - changeable during runtime. */
   public static boolean ACCESS_MODE_CHANGED = false;

   private static AbstractProxy proxy;

   /**
    * Checks which proxy to return
    * 
    * @author wexoo
    */
   public static AbstractProxy getProxy() {
      if (ProxyFactory.proxy == null || ProxyFactory.ACCESS_MODE_CHANGED) {
         if (!PreferencesActivity.getStringPreference(R.string.lp_access_mode_key, null).equals("offline")) {
            Log.d(ProxyFactory.TAG, "New online proxy created!");
            ProxyFactory.proxy = new OnlineProxy();
         } else {
            Log.d(ProxyFactory.TAG, "New offline proxy created!");
            ProxyFactory.proxy = new OfflineProxy();
         }
         ProxyFactory.ACCESS_MODE_CHANGED = false;
      }
      Log.d(ProxyFactory.TAG, "Accessing " + ProxyFactory.proxy.getClass() + "!");
      return ProxyFactory.proxy;
   }
}