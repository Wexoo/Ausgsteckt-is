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

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * The Class OpenTime.
 * 
 * @author wexoo, naikon
 * @version Aug 26, 2011
 */
@DatabaseTable(tableName = OpenTime.TABLE_NAME)
public class OpenTime {

   public static final String TABLE_NAME = "open_time";
   public static final String ID_COLUMN = "id";
   public static final String START_COLUMN = "start";
   public static final String END_COLUMN = "end";

   @DatabaseField(id = true, columnName = OpenTime.ID_COLUMN)
   private Integer id;

   @DatabaseField(columnName = OpenTime.START_COLUMN, format = DALUtils.DEFAULT_TIME_FORMAT, dataType = DataType.DATE_STRING)
   private Date start;

   @DatabaseField(columnName = OpenTime.END_COLUMN, format = DALUtils.DEFAULT_TIME_FORMAT, dataType = DataType.DATE_STRING)
   private Date end;

   public OpenTime() {
   }

   /**
    * Instantiates a new open time.
    * 
    * @param id the id
    * @param start the start
    * @param end the end
    */
   public OpenTime(final Integer id, final Date start, final Date end) {
      this.id = id;
      this.start = start;
      this.end = end;
   }

   /**
    * Gets the id.
    * 
    * @return the id
    */
   public Integer getId() {
      return id;
   }

   /**
    * Gets the start.
    * 
    * @return the start
    */
   public Date getStart() {
      return start;
   }

   /**
    * Gets the end.
    * 
    * @return the end
    */
   public Date getEnd() {
      return end;
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
    * Sets the start.
    * 
    * @param start the new start
    */
   public void setStart(final Date start) {
      this.start = start;
   }

   /**
    * Sets the end.
    * 
    * @param end the new end
    */
   public void setEnd(final Date end) {
      this.end = end;
   }
}