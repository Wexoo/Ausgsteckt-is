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
 * @author wexoo
 */
@DatabaseTable(tableName = City.TABLE_NAME)
public class City {

   public static final String TABLE_NAME = "city";
   public static final String ID_COLUMN = "id";
   public static final String NAME_COLUMN = "name";
   public static final String ZIPCODE_COLUMN = "zipcode";
   public static final String DISTRICT_COLUMN = "id_district";
   public static final String REGION_COLUMN = "id_region";
   private Integer amount;

   @DatabaseField(id = true, columnName = City.ID_COLUMN)
   private Integer id;

   @DatabaseField(columnName = City.NAME_COLUMN)
   private String name;

   @DatabaseField(columnName = City.ZIPCODE_COLUMN)
   private Integer zipCode;

   @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = City.DISTRICT_COLUMN)
   private District district;

   @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = City.REGION_COLUMN)
   private Region region;

   public City() {
   }

   public City(Integer id) {
      this.id = id;
   }

   public City(final Integer id, final String name, final Integer zipCode) {
      this.id = id;
      this.name = name;
      this.zipCode = zipCode;
   }

   public City(final Integer id, final String name, final Integer zipCode, final Integer amount) {
      this.id = id;
      this.name = name;
      this.zipCode = zipCode;
      this.amount = amount;
   }

   public City(Integer id, String name, Integer zipCode, District district, Region region) {
      super();
      this.id = id;
      this.name = name;
      this.zipCode = zipCode;
      this.district = district;
      this.region = region;
   }

   public Integer getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   /**
    * Gets the zip code.
    * 
    * @return the zip code
    */
   public Integer getZipCode() {
      return zipCode;
   }

   /**
    * Gets the district.
    * 
    * @return the district
    */
   public District getDistrict() {
      return district;
   }

   /**
    * Gets the region.
    * 
    * @return the region
    */
   public Region getRegion() {
      return region;
   }

   /**
    * Gets the amount.
    * 
    * @return the amount
    */
   public Integer getAmount() {
      return amount;
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

   /**
    * Sets the zip code.
    * 
    * @param zipCode the new zip code
    */
   public void setZipCode(final Integer zipCode) {
      this.zipCode = zipCode;
   }

   /**
    * Sets the district.
    * 
    * @param district the new district
    */
   public void setDistrict(final District district) {
      this.district = district;
   }

   /**
    * Sets the region.
    * 
    * @param region the new region
    */
   public void setRegion(final Region region) {
      this.region = region;
   }

   /**
    * Sets the amount.
    * 
    * @param count the new amount
    */
   public void setAmount(final Integer count) {
      amount = count;
   }
}