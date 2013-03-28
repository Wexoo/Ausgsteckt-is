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

package net.ausgstecktis.util;

import net.ausgstecktis.R;
import net.ausgstecktis.ui.HeurigenApp;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;

/**
 * AbstractAlertDialogBuilder.java
 * 
 * @author wexoo
 * @version Aug 27, 2011
 */
public abstract class AbstractAlertDialogBuilder extends Builder {

   public AbstractAlertDialogBuilder(final Integer messageKey) {
      this(HeurigenApp.mainContext, messageKey);
   }

   public AbstractAlertDialogBuilder(final String message) {
      this(HeurigenApp.mainContext, message);
   }

   public AbstractAlertDialogBuilder(final Context context, final String message) {
      this(context, message, R.string.alert_yes, null, R.string.alert_no);
   }

   public AbstractAlertDialogBuilder(final Context context, final Integer messageKey) {
      this(context, context.getString(messageKey), R.string.alert_yes, null, R.string.alert_no);
   }

   public AbstractAlertDialogBuilder(final Context context, final String message, final Integer posButtonKey,
         final Integer neuButtonKey, final Integer negButtonKey) {
      super(context);

      this.setMessage(message);
      setCancelable(false);
      this.setPositiveButton(context.getString(posButtonKey),
            new DialogInterface.OnClickListener() {

               public void onClick(final DialogInterface dialog, final int id) {
                  positiveButtonAction(context);
               }
            });

      if (neuButtonKey != null)
         this.setNeutralButton(context.getString(posButtonKey),
               new DialogInterface.OnClickListener() {

                  public void onClick(final DialogInterface dialog, final int id) {
                     neutralButtonAction(context);
                  }
               });
      this.setNegativeButton(context.getString(negButtonKey),
            new DialogInterface.OnClickListener() {

               public void onClick(final DialogInterface dialog, final int id) {
                  negativeButtonAction(context);
                  dialog.cancel();
               }
            });
   }

   /**
    * Show alert dialog.
    */
   public AlertDialog showAlertDialog() {
      final AlertDialog alert = create();

      alert.show();

      return alert;
   }

   /**
    * Positive button action.
    * 
    * @param context the context
    */
   protected abstract void positiveButtonAction(Context context);

   /**
    * Neutral button action.
    * 
    * @param context the context
    */
   protected void neutralButtonAction(final Context context) {
   }

   /**
    * Negative button action.
    * 
    * @param context the context
    */
   protected abstract void negativeButtonAction(Context context);
}