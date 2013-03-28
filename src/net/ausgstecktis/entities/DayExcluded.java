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

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * The Class DayExcluded.
 * 
 * @author naikon
 * @version Aug 26, 2011
 */
@DatabaseTable(tableName = DayExcluded.TABLE_NAME)
public class DayExcluded {

   public static final String TABLE_NAME = "day_excluded";
   public static final String ID_COLUMN = "id";
   public static final String HEURIGER_COLUMN = "id_heuriger";
   public static final String DAY_COLUMN = "day";

   @DatabaseField(id = true, columnName = DayExcluded.ID_COLUMN)
   private Integer id;

   @DatabaseField(canBeNull = false, foreign = true, columnName = DayExcluded.HEURIGER_COLUMN)
   private Heuriger heuriger;

   @DatabaseField(columnName = DayExcluded.DAY_COLUMN)
   private Date day;

   /**
    * Instantiates a new day excluded.
    */
   public DayExcluded() {
   }

   /**
    * Instantiates a new day excluded.
    * 
    * @param id the id
    * @param heuriger the heuriger
    * @param day the day
    */
   public DayExcluded(final Integer id, final Heuriger heuriger, final Date day) {
      this.id = id;
      this.heuriger = heuriger;
      this.day = day;
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
    * Gets the heuriger.
    * 
    * @return the heuriger
    */
   public Heuriger getHeuriger() {
      return this.heuriger;
   }

   /**
    * Gets the day.
    * 
    * @return the day
    */
   public Date getDay() {
      return this.day;
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
    * Sets the heuriger.
    * 
    * @param heuriger the new heuriger
    */
   public void setHeuriger(final Heuriger heuriger) {
      this.heuriger = heuriger;
   }

   /**
    * Sets the day.
    * 
    * @param day the new day
    */
   public void setDay(final Date day) {
      this.day = day;
   }
}