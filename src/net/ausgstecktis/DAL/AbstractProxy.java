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

package net.ausgstecktis.DAL;

import java.util.ArrayList;
import java.util.Date;

import net.ausgstecktis.entities.City;
import net.ausgstecktis.entities.District;
import net.ausgstecktis.entities.Heuriger;
import net.ausgstecktis.entities.OpenTime;
import net.ausgstecktis.entities.OpeningCalendar;
import android.database.Cursor;

/**
 * The Class AbstractProxy.
 * 
 * @author wexoo
 * @version Aug 26, 2011
 */
public abstract class AbstractProxy {

   /** Saved search result from the latest query */
   private ArrayList<Heuriger> heurigenList = new ArrayList<Heuriger>();

   /** The calendar list. */
   private ArrayList<OpeningCalendar> calendarList = new ArrayList<OpeningCalendar>();

   /** The open time. */
   private ArrayList<OpenTime> openTime = new ArrayList<OpenTime>();

   /** List for the cities to show in TodaySearchActivity */
   private ArrayList<City> cityList = new ArrayList<City>();

   /** Currently selected entity */
   private Heuriger selectedHeuriger;
   private City selectedCity;
   private District selectedDistrict;

   /**
    * Get Heurige which match the given string partly or exactly.
    * 
    * @param searchString the exact string or part of the searched data
    * @return the ArrayList with heurigen
    */
   public abstract ArrayList<Heuriger> getHeurigeByKeyword(String searchString);

   /**
    * Set the heurigen by keyword.
    * 
    * @param searchString the keyword for the search condition
    */
   public void setHeurigeByKeyword(final String searchString) {
      heurigenList = getHeurigeByKeyword(searchString);
   }

   public abstract Heuriger getHeurigenById(Integer id);

   public void setSelectedHeurigenById(Integer id) {
      selectedHeuriger = getHeurigenById(id);
   }

   /**
    * Get Heurige by district id for map view.
    * 
    * @param districtId the id of the district
    * @return the ArrayList with heurigen
    */
   public abstract ArrayList<Heuriger> getHeurigeByDistrict(Integer districtId);

   /**
    * Set the heuriger by id district.
    * 
    * @param districtId the id for the district
    */
   public void setHeurigeByDistrict(final Integer districtId) {
      heurigenList = getHeurigeByDistrict(districtId);
   }

   /**
    * Get heurigen which are open at given date.
    * 
    * @param date the date for the search condition format is "YYYY-MM-DD"
    * @return the ArrayList with heurigen
    */
   public abstract ArrayList<Heuriger> getHeurigeByDate(Date date);

   /**
    * Set the heurigen by date.
    * 
    * @param date the date for the search condition format is "YYYY-MM-DD"
    */
   public void setHeurigeByDate(final Date date) {
      heurigenList = getHeurigeByDate(date);
   }

   /**
    * Get Heurige with distance from current position.
    * 
    * @param latitude the gps coordinates for latitude
    * @param longitude the gps coordinates for longitude
    * @return the ArrayList with heurigen
    */
   public abstract ArrayList<Heuriger> getNearbyHeurige(double latitude, double longitude);

   /**
    * Set the heurigen by coordinates.
    * 
    * @param latitude the gps coordinates for latitude
    * @param longitude the gps coordinates for longitude
    */
   public void setNearbyHeurige(final double latitude, final double longitude) {
      heurigenList = getNearbyHeurige(latitude, longitude);
   }

   /**
    * Get favorite Heurige of user from local database.
    * 
    * @return the ArrayList with heurigen
    */
   public abstract ArrayList<Heuriger> getFavoriteHeurige();

   /**
    * Get Heurige for Location and Date.
    * 
    * @param date the date
    * @param searchString the search string
    * @return the ArrayList with heurigen
    */
   public abstract ArrayList<Heuriger> getHeurigeByLocation(Date date, String searchString);

   /**
    * Set the heurigen list by the location.
    * 
    * @param date the date for the search condition format is "YYYY-MM-DD"
    * @param searchString the location
    */
   public void setHeurigeByLocation(final Date date, final String searchString) {
      heurigenList = getHeurigeByLocation(date, searchString);
   }

   /**
    * Get opening dates for one Heuriger.
    * 
    * @param heurigenId the id for the heurigen
    * @return the ArrayList with open calendar ranges for the heurigen
    */
   public abstract ArrayList<OpeningCalendar> getOpenDatesById(Integer heurigenId);

   /**
    * Set the open calendar ranges for the heurigen.
    * 
    * @param heurigenId the id from the heuriger
    */
   public void setOpenDatesById(final Integer heurigenId) {
      calendarList = getOpenDatesById(heurigenId);
   }

   /**
    * Get City in which Heurigen are open at given date.
    * 
    * @param date the format is "YYYY-MM-DD"
    * @return the ArrayList with cities
    */
   public abstract ArrayList<City> getCityByDate(Date date);

   /**
    * Sets the city by date.
    * 
    * @param date the new city by date
    */
   public void setCityByDate(final Date date) {
      cityList = getCityByDate(date);
   }

   /**
    * Get Heurige from a given date and city.
    * 
    * @param date the date for the search condition format is "YYYY-MM-DD"
    * @param idCity the id for city
    * @return the ArrayList with heurigen
    */
   public abstract ArrayList<Heuriger> getHeurigeByDateCity(Date date, String idCity);

   /**
    * Sets the heurige by date city.
    * 
    * @param date the date
    * @param idCity the id city
    */
   public void setHeurigeByDateCity(final Date date, final String idCity) {
      heurigenList = getHeurigeByDateCity(date, idCity);
   }

   /**
    * Returns all district where at least on heuriger is located.
    * 
    * @return the ArrayList with districts
    */
   public abstract ArrayList<District> getDistrictsWhereHeurigeExist();

   /** return the words for the keywords autocompletetextview in the SearchActivity */
   public abstract Cursor getAutoCompleteKeywordsCursor(String constraint);

   /** return the words for the area autocompletetextview in the SearchActivity */
   public abstract Cursor getAutoCompleteAreasCursor(String constraint);

   /**
    * Return a List of all heurigen.
    * 
    * @return the ArrayList with heurigen
    */
   public ArrayList<Heuriger> getHeurigenList() {
      if (heurigenList == null)
         heurigenList = new ArrayList<Heuriger>();
      return heurigenList;
   }

   /**
    * Returns a list of calendar dates regarding opening times of a heuriger.
    * 
    * @return the ArrayList with open calendar ranges
    */
   public ArrayList<OpeningCalendar> getCalendarList() {
      if (calendarList == null)
         calendarList = new ArrayList<OpeningCalendar>();
      return calendarList;
   }

   /**
    * Return the opemTime List.
    * 
    * @return the ArrayList with open times
    */
   public ArrayList<OpenTime> getOpenTimeList() {
      if (openTime == null)
         openTime = new ArrayList<OpenTime>();
      return openTime;
   }

   /**
    * Return the City List.
    * 
    * @return the ArrayList with cities
    */
   public ArrayList<City> getCityList() {
      if (cityList == null)
         cityList = new ArrayList<City>();
      return cityList;
   }

   /**
    * Set the Heurigen List.
    * 
    * @param heurigenList the ArrayList with heurigen
    */
   public void setHeurigenList(final ArrayList<Heuriger> heurigenList) {
      this.heurigenList = heurigenList;
   }

   /**
    * Set the City List.
    * 
    * @param cityList the ArrayList with cities
    */
   public void setCityList(final ArrayList<City> cityList) {
      this.cityList = cityList;
   }

   /**
    * Add a new element to the Heurigen List.
    * 
    * @param element the heurigen object
    */
   public void addElementToHeurigenList(final Heuriger element) {
      if (heurigenList == null)
         heurigenList = new ArrayList<Heuriger>();
      heurigenList.add(element);
   }

   /**
    * Add a new element to the City List.
    * 
    * @param element the city object
    */
   public void addElementToCityList(final City element) {
      if (cityList == null)
         cityList = new ArrayList<City>();
      cityList.add(element);
   }

   /**
    * Remove the given heurigen from the heurigen List.
    * 
    * @param element the heurigen object
    */
   public void removeElementFromHeurigenList(final Heuriger element) {
      if (!heurigenList.isEmpty() && heurigenList.contains(element))
         heurigenList.remove(element);
   }

   /**
    * Remove the given City from the City List.
    * 
    * @param element the city object
    */
   public void removeElementFromCityList(final City element) {
      if (!cityList.isEmpty() && cityList.contains(element))
         cityList.remove(element);
   }

   /**
    * Get the selected heurigen.
    * 
    * @return the selected heurigen
    */
   public Heuriger getSelectedHeuriger() {
      return selectedHeuriger;
   }

   /**
    * Get the selected city.
    * 
    * @return the selected city
    */
   public City getSelectedCity() {
      return selectedCity;
   }

   /**
    * Set the selected herurigen.
    * 
    * @param selectedHeuriger the heuriger object
    */
   public void setSelectedHeuriger(final Heuriger selectedHeuriger) {
      this.selectedHeuriger = selectedHeuriger;
   }

   /**
    * Set the selected city.
    * 
    * @param selectedCity the city object
    */
   public void setSelectedCity(final City selectedCity) {
      this.selectedCity = selectedCity;
   }

   /**
    * Get the selected district.
    * 
    * @return the district object
    */
   public District getSelectedDistrict() {
      return selectedDistrict;
   }

   /**
    * Set the selected district.
    * 
    * @param selectedDistrict the district object
    */
   public void setSelectedDistrict(final District selectedDistrict) {
      this.selectedDistrict = selectedDistrict;
   }

   /**
    * Delete the whole heurigen List.
    */
   public void clearHeurigenList() {
      heurigenList.clear();
   }

   /**
    * Delete the whole city List.
    */
   public void clearCityList() {
      cityList.clear();
   }

   /**
    * Update the heuriger in the favorite list.
    * 
    * @param heuriger the heuriger to update
    * @return true, if successful
    */
   public abstract boolean updateFavorite(Heuriger heuriger);

   /**
    * Release resources.
    */
   public abstract void releaseResources();

   /**
    * Sets the favorite heurige.
    */
   public void setFavoriteHeurige() {
      heurigenList = getFavoriteHeurige();
   }
}