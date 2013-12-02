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

import net.wexoo.organicdroid.convert.DateAndTimeConverter;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * DayIncluded.java
 * 
 * @author wexoo
 */
@DatabaseTable(tableName = DayIncluded.TABLE_NAME)
public class DayIncluded {

   public static final String TABLE_NAME = "day_included";
   public static final String ID_COLUMN = "id";
   public static final String HEURIGER_COLUMN = "id_heuriger";
   public static final String DAY_COLUMN = "day";

   @DatabaseField(id = true, columnName = DayIncluded.ID_COLUMN)
   private Integer id;

   @DatabaseField(canBeNull = false, foreign = true, columnName = DayIncluded.HEURIGER_COLUMN)
   private Heuriger heuriger;

   @DatabaseField(columnName = DayIncluded.DAY_COLUMN, format = DateAndTimeConverter.FILE_DATE_FORMAT)
   private Date day;

   public DayIncluded() {
   }

   public DayIncluded(final Integer id, final Heuriger heuriger, final Date day) {
      this.id = id;
      this.heuriger = heuriger;
      this.day = day;
   }

   public Integer getId() {
      return id;
   }

   public Heuriger getHeuriger() {
      return heuriger;
   }

   public Date getDay() {
      return day;
   }

   public void setId(final Integer id) {
      this.id = id;
   }

   public void setHeuriger(final Heuriger heuriger) {
      this.heuriger = heuriger;
   }

   public void setDay(final Date day) {
      this.day = day;
   }
}