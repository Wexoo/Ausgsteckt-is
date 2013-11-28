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
 * @author wexoo
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

   public Region() {
   }

   public Region(final Integer id, final String name) {
      this.id = id;
      this.name = name;
   }

   public Integer getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public void setId(final Integer id) {
      this.id = id;
   }

   public void setName(final String name) {
      this.name = name;
   }
}