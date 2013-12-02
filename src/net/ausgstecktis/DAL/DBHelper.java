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

import java.util.ArrayList;
import java.util.Arrays;

import net.ausgstecktis.entities.City;
import net.ausgstecktis.entities.DayExcluded;
import net.ausgstecktis.entities.DayIncluded;
import net.ausgstecktis.entities.District;
import net.ausgstecktis.entities.Heuriger;
import net.ausgstecktis.entities.OpenDay;
import net.ausgstecktis.entities.OpenTime;
import net.ausgstecktis.entities.OpeningCalendar;
import net.ausgstecktis.entities.Region;
import net.wexoo.organicdroidormlite.base.BaseDBHelper;
import android.content.Context;

/**
 * DBHelper.java
 * 
 * @author naikon, wexoo
 */
public class DBHelper extends BaseDBHelper {

   protected static final String TAG = DBHelper.class.getSimpleName();

   public DBHelper(Context context) {
      super(context);
   }

   @Override
   protected ArrayList<Class<?>> getEntityClasses(ArrayList<Class<?>> arrayList) {
      Class<?>[] test = {Heuriger.class, Region.class, City.class, District.class, OpeningCalendar.class,
            DayExcluded.class, DayIncluded.class, OpenDay.class, OpenTime.class};
      arrayList.addAll(Arrays.asList(test));
      return super.getEntityClasses(arrayList);
   }
}