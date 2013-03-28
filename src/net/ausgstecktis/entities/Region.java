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
 * Region.java
 * 
 * @author naikon
 * @version Aug 26, 2011
 */
@DatabaseTable(tableName = Region.TABLE_NAME)
public class Region {

   public static final String TABLE_NAME = "region";
   public static final String ID_COLUMN = "id";
   public static final String NAME_COLUMN = "name";

   @DatabaseField(id = true, columnName = Region.ID_COLUMN)
   private Integer id;

   @DatabaseField(columnName = Region.NAME_COLUMN)
   private String name;

   /**
    * Instantiates a new region.
    */
   public Region() {
   }

   /**
    * Instantiates a new region.
    * 
    * @param id the id
    * @param name the name
    */
   public Region(final Integer id, final String name) {
      this.id = id;
      this.name = name;
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
    * Gets the name.
    * 
    * @return the name
    */
   public String getName() {
      return name;
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
    * Sets the name.
    * 
    * @param name the new name
    */
   public void setName(final String name) {
      this.name = name;
   }

}