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
 * Heuriger.java
 * 
 * @author wexoo
 * @version Aug 26, 2011
 */
@DatabaseTable(tableName = Heuriger.TABLE_NAME)
public class Heuriger {

   public static final class KEYWORDS {
      public static final String NEUNTEUFEL = "Buschenschank Neunteufel";
   }

   public static final String TABLE_NAME = "heuriger";
   public static final String ID_COLUMN = "id";
   public static final String NAME_COLUMN = "name";
   public static final String SORT_NAME_COLUMN = "sort_name";
   public static final String STREET_COLUMN = "street";
   public static final String STREETNUMBER_COLUMN = "streetnumber";
   public static final String CITY_COLUMN = "id_city";
   public static final String PHONE_COLUMN = "phone";
   public static final String PHONE2_COLUMN = "phone2";
   public static final String FAX_COLUMN = "fax";
   public static final String EMAIL_COLUMN = "email";
   public static final String WEBSITE_COLUMN = "website";
   public static final String INDOOR_COLUMN = "indoor";
   public static final String OUTDOOR_COLUMN = "outdoor";
   public static final String DESCRIPTION_COLUMN = "description";
   public static final String OPENING_COLUMN = "opening";
   public static final String LONGITUDE_COLUMN = "longitude";
   public static final String LATITUDE_COLUMN = "latitude";
   public static final String FAVORITE_COLUMN = "favorite";
   public static final String TOP_COLUMN = "top";

   @DatabaseField(id = true, columnName = Heuriger.ID_COLUMN)
   private Integer id;

   @DatabaseField(columnName = Heuriger.NAME_COLUMN)
   private String name;

   @DatabaseField(columnName = Heuriger.SORT_NAME_COLUMN)
   private String sort_name;

   @DatabaseField(columnName = Heuriger.STREET_COLUMN)
   private String street;

   @DatabaseField(columnName = Heuriger.STREETNUMBER_COLUMN)
   private Integer streetNumber;

   @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = Heuriger.CITY_COLUMN)
   private City city;

   @DatabaseField(columnName = Heuriger.PHONE_COLUMN)
   private Long phone;

   @DatabaseField(columnName = Heuriger.PHONE2_COLUMN)
   private Long phone2;

   @DatabaseField(columnName = Heuriger.FAX_COLUMN)
   private Long fax;

   @DatabaseField(columnName = Heuriger.EMAIL_COLUMN)
   private String mail;

   @DatabaseField(columnName = Heuriger.WEBSITE_COLUMN)
   private String website;

   @DatabaseField(columnName = Heuriger.INDOOR_COLUMN)
   private Integer indoor;

   @DatabaseField(columnName = Heuriger.OUTDOOR_COLUMN)
   private Integer outdoor;

   @DatabaseField(columnName = Heuriger.DESCRIPTION_COLUMN)
   private String description;

   @DatabaseField(columnName = Heuriger.OPENING_COLUMN)
   private String opening;

   @DatabaseField(columnName = Heuriger.LONGITUDE_COLUMN)
   private Double longitude;

   @DatabaseField(columnName = Heuriger.LATITUDE_COLUMN)
   private Double latitude;

   @DatabaseField(columnName = Heuriger.TOP_COLUMN)
   private boolean top = false;

   @DatabaseField(columnName = Heuriger.FAVORITE_COLUMN)
   private boolean favorite = false;

   private Double distance;

   public Heuriger() {
   }

   public Heuriger(final Integer id) {
      this.id = id;
   }

   public Heuriger(final Integer id, final Boolean favorite) {
      this.id = id;
      this.favorite = favorite;
   }

   public Heuriger(final String name, final Long phone, final String mail) {
      this.name = name;
      this.phone = phone;
      this.mail = mail;
   }

   public Heuriger(final Integer id, final String name, final String sort_name, final String street,
         final Integer streetNumber, final City city, final Long phone,
         final Long fax, final String mail, final String website, final Integer indoor,
         final Integer outdoor, final String description, final String opening,
         final Double longitude, final Double latitude) {
      super();
      this.id = id;
      this.name = name;
      this.sort_name = sort_name;
      this.street = street;
      this.streetNumber = streetNumber;
      this.city = city;
      this.phone = phone;
      this.fax = fax;
      this.mail = mail;
      this.website = website;
      this.indoor = indoor;
      this.outdoor = outdoor;
      this.description = description;
      this.opening = opening;
      this.longitude = longitude;
      this.latitude = latitude;
   }

   public Heuriger(Integer id, String name, String sort_name, String street, Integer streetNumber, City city,
         Long phone, Long phone2, Long fax, String mail, String website, Integer indoor, Integer outdoor,
         String description, String opening, Double longitude, Double latitude, boolean top, boolean favorite,
         Double distance) {
      super();
      this.id = id;
      this.name = name;
      this.sort_name = sort_name;
      this.street = street;
      this.streetNumber = streetNumber;
      this.city = city;
      this.phone = phone;
      this.phone2 = phone2;
      this.fax = fax;
      this.mail = mail;
      this.website = website;
      this.indoor = indoor;
      this.outdoor = outdoor;
      this.description = description;
      this.opening = opening;
      this.longitude = longitude;
      this.latitude = latitude;
      this.top = top;
      this.favorite = favorite;
      this.distance = distance;
   }

   public Heuriger(final Integer id, final String name, final String sort_name, final String street,
         final Integer streetNumber, final City city, final Long phone,
         final Long fax, final String mail, final String website, final Integer indoor,
         final Integer outdoor, final String description, final String opening,
         final Double longitude, final Double latitude, final Double distance, final Boolean favorite) {
      super();
      this.id = id;
      this.name = name;
      this.sort_name = sort_name;
      this.street = street;
      this.streetNumber = streetNumber;
      this.city = city;
      this.phone = phone;
      this.fax = fax;
      this.mail = mail;
      this.website = website;
      this.indoor = indoor;
      this.outdoor = outdoor;
      this.description = description;
      this.opening = opening;
      this.longitude = longitude;
      this.latitude = latitude;
      this.distance = distance;
      this.favorite = favorite;
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
    * Sets the id.
    * 
    * @param id the new id
    */
   public void setId(final Integer id) {
      this.id = id;
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
    * Sets the name.
    * 
    * @param name the new name
    */
   public void setName(final String name) {
      this.name = name;
   }

   /**
    * Gets the sort name.
    * 
    * @return the sort name
    */
   public String getSortName() {
      return sort_name;
   }

   /**
    * Sets the sort name.
    * 
    * @param sort_name the new sort name
    */
   public void setSortName(final String sort_name) {
      this.sort_name = sort_name;
   }

   /**
    * Gets the street.
    * 
    * @return the street
    */
   public String getStreet() {
      return street;
   }

   /**
    * Sets the street.
    * 
    * @param street the new street
    */
   public void setStreet(final String street) {
      this.street = street;
   }

   /**
    * Gets the street number.
    * 
    * @return the street number
    */
   public Integer getStreetNumber() {
      return streetNumber;
   }

   /**
    * Sets the street number.
    * 
    * @param streetNumber the new street number
    */
   public void setStreetNumber(final Integer streetNumber) {
      this.streetNumber = streetNumber;
   }

   /**
    * Gets the city.
    * 
    * @return the city
    */
   public City getCity() {
      return city;
   }

   /**
    * Sets the city.
    * 
    * @param city the new city
    */
   public void setCity(final City city) {
      this.city = city;
   }

   /**
    * Gets the phone.
    * 
    * @return the phone
    */
   public Long getPhone() {
      return phone;
   }

   /**
    * Sets the phone.
    * 
    * @param phone the new phone
    */
   public void setPhone(final Long phone) {
      this.phone = phone;
   }

   /**
    * Sets the phone2.
    * 
    * @param phone2 the new phone2
    */
   public void setPhone2(final Long phone2) {
      this.phone2 = phone2;
   }

   /**
    * Gets the phone2.
    * 
    * @return the phone2
    */
   public Long getPhone2() {
      return phone2;
   }

   /**
    * Gets the fax.
    * 
    * @return the fax
    */
   public Long getFax() {
      return fax;
   }

   /**
    * Sets the fax.
    * 
    * @param fax the new fax
    */
   public void setFax(final Long fax) {
      this.fax = fax;
   }

   /**
    * Gets the mail.
    * 
    * @return the mail
    */
   public String getMail() {
      return mail;
   }

   /**
    * Sets the mail.
    * 
    * @param mail the new mail
    */
   public void setMail(final String mail) {
      this.mail = mail;
   }

   /**
    * Gets the website.
    * 
    * @return the website
    */
   public String getWebsite() {
      return website;
   }

   /**
    * Sets the website.
    * 
    * @param website the new website
    */
   public void setWebsite(final String website) {
      this.website = website;
   }

   /**
    * Gets the indoor.
    * 
    * @return the indoor
    */
   public Integer getIndoor() {
      return indoor;
   }

   /**
    * Sets the indoor.
    * 
    * @param indoor the new indoor
    */
   public void setIndoor(final Integer indoor) {
      this.indoor = indoor;
   }

   /**
    * Gets the outdoor.
    * 
    * @return the outdoor
    */
   public Integer getOutdoor() {
      return outdoor;
   }

   /**
    * Sets the outdoor.
    * 
    * @param outdoor the new outdoor
    */
   public void setOutdoor(final Integer outdoor) {
      this.outdoor = outdoor;
   }

   /**
    * Gets the description.
    * 
    * @return the description
    */
   public String getDescription() {
      return description;
   }

   /**
    * Sets the description.
    * 
    * @param description the new description
    */
   public void setDescription(final String description) {
      this.description = description;
   }

   /**
    * Gets the opening.
    * 
    * @return the opening
    */
   public String getOpening() {
      return opening;
   }

   /**
    * Sets the opening.
    * 
    * @param opening the new opening
    */
   public void setOpening(final String opening) {
      this.opening = opening;
   }

   /**
    * Gets the longitude.
    * 
    * @return the longitude
    */
   public Double getLongitude() {
      return longitude;
   }

   /**
    * Sets the longitude.
    * 
    * @param longitude the new longitude
    */
   public void setLongitude(final Double longitude) {
      this.longitude = longitude;
   }

   /**
    * Gets the latitude.
    * 
    * @return the latitude
    */
   public Double getLatitude() {
      return latitude;
   }

   /**
    * Sets the latitude.
    * 
    * @param latitude the new latitude
    */
   public void setLatitude(final Double latitude) {
      this.latitude = latitude;
   }

   /**
    * Gets the favorite.
    * 
    * @return the favorite
    */
   public boolean getFavorite() {
      return favorite;
   }

   /**
    * Sets the favorite.
    * 
    * @param favorite the new favorite
    */
   public void setFavorite(final boolean favorite) {
      this.favorite = favorite;
   }

   /**
    * Checks if is top.
    * 
    * @return true, if is top
    */
   public boolean isTop() {
      return top;
   }

   /**
    * Sets the top.
    * 
    * @param top the new top
    */
   public void setTop(final boolean top) {
      this.top = top;
   }

   /**
    * Gets the distance.
    * 
    * @return the distance
    */
   public Double getDistance() {
      return distance;
   }

   /**
    * Sets the distance.
    * 
    * @param distance the new distance
    */
   public void setDistance(final Double distance) {
      this.distance = distance;
   }
}