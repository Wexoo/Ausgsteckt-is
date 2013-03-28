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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import net.ausgstecktis.DAL.util.DALUtils;
import net.ausgstecktis.entities.City;
import net.ausgstecktis.entities.District;
import net.ausgstecktis.entities.Heuriger;
import net.ausgstecktis.entities.OpenDay;
import net.ausgstecktis.entities.OpenTime;
import net.ausgstecktis.entities.OpeningCalendar;
import net.ausgstecktis.ui.HeurigenApp;
import net.ausgstecktis.util.Log;
import net.ausgstecktis.util.LogicalOperator;
import net.ausgstecktis.util.ParenthesisBehaviour;
import net.ausgstecktis.util.SQLiteCondition;
import android.database.Cursor;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;

/**
 * OfflineProxy.java
 * 
 * @author wexoo
 * @version Aug 27, 2011
 */
public class OfflineProxy extends AbstractProxy {

   /** General SQLite database information. */
   public static final int DATABASE_VERSION = 1;

   private static final String TAG = OfflineProxy.class.getSimpleName();
   private static OrmLiteSqliteOpenHelper helper;
   private OnlineProxy onlineProxy;

   /**
    * Instantiates a new offline proxy.
    */
   public OfflineProxy() {
      helper = OpenHelperManager.getHelper(HeurigenApp.mainContext);
      Log.d(TAG, "Helper fetched using HeurigenApp.mainContext!");

      getHeurigeByKeyword("blablablublu");
   }

   @Override
   public ArrayList<Heuriger> getHeurigeByKeyword(final String searchString) {

      try {
         final Dao<Heuriger, Integer> heurigerDao = getHeurigerDao();

         final Where<Heuriger, Integer> where = heurigerDao
               .queryBuilder()
               .orderBy(Heuriger.SORT_NAME_COLUMN, true)
               .where()
               .like(Heuriger.NAME_COLUMN, "%" + searchString + "%")
               .or().like(Heuriger.EMAIL_COLUMN, "%" + searchString + "%")
               .or().like(Heuriger.WEBSITE_COLUMN, "%" + searchString + "%")
               .or().like(Heuriger.STREET_COLUMN, "%" + searchString + "%")
               .or().like(Heuriger.DESCRIPTION_COLUMN, "%" + searchString + "%")
               .or().like(Heuriger.OPENING_COLUMN, "%" + searchString + "%");

         return getHeurige(where);
      } catch (final SQLException e) {
         Log.e(OfflineProxy.TAG, e.getMessage());
         return null;
      } finally {
         releaseResources();
      }
   }

   @Override
   public Heuriger getHeurigenById(Integer id) {
      try {
         final Dao<Heuriger, Integer> heurigerDao = getHeurigerDao();

         return heurigerDao.queryForId(id);
      } catch (final SQLException e) {
         Log.e(OfflineProxy.TAG, e.getMessage());
         return null;
      } finally {
         releaseResources();
      }
   }

   @Override
   public ArrayList<Heuriger> getHeurigeByDate(final Date date) {
      return this.getHeurigeByDateAndLocation(date, "", Heuriger.class, "distinct h.id", null);
   }

   @Override
   public ArrayList<Heuriger> getNearbyHeurige(final double latitude, final double longitude) {
      /**
       * not available in offline mode, use online instead
       */
      return getOnlineProxy().getNearbyHeurige(latitude, longitude);
   }

   @Override
   public ArrayList<Heuriger> getHeurigeByLocation(final Date date,
         final String searchString) {
      return this.getHeurigeByDateAndLocation(date, searchString, Heuriger.class,
            "distinct h.id", null);
   }

   /**
    * {@inheritDoc}
    * 
    * @see net.ausgstecktis.DAL.AbstractProxy#getCityByDate(java.util.Date)
    */
   @Override
   public ArrayList<City> getCityByDate(final Date date) {
      return this.getHeurigeByDateAndLocation(date, "", City.class,
            "c.id, count(h.id)", "c.id");
   }

   /**
    * {@inheritDoc}
    * 
    * @see net.ausgstecktis.DAL.AbstractProxy#getHeurigeByDateCity(java.util.Date, java.lang.String)
    */
   @Override
   public ArrayList<Heuriger> getHeurigeByDateCity(final Date date, final String idCity) {
      return this.getHeurigeByDateAndLocation(date, "", Heuriger.class,
            "distinct h.id", null, new SQLiteCondition(LogicalOperator.AND,
                  "c.id = " + idCity));
   }

   /**
    * {@inheritDoc}
    * 
    * @see net.ausgstecktis.DAL.AbstractProxy#getOpenDatesById(java.lang.Integer)
    */
   @Override
   public ArrayList<OpeningCalendar> getOpenDatesById(final Integer heurigenId) {
      final ArrayList<OpeningCalendar> resultList = new ArrayList<OpeningCalendar>();

      try {
         final Dao<Heuriger, Integer> objectDao = getHeurigerDao();

         final Iterator<String[]> ids = objectDao.queryRaw(
               "select DISTINCT ca.id, ca.start, ca.end from "
                     + Heuriger.TABLE_NAME + " h, "
                     + OpeningCalendar.TABLE_NAME + " ca, "
                     + OpenDay.TABLE_NAME + " od, "
                     + OpenTime.TABLE_NAME + " ot " + " where ca."
                     + OpeningCalendar.HEURIGER_COLUMN + " = h.id "
                     + " and ca.id = od." + OpenDay.CALENDAR_COLUMN
                     + " and od." + OpenDay.OPEN_TIME_COLUMN
                     + " = ot.id " + " and h.id = " + heurigenId
                     + " and strftime('%s', ca.end, 'localtime')*1000 > " + new Date().getTime()
                     + " order by ca.start asc").iterator();

         while (ids.hasNext()) {
            final String[] nextValue = ids.next();

            resultList.add(new OpeningCalendar(Integer.valueOf(nextValue[0]),
                  null, DALUtils.getDefaultDateValueOfString(nextValue[1]),
                  DALUtils.getDefaultDateValueOfString(nextValue[2])));
         }
         return resultList;

      } catch (final SQLException e) {
         Log.e(OfflineProxy.TAG, e.getMessage());
         return null;
      } finally {
         releaseResources();
      }
   }

   private <T> ArrayList<T> getHeurigeByDateAndLocation(final Date date,
         final String location, final Class<T> resultClass, final String selects,
         final String groupBy, final SQLiteCondition... conditions) {
      String conditionString;

      try {
         final ArrayList<T> resultList = new ArrayList<T>();

         final Dao<T, Integer> objectDao = getGenericDao(resultClass);

         conditionString = buildConditionString(conditions);

         StringBuilder rawQueryBuilder = new StringBuilder();
         rawQueryBuilder.append("select ");
         rawQueryBuilder.append(selects);
         // used tables
         rawQueryBuilder.append(" from ");
         rawQueryBuilder.append(Heuriger.TABLE_NAME);
         rawQueryBuilder.append(" h, ");
         rawQueryBuilder.append(City.TABLE_NAME);
         rawQueryBuilder.append(" c, ");
         rawQueryBuilder.append(District.TABLE_NAME);
         rawQueryBuilder.append(" d, ");
         rawQueryBuilder.append(OpeningCalendar.TABLE_NAME);
         rawQueryBuilder.append(" ca, ");
         rawQueryBuilder.append(OpenDay.TABLE_NAME);
         rawQueryBuilder.append(" od, ");
         rawQueryBuilder.append(OpenTime.TABLE_NAME);
         rawQueryBuilder.append(" ot ");
         //joins
         rawQueryBuilder.append("where h.");
         rawQueryBuilder.append(Heuriger.CITY_COLUMN);
         rawQueryBuilder.append(" = c.id and c.");
         rawQueryBuilder.append(City.DISTRICT_COLUMN);
         rawQueryBuilder.append(" = d.id and ca.");
         rawQueryBuilder.append(OpeningCalendar.HEURIGER_COLUMN);
         rawQueryBuilder.append(" = h.id and ca.id = od.");
         rawQueryBuilder.append(OpenDay.CALENDAR_COLUMN);
         rawQueryBuilder.append(" and od.");
         rawQueryBuilder.append(OpenDay.OPEN_TIME_COLUMN);
         rawQueryBuilder.append(" = ot.id ");
         //conditions
         if (date != null) {
            rawQueryBuilder.append("and od.");
            rawQueryBuilder.append(OpenDay.DAY_COLUMN);
            rawQueryBuilder.append(" = strftime('%w', 'now', 'localtime') + 1");
            rawQueryBuilder.append(" and ");
            rawQueryBuilder.append(date.getTime());
            rawQueryBuilder.append(" >= strftime('%s', ca.start, 'localtime')*1000");
            rawQueryBuilder.append(" and ");
            rawQueryBuilder.append(date.getTime());
            rawQueryBuilder.append(" <= strftime('%s', ca.end)*1000 ");
         }
         rawQueryBuilder.append("and c.");
         rawQueryBuilder.append(City.NAME_COLUMN);
         rawQueryBuilder.append(" like '%");
         rawQueryBuilder.append(location);
         rawQueryBuilder.append("%'");
         rawQueryBuilder.append(conditionString);
         //group by
         rawQueryBuilder.append(groupBy != null ? " group by " + groupBy : "");
         //order by
         rawQueryBuilder.append(" order by ");
         if (resultClass.equals(City.class)) {
            rawQueryBuilder.append("c.");
            rawQueryBuilder.append(City.NAME_COLUMN);
         } else {
            rawQueryBuilder.append("h.");
            rawQueryBuilder.append(Heuriger.NAME_COLUMN);
         }

         //         Log.d(TAG, rawQueryBuilder.toString());

         final Iterator<String[]> ids = objectDao.queryRaw(
               rawQueryBuilder.toString())
               .iterator();

         while (ids.hasNext()) {
            final String[] nextRow = ids.next();
            final T object = objectDao.queryForId(Integer.valueOf(nextRow[0]));
            resultList.add(object);
            if (resultClass.equals(City.class) && nextRow.length > 1)
               ((City) object).setAmount(Integer.valueOf(nextRow[1]));
         }
         return resultList;
      } catch (final SQLException e) {
         Log.e(OfflineProxy.TAG, e.getMessage());
         return null;
      } finally {
         releaseResources();
      }
   }

   /**
    * {@inheritDoc}
    * 
    * @see net.ausgstecktis.DAL.AbstractProxy#getFavoriteHeurige()
    */
   @Override
   public ArrayList<Heuriger> getFavoriteHeurige() {
      try {
         final Dao<Heuriger, Integer> objectDao = getHeurigerDao();

         final Where<Heuriger, Integer> where = objectDao.queryBuilder().orderBy(Heuriger.NAME_COLUMN, true).where()
               .eq(Heuriger.FAVORITE_COLUMN, true);

         return getHeurige(where);
      } catch (final SQLException e) {
         Log.e(TAG, e.getMessage());
         return null;
      } finally {
         releaseResources();
      }
   }

   /**
    * Returns the heurigen list.
    * 
    * @param where the where
    * @return Arraylist heurige
    */
   public ArrayList<Heuriger> getHeurige(final Where<Heuriger, Integer> where) {
      final ArrayList<Heuriger> resultList = new ArrayList<Heuriger>();

      try {
         final Dao<Heuriger, Integer> heurigerDao = getHeurigerDao();

         resultList.addAll(heurigerDao.query(where.prepare()));

      } catch (SQLException e) {
         Log.e(OfflineProxy.TAG, e.getMessage());
      } finally {
         releaseResources();
      }
      return resultList;
   }

   /**
    * {@inheritDoc}
    * 
    * @see net.ausgstecktis.DAL.AbstractProxy#getHeurigeByDistrict(java.lang.Integer)
    */
   @Override
   public ArrayList<Heuriger> getHeurigeByDistrict(final Integer districtId) {
      try {
         ArrayList<Heuriger> heurigenList = new ArrayList<Heuriger>();

         Dao<Heuriger, Integer> heurigerDao = getHeurigerDao();

         StringBuilder rawQueryBuilder = new StringBuilder();
         rawQueryBuilder.append("select h.");
         rawQueryBuilder.append(Heuriger.ID_COLUMN);
         rawQueryBuilder.append(" from ");
         rawQueryBuilder.append(Heuriger.TABLE_NAME);
         rawQueryBuilder.append(" h, ");
         rawQueryBuilder.append(City.TABLE_NAME);
         rawQueryBuilder.append(" c where h.");
         rawQueryBuilder.append(Heuriger.CITY_COLUMN);
         rawQueryBuilder.append(" = c.");
         rawQueryBuilder.append(City.ID_COLUMN);
         rawQueryBuilder.append(" and c.");
         rawQueryBuilder.append(City.DISTRICT_COLUMN);
         rawQueryBuilder.append(" = ");
         rawQueryBuilder.append(districtId);

         Iterator<String[]> ids = heurigerDao.queryRaw(
               rawQueryBuilder.toString())
               .iterator();

         while (ids.hasNext())
            heurigenList.add(heurigerDao.queryForId(Integer.valueOf(ids.next()[0])));
         return heurigenList;
      } catch (SQLException e) {
         Log.e(TAG, e.getMessage());
         return null;
      } finally {
         releaseResources();
      }
   }

   /**
    * Gets the districts where heurige exist.
    * 
    * @return the districts where heurige exist
    * @see net.ausgstecktis.DAL.AbstractProxy#getDistrictsWhereHeurigeExist()
    */
   @Override
   public ArrayList<District> getDistrictsWhereHeurigeExist() {

      try {
         ArrayList<District> districts = new ArrayList<District>();

         Dao<District, Integer> districtDao = getGenericDao(District.class);

         StringBuilder rawQueryBuilder = new StringBuilder();
         rawQueryBuilder.append("select distinct d.");
         rawQueryBuilder.append(District.ID_COLUMN);
         rawQueryBuilder.append(" from ");
         rawQueryBuilder.append(District.TABLE_NAME);
         rawQueryBuilder.append(" d, ");
         rawQueryBuilder.append(City.TABLE_NAME);
         rawQueryBuilder.append(" c");
         rawQueryBuilder.append(" where d.");
         rawQueryBuilder.append(District.ID_COLUMN);
         rawQueryBuilder.append(" = c.");
         rawQueryBuilder.append(City.DISTRICT_COLUMN);
         rawQueryBuilder.append(" and c.");
         rawQueryBuilder.append(City.ID_COLUMN);
         rawQueryBuilder.append(" in (select distinct h.");
         rawQueryBuilder.append(Heuriger.CITY_COLUMN);
         rawQueryBuilder.append(" from ");
         rawQueryBuilder.append(Heuriger.TABLE_NAME);
         rawQueryBuilder.append(" h)");
         rawQueryBuilder.append(" order by d.");
         rawQueryBuilder.append(District.NAME_COLUMN);

         Iterator<String[]> ids = districtDao.queryRaw(rawQueryBuilder.toString())
               .iterator();

         while (ids.hasNext())
            districts.add(districtDao.queryForId(Integer.valueOf(ids.next()[0])));

         return districts;
      } catch (SQLException e) {
         Log.e(TAG, e.getMessage());
         return null;
      } finally {
         releaseResources();
      }
   }

   /**
    * @see net.ausgstecktis.DAL.AbstractProxy#getAutoCompleteKeywordsCursor(java.lang.String)
    */
   @Override
   public Cursor getAutoCompleteKeywordsCursor(String constraint) {

      String[] selectedColumns = {Heuriger.ID_COLUMN + " as _id", Heuriger.NAME_COLUMN};

      String whereString = null;
      if (constraint != null && !constraint.equals("")) {
         StringBuilder whereBuilder = new StringBuilder(Heuriger.NAME_COLUMN);
         whereBuilder.append(" like '%");
         whereBuilder.append(constraint);
         whereBuilder.append("%'");
         whereString = whereBuilder.toString();
      }

      Cursor keywordsCursor =
            getHelper().getReadableDatabase()
                  .query(Heuriger.TABLE_NAME, selectedColumns, whereString, null, null, null, Heuriger.NAME_COLUMN);

      releaseResources();

      return keywordsCursor;
   }

   /**
    * @see net.ausgstecktis.DAL.AbstractProxy#getAutoCompleteAreasCursor(java.lang.String)
    */
   @Override
   public Cursor getAutoCompleteAreasCursor(String constraint) {

      StringBuilder queryBuilder = new StringBuilder();
      queryBuilder.append("select min(_id) as _id, name from ");
      queryBuilder.append("(select distinct c.id as _id, c.name  as name ");
      queryBuilder.append("from heuriger ");
      queryBuilder.append("join city c on heuriger.id_city = c.id ");
      if (constraint != null)
         queryBuilder.append("where c.name like ? ");
      queryBuilder.append("union ");
      queryBuilder.append("select distinct d.id + 10000 as _id, d.name  as name ");
      queryBuilder.append("from heuriger ");
      queryBuilder.append("join city on heuriger.id_city = city.id ");
      queryBuilder.append("join district d on  city.id_district = d.id ");
      if (constraint != null)
         queryBuilder.append("where d.name like ? ");
      queryBuilder.append("union ");
      queryBuilder.append("select distinct r.id + 20000 as _id, r.name as name ");
      queryBuilder.append("from heuriger ");
      queryBuilder.append("join city on heuriger.id_city = city.id ");
      queryBuilder.append("join region r on r.id = city.id_region ");
      if (constraint != null)
         queryBuilder.append("where r.name like ? ");
      queryBuilder.append(" order by name)");
      queryBuilder.append(" group by name");

      if (constraint != null)
         constraint = "%" + constraint + "%";

      Cursor areasCursor =
            getHelper().getReadableDatabase()
                  .rawQuery(queryBuilder.toString(),
                        constraint != null ? new String[] {constraint, constraint, constraint} : null);

      //      areasCursor.moveToFirst();
      //      while (!areasCursor.isAfterLast()) {
      //         Log.d(TAG, "id_" + areasCursor.getString(areasCursor.getColumnIndex("_id")) +
      //               " name: " + areasCursor.getString(areasCursor.getColumnIndex("name")));
      //         areasCursor.moveToNext();
      //      }

      //      releaseResources();

      return areasCursor;
   }

   /**
    * {@inheritDoc}
    * 
    * @see net.ausgstecktis.DAL.AbstractProxy#updateFavorite(net.ausgstecktis.entities.Heuriger)
    */
   @Override
   public boolean updateFavorite(final Heuriger heuriger) {

      try {
         final Dao<Heuriger, Integer> objectDao = getHeurigerDao();

         if (objectDao
               .queryRaw(
                     "select count(*) from "
                           + Heuriger.TABLE_NAME + " WHERE "
                           + Heuriger.ID_COLUMN + " = ?",
                     heuriger.getId().toString()).getResults().get(0)[0]
               .equals("0"))
            objectDao.updateRaw("INSERT INTO "
                  + Heuriger.TABLE_NAME + " ("
                  + Heuriger.ID_COLUMN + ", " + Heuriger.FAVORITE_COLUMN
                  + ") VALUES (?, ?)", heuriger.getId().toString(),
                  heuriger.getFavorite() ? "1" : "0");
         else
            objectDao.updateRaw("UPDATE " + Heuriger.TABLE_NAME
                  + " SET " + Heuriger.FAVORITE_COLUMN + " = ? WHERE "
                  + Heuriger.ID_COLUMN + " = ? ",
                  heuriger.getFavorite() ? "1" : "0", heuriger.getId()
                        .toString());
      } catch (final SQLException e) {
         Log.e(TAG, e.getMessage());
         return false;
      } finally {
         releaseResources();
      }
      return true;
   }

   /**
    * Checks if the given heurigen with the id is favorite.
    * 
    * @param id the id
    * @return true or false
    */
   public Boolean isHeurigerFavorite(final Integer id) {
      Heuriger heuriger;
      try {
         final Dao<Heuriger, Integer> objectDao = getHeurigerDao();

         heuriger = objectDao.queryForId(id);

      } catch (final SQLException e) {
         Log.e(TAG, e.getMessage());
         return false;
      } finally {
         releaseResources();
      }
      if (heuriger != null)
         return heuriger.getFavorite();
      return false;
   }

   /**
    * Builds the condition string.
    * 
    * @param conditions the conditions
    * @return the string
    */
   private String buildConditionString(final SQLiteCondition... conditions) {
      String conditionString = "";

      for (final SQLiteCondition cond : conditions) {
         conditionString += " " + cond.getOperator().name() + " ";
         if (cond.getParenthesis().equals(ParenthesisBehaviour.OPEN))
            conditionString += cond.getParenthesis().toString();
         conditionString += cond.getCondition();
         if (cond.getParenthesis().equals(ParenthesisBehaviour.CLOSE))
            conditionString += cond.getParenthesis().toString();
      }
      return conditionString;
   }

   /**
    * {@inheritDoc}
    * 
    * @see net.ausgstecktis.DAL.AbstractProxy#releaseResources()
    */
   @Override
   public void releaseResources() {
   }

   /**
    * Gets the online proxy.
    * 
    * @return the online proxy
    */
   public OnlineProxy getOnlineProxy() {
      if (onlineProxy == null)
         onlineProxy = new OnlineProxy();
      return onlineProxy;
   }

   private Dao<Heuriger, Integer> getHeurigerDao() {
      return getGenericDao(Heuriger.class);
   }

   private <Entitiy> Dao<Entitiy, Integer> getGenericDao(Class<Entitiy> entity) {
      //      getHelper();
      try {
         return helper.getDao(entity);
      } catch (SQLException e) {
         e.printStackTrace();
         return null;
      }
   }

   private OrmLiteSqliteOpenHelper getHelper() {
      //      if (helper == null || !helper.isOpen()) {
      //         helper = OpenHelperManager.getHelper(HeurigenApp.mainContext);
      //         Log.d(OfflineProxy.TAG, "Helper fetched by using HeurigenApp.mainContext");
      //      }
      return helper;
   }
}