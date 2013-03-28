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
package net.ausgstecktis.ui.search;

import net.ausgstecktis.R;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * AutoCompleteCursorAdapter.java
 * 
 * @author wexoo
 * @since 1.0.2 22.09.2011
 */
public abstract class AutoCompleteCursorAdapter extends CursorAdapter {

   /**
    * @author wexoo
    * @since 1.0.2 22.09.2011
    */
   public AutoCompleteCursorAdapter(Context context, Cursor c) {
      super(context, c);
   }

   /**
    * @see android.widget.CursorAdapter#bindView(android.view.View, android.content.Context, android.database.Cursor)
    */
   
   public void bindView(View view, Context context, Cursor cursor) {
      String item = createDisplayValue(cursor);
      ((TextView) view).setText(item);
   }

   /**
    * @see android.widget.CursorAdapter#newView(android.content.Context, android.database.Cursor, android.view.ViewGroup)
    */
   
   public View newView(Context context, Cursor cursor, ViewGroup parent) {
      final LayoutInflater inflater = LayoutInflater.from(context);
      final TextView view = (TextView) inflater.inflate(R.layout.auto_complete_text_view, parent, false);

      String item = createDisplayValue(cursor);
      view.setText(item);
      return view;
   }

   /**
    * @see android.widget.CursorAdapter#runQueryOnBackgroundThread(java.lang.CharSequence)
    */
   
   public Cursor runQueryOnBackgroundThread(CharSequence constraint) {

      if (getFilterQueryProvider() != null) {
         return getFilterQueryProvider().runQuery(constraint);
      }

      return returnAutoCompleteCursor(constraint != null ? constraint.toString() : "");
   }

   
   public CharSequence convertToString(Cursor cursor) {
      return createDisplayValue(cursor);
   }

   /**
    * Method used in runQueryOnBackgroundThread to update auto complete data.
    * Return the cursor which queries the data for this adapter.
    * 
    * @author wexoo
    * @since 1.0.2 23.09.2011
    */
   protected abstract Cursor returnAutoCompleteCursor(String constraint);

   /**
    * Get the display value for the drop down and the text view.
    * Return values needed from the cursor.
    * 
    * @author wexoo
    * @since 1.0.2 22.09.2011
    */
   protected abstract String createDisplayValue(Cursor cursor);
}