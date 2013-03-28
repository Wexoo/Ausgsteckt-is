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

package net.ausgstecktis.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * The Class OpenDay.
 * 
 * @author naikon
 * @version Aug 26, 2011
 */
@DatabaseTable(tableName = OpenDay.TABLE_NAME)
public class OpenDay {

   public static final String TABLE_NAME = "open_day";
   public static final String ID_COLUMN = "id";
   public static final String CALENDAR_COLUMN = "id_calendar";
   public static final String OPEN_TIME_COLUMN = "id_open_time";
   public static final String DAY_COLUMN = "day";

   @DatabaseField(id = true, columnName = OpenDay.ID_COLUMN)
   private Integer id;

   @DatabaseField(canBeNull = false, foreign = true, columnName = OpenDay.CALENDAR_COLUMN)
   private OpeningCalendar calendar;

   @DatabaseField(canBeNull = false, foreign = true, columnName = OpenDay.OPEN_TIME_COLUMN)
   private OpenTime openTime;

   /** General open days - value from 1 (Monday) to 7 (Sunday). */
   @DatabaseField(columnName = OpenDay.DAY_COLUMN)
   private Integer dayOfTheWeek;

   /**
    * Instantiates a new open day.
    */
   public OpenDay() {
   }

   /**
    * Instantiates a new open day.
    * 
    * @param id the id
    * @param calendar the calendar
    * @param openTime the open time
    * @param dayOfTheWeek the day of the week
    */
   public OpenDay(final Integer id, final OpeningCalendar calendar, final OpenTime openTime, final Integer dayOfTheWeek) {
      this.id = id;
      this.calendar = calendar;
      this.openTime = openTime;
      this.dayOfTheWeek = dayOfTheWeek;
   }

   /**
    * Gets the id.
    * 
    * @return the id
    */
   public Integer getId() {
      return this.id;
   }

   /**
    * Gets the calendar.
    * 
    * @return the calendar
    */
   public OpeningCalendar getCalendar() {
      return this.calendar;
   }

   /**
    * Gets the open time.
    * 
    * @return the open time
    */
   public OpenTime getOpenTime() {
      return this.openTime;
   }

   /**
    * Gets the day of the week.
    * 
    * @return the day of the week
    */
   public Integer getDayOfTheWeek() {
      return this.dayOfTheWeek;
   }

   /**
    * Sets the id.
    * 
    * @param id the new id
    */
   public void setId(final Integer id) {
      this.id = id;
   }

   /**
    * Sets the calendar.
    * 
    * @param calendar the new calendar
    */
   public void setCalendar(final OpeningCalendar calendar) {
      this.calendar = calendar;
   }

   /**
    * Sets the open time.
    * 
    * @param openTime the new open time
    */
   public void setOpenTime(final OpenTime openTime) {
      this.openTime = openTime;
   }

   /**
    * Sets the day of the week.
    * 
    * @param dayOfTheWeek the new day of the week
    */
   public void setDayOfTheWeek(final Integer dayOfTheWeek) {
      this.dayOfTheWeek = dayOfTheWeek;
   }
}