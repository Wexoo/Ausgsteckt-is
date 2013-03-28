/***
 * Copyright (C) 2011 naikon, wexoo android@geekosphere.org
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package net.ausgstecktis.ui;

import net.ausgstecktis.R;
import android.os.Bundle;
import android.widget.TextView;

/**
 * InfoActivity.java
 * 
 * @author naikon, wexoo
 * @version Aug 27, 2011
 */
public class InfoActivity extends SuperActivity {

   /**
    * {@inheritDoc}
    * 
    * @see net.ausgstecktis.ui.SuperActivity#onCreate(android.os.Bundle)
    */
   @Override
   protected void onCreate(final Bundle savedInstanceState) {
      this.setContentView(R.layout.activity_info);
      super.onCreate(savedInstanceState);

      if (!PreferencesActivity.getStringPreference(R.string.last_database_migration_key, null).equals(""))
         ((TextView) findViewById(R.id.tv_local_database_version)).setText(PreferencesActivity.getStringPreference(
               R.string.last_database_migration_key, null));
      else
         ((TextView) findViewById(R.id.tv_local_database_version)).setText(R.string.info_no_database);

      if (!PreferencesActivity.getStringPreference(R.string.database_migration_duration_key, null).equals(""))
         ((TextView) findViewById(R.id.tv_duration_last_update)).setText(PreferencesActivity.getStringPreference(
               R.string.database_migration_duration_key, null) + " " + getText(R.string.time_entity));
      else
         ((TextView) findViewById(R.id.tv_duration_last_update)).setText(R.string.info_no_database);
   }
}