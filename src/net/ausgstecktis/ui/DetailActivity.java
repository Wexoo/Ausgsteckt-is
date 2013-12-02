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

package net.ausgstecktis.ui;

import net.ausgstecktis.R;
import net.ausgstecktis.DAL.ProxyFactory;
import net.ausgstecktis.entities.Heuriger;
import net.ausgstecktis.entities.OpeningCalendar;
import net.wexoo.organicdroid.concurrency.AbstractAsyncTask;
import net.wexoo.organicdroid.convert.DateAndTimeConverter;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * DetailActivity.java
 * 
 * @author naikon, wexoo
 * @version Aug 27, 2011
 */
public class DetailActivity extends SuperActivity {

   @SuppressWarnings("unused")
   private static final String TAG = DetailActivity.class.getSimpleName();
   private CompoundButton mStarred;
   private ProgressBar progressBar;
   private ImageView mapIcon;
   private ImageView mapSeparator;
   private TableRow mapNavigate;
   private TextView loading;
   private String openTimeText = "";
   private Heuriger selectedHeuriger;
   private GetOpenTimesAsyncTask openTimesAsyncTask;

   @Override
   public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.activity_detail);

      selectedHeuriger = ProxyFactory.getProxy().getSelectedHeuriger();

      if (!SuperActivity.checkIfMigrationOrCheckInProgress()) {
         mStarred = (CompoundButton) findViewById(R.id.cb_star_button);

         mStarred.setChecked(selectedHeuriger.getFavorite());
         mStarred.setFocusable(true);
         mStarred.setClickable(true);
      } else {
         mStarred = (CompoundButton) findViewById(R.id.cb_star_button);
         mStarred.setVisibility(View.GONE);
      }

      /** Description */
      if (!selectedHeuriger.getDescription().equals("")) {
         ((TextView) findViewById(R.id.tv_detail_description)).setText(selectedHeuriger.getDescription());
         findViewById(R.id.box_description).setVisibility(View.VISIBLE);
      } else
         findViewById(R.id.box_description).setVisibility(View.GONE);

      /** Name **/
      ((TextView) findViewById(R.id.tv_detail_name)).setText(selectedHeuriger.getName());

      /** Street **/
      if (!selectedHeuriger.getStreetNumber().toString().equals("")) {
         if (selectedHeuriger.getStreetNumber().toString().equals("0")) {
            if ((selectedHeuriger.getLatitude().toString().equals("0.0"))
                  || (selectedHeuriger.getLongitude().toString().equals("0.0")))
               ((TextView) findViewById(R.id.tv_detail_street)).setText(selectedHeuriger.getStreet().toString());
            else {
               final SpannableString underlineStreet = new SpannableString(selectedHeuriger.getStreet().toString());
               underlineStreet.setSpan(new UnderlineSpan(), 0, underlineStreet.length(), 0);
               ((TextView) findViewById(R.id.tv_detail_street)).setText(underlineStreet);
            }
         } else if ((selectedHeuriger.getLatitude().toString().equals("0.0"))
               || (selectedHeuriger.getLongitude().toString().equals("0.0")))
            ((TextView) findViewById(R.id.tv_detail_street)).setText(selectedHeuriger.getStreet().toString() + " "
                  + selectedHeuriger.getStreetNumber().toString());
         else {
            final SpannableString underlineStreetNumber =
                  new SpannableString(selectedHeuriger.getStreet().toString()
                        + " " + selectedHeuriger.getStreetNumber().toString());
            underlineStreetNumber.setSpan(new UnderlineSpan(), 0, underlineStreetNumber.length(), 0);
            ((TextView) findViewById(R.id.tv_detail_street)).setText(underlineStreetNumber);
         }
      } else {
         final TextView tvCityDetail = (TextView) findViewById(R.id.tv_detail_city);
         tvCityDetail.setVisibility(View.GONE);
         final ImageView iconCityDetail = (ImageView) findViewById(R.id.ic_city_detail);
         iconCityDetail.setVisibility(View.GONE);
      }

      /** City **/
      if (!selectedHeuriger.getStreetNumber().toString().equals("")) {
         final String cityText = selectedHeuriger.getCity().getZipCode().toString() + " "
               + selectedHeuriger.getCity().getName().toString();
         final SpannableString underlineCity = new SpannableString(cityText);
         underlineCity.setSpan(new UnderlineSpan(), 0, underlineCity.length(), 0);

         if ((selectedHeuriger.getLatitude().toString().equals("0.0"))
               || (selectedHeuriger.getLongitude().toString().equals("0.0")))
            ((TextView) findViewById(R.id.tv_detail_city)).setText(cityText);
         else
            ((TextView) findViewById(R.id.tv_detail_city)).setText(underlineCity);
      } else {
         final TextView tvCityDetail = (TextView) findViewById(R.id.tv_detail_city);
         tvCityDetail.setVisibility(View.GONE);
         final ImageView iconCityDetail = (ImageView) findViewById(R.id.ic_city_detail);
         iconCityDetail.setVisibility(View.GONE);
      }

      /** phone */
      if (selectedHeuriger.getPhone() != null && !selectedHeuriger.getPhone().equals(0L)) {
         final SpannableString underlinePhone = new SpannableString("+" + selectedHeuriger.getPhone().toString());
         underlinePhone.setSpan(new UnderlineSpan(), 0, underlinePhone.length(), 0);
         ((TextView) findViewById(R.id.tv_detail_phone)).setText(underlinePhone);
      } else {
         final TextView tvPhoneDetail = (TextView) findViewById(R.id.tv_detail_phone);
         tvPhoneDetail.setVisibility(View.GONE);
         final ImageView iconPhoneDetail = (ImageView) findViewById(R.id.ic_phone_detail);
         iconPhoneDetail.setVisibility(View.GONE);
      }

      /** website */
      if (!selectedHeuriger.getWebsite().equals("")) {
         final SpannableString underlineWebsite = new SpannableString(selectedHeuriger.getWebsite().toString());
         underlineWebsite.setSpan(new UnderlineSpan(), 0, underlineWebsite.length(), 0);
         ((TextView) findViewById(R.id.tv_detail_website)).setText(underlineWebsite);
      } else {
         final TextView tvWebDetail = (TextView) findViewById(R.id.tv_detail_website);
         tvWebDetail.setVisibility(View.GONE);
         final ImageView iconWebDetail = (ImageView) findViewById(R.id.ic_web_detail);
         iconWebDetail.setVisibility(View.GONE);
      }

      /** email */
      if (!selectedHeuriger.getMail().equals("")) {
         final SpannableString underlineMail = new SpannableString(selectedHeuriger.getMail().toString());
         underlineMail.setSpan(new UnderlineSpan(), 0, underlineMail.length(), 0);
         ((TextView) findViewById(R.id.tv_detail_mail)).setText(underlineMail);
      } else {
         final TextView tvMailDetail = (TextView) findViewById(R.id.tv_detail_mail);
         tvMailDetail.setVisibility(View.GONE);
         final ImageView iconMailDetail = (ImageView) findViewById(R.id.ic_mail_detail);
         iconMailDetail.setVisibility(View.GONE);
      }

      /** opening */
      if (!selectedHeuriger.getOpening().equals(""))
         ((TextView) findViewById(R.id.tv_detail_opening)).setText(selectedHeuriger.getOpening());
      else {
         findViewById(R.id.box_detail_opening).setVisibility(View.GONE);
         final TextView tvOpeningDetail = (TextView) findViewById(R.id.tv_detail_opening);
         tvOpeningDetail.setVisibility(View.GONE);
      }

      /**
       * GPS data
       * if no GPS data available don't show map and navigate features
       */
      if ((selectedHeuriger.getLatitude().toString().equals("0.0"))
            || (selectedHeuriger.getLongitude().toString().equals("0.0"))) {

         mapNavigate = (TableRow) findViewById(R.id.tr_navigation_row);
         mapNavigate.setVisibility(View.INVISIBLE);
      }

      openTimesAsyncTask = new GetOpenTimesAsyncTask();
      openTimesAsyncTask.execute();

      progressBar = (ProgressBar) findViewById(R.id.title_refresh_progress);
      progressBar.setVisibility(View.VISIBLE);

      loading = (TextView) findViewById(R.id.tv_detail_opening_time_loading);
      loading.setVisibility(View.VISIBLE);
   }

   @Override
   protected void cancelAllAsyncTasksOfActivity() {
      cancelAllAsynTasks(openTimesAsyncTask);
   }

   public class GetOpenTimesAsyncTask extends AbstractAsyncTask {

      @Override
      protected void onPostExecute(final Void result) {
         progressBar.setVisibility(View.GONE);
         loading.setVisibility(View.GONE);

         /** open times */
         if (!ProxyFactory.getProxy().getCalendarList().isEmpty()) {
            for (final OpeningCalendar openCalendar : ProxyFactory.getProxy().getCalendarList())
               openTimeText = openTimeText + DateAndTimeConverter.getGermanDateAsString(openCalendar.getStart())
                     + " - " + DateAndTimeConverter.getGermanDateAsString(openCalendar.getEnd()) + "\n";
            ((TextView) findViewById(R.id.tv_detail_opening_time)).setText(openTimeText);
         } else {
            final TextView tvOpeningTime = (TextView) findViewById(R.id.tv_detail_opening_time);
            tvOpeningTime.setText(R.string.detail_no_calendar);
         }
      }

      @Override
      protected Void doInBackground(final Void... params) {
         ProxyFactory.getProxy().setOpenDatesById(selectedHeuriger.getId());
         return null;
      }
   }
}
