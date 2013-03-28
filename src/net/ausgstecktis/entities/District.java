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
 * The Class District.
 * 
 * @author naikon
 * @version Aug 26, 2011
 */
@DatabaseTable(tableName = District.TABLE_NAME)
public class District {

   public static final String TABLE_NAME = "district";
   public static final String ID_COLUMN = "id";
   public static final String KBZ_COLUMN = "kbz";
   public static final String NAME_COLUMN = "name";

   @DatabaseField(id = true, columnName = District.ID_COLUMN)
   private Integer id;

   @DatabaseField(columnName = District.KBZ_COLUMN)
   private String kbz;

   @DatabaseField(columnName = District.NAME_COLUMN)
   private String name;

   /**
    * Table names online and offline.
    */

   public District() {
   }

   /**
    * Instantiates a new district.
    * 
    * @param id the id
    * @param kbz the kbz
    * @param name the name
    */
   public District(final Integer id, final String kbz, final String name) {
      this.id = id;
      this.kbz = kbz;
      this.name = name;
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
    * Gets the kbz.
    * 
    * @return the kbz
    */
   public String getKbz() {
      return this.kbz;
   }

   /**
    * Gets the name.
    * 
    * @return the name
    */
   public String getName() {
      return this.name;
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
    * Sets the kbz.
    * 
    * @param kbz the new kbz
    */
   public void setKbz(final String kbz) {
      this.kbz = kbz;
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