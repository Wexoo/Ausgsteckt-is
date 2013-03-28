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

import android.os.AsyncTask;

/**
 * @author wexoo
 * @version Aug 27, 2011
 */
public abstract class AbstractAsyncTask extends AsyncTask<Void, Integer, Void> {

   // maybe put updating of progressbar and refresh icon into this class

   /**
    * {@inheritDoc}
    * 
    * @see android.os.AsyncTask#doInBackground(Params[])
    */
   
   protected abstract Void doInBackground(Void... params);
}