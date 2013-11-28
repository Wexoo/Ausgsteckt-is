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

import net.ausgstecktis.DAL.util.DALUtils;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * OpeningCalendar.java
 * 
 * @author wexoo
 */
@DatabaseTable(tableName = OpeningCalendar.TABLE_NAME)
public class OpeningCalendar {

   public static final String TABLE_NAME = "calendar";
   public static final String ID_COLUMN = "id";
   public static final String HEURIGER_COLUMN = "id_heuriger";
   public static final String START_COLUMN = "start";
   public static final String END_COLUMN = "end";

   @DatabaseField(id = true, columnName = OpeningCalendar.ID_COLUMN)
   private Integer id;

   @DatabaseField(canBeNull = false, foreign = true, columnName = OpeningCalendar.HEURIGER_COLUMN)
   private Heuriger heuriger;

   @DatabaseField(columnName = OpeningCalendar.START_COLUMN, format = DALUtils.DEFAULT_DATE_FORMAT)
   private Date start;

   @DatabaseField(columnName = OpeningCalendar.END_COLUMN, format = DALUtils.DEFAULT_DATE_FORMAT)
   private Date end;

   public OpeningCalendar() {
   }

   public OpeningCalendar(final Integer id, final Heuriger heuriger, final Date start, final Date end) {
      this.id = id;
      this.heuriger = heuriger;
      this.start = start;
      this.end = end;
   }

   public Integer getId() {
      return id;
   }

   public Heuriger getHeuriger() {
      return heuriger;
   }

   public Date getStart() {
      return start;
   }

   public Date getEnd() {
      return end;
   }

   public void setId(final Integer id) {
      this.id = id;
   }

   public void setHeuriger(final Heuriger heuriger) {
      this.heuriger = heuriger;
   }

   public void setStart(final Date start) {
      this.start = start;
   }

   public void setEnd(final Date end) {
      this.end = end;
   }
}