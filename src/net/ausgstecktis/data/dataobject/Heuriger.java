/**
 * 
 */
package net.ausgstecktis.data.dataobject;

import net.ausgstecktis.entities.City;

/**
 * @author siyb
 */
public class Heuriger {
   private City city;

   private int _id;
   private int streetNumber;
   private int indoor;
   private int outdoor;
   private int description;

   private String name;
   private String sort_name;
   private String street;
   private String phone;
   private String phone2;
   private String fax;
   private String mail;
   private String website;

   private String opening;
   private double longitude;
   private double latitude;
   private double distance;

   private boolean top;
   private boolean favorite;

   public City getCity() {
      return city;
   }

   public void setCity(City city) {
      this.city = city;
   }

   public int get_id() {
      return _id;
   }

   public void set_id(int _id) {
      this._id = _id;
   }

   public int getStreetNumber() {
      return streetNumber;
   }

   public void setStreetNumber(int streetNumber) {
      this.streetNumber = streetNumber;
   }

   public int getIndoor() {
      return indoor;
   }

   public void setIndoor(int indoor) {
      this.indoor = indoor;
   }

   public int getOutdoor() {
      return outdoor;
   }

   public void setOutdoor(int outdoor) {
      this.outdoor = outdoor;
   }

   public int getDescription() {
      return description;
   }

   public void setDescription(int description) {
      this.description = description;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getSort_name() {
      return sort_name;
   }

   public void setSort_name(String sort_name) {
      this.sort_name = sort_name;
   }

   public String getStreet() {
      return street;
   }

   public void setStreet(String street) {
      this.street = street;
   }

   public String getPhone() {
      return phone;
   }

   public void setPhone(String phone) {
      this.phone = phone;
   }

   public String getPhone2() {
      return phone2;
   }

   public void setPhone2(String phone2) {
      this.phone2 = phone2;
   }

   public String getFax() {
      return fax;
   }

   public void setFax(String fax) {
      this.fax = fax;
   }

   public String getMail() {
      return mail;
   }

   public void setMail(String mail) {
      this.mail = mail;
   }

   public String getWebsite() {
      return website;
   }

   public void setWebsite(String website) {
      this.website = website;
   }

   public String getOpening() {
      return opening;
   }

   public void setOpening(String opening) {
      this.opening = opening;
   }

   public double getLongitude() {
      return longitude;
   }

   public void setLongitude(double longitude) {
      this.longitude = longitude;
   }

   public double getLatitude() {
      return latitude;
   }

   public void setLatitude(double latitude) {
      this.latitude = latitude;
   }

   public double getDistance() {
      return distance;
   }

   public void setDistance(double distance) {
      this.distance = distance;
   }

   public boolean isTop() {
      return top;
   }

   public void setTop(boolean top) {
      this.top = top;
   }

   public boolean isFavorite() {
      return favorite;
   }

   public void setFavorite(boolean favorite) {
      this.favorite = favorite;
   }

}
