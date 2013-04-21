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

package net.ausgstecktis.adapter;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import net.ausgstecktis.R;
import net.ausgstecktis.util.Log;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.SectionIndexer;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Displays a list view in alphabetized order
 * Copied from https://eshyu.wordpress.com/2010/08/15/cursoradapter-with-alphabet-indexed-section-headers/
 * SearchResultAlphabetizedAdapter.java
 * 
 * @author naikon, wexoo
 */
public class SearchResultAlphabetizedAdapter extends SimpleCursorAdapter implements SectionIndexer {

   private static final int TYPE_HEADER = 1;
   private static final int TYPE_NORMAL = 0;
   private static final int TYPE_COUNT = 2;
   private static final String TAG = "AlphabetizedAdapter";
   private final AlphabetIndexer indexer;
   private final int[] usedSectionNumbers;
   private final Map<Integer, Integer> sectionToOffset;
   private final Map<Integer, Integer> sectionToPosition;
   private final Context context;

   public SearchResultAlphabetizedAdapter(final Context context, final int layout, final Cursor c,
         final String[] from, final int[] to) {
      super(context, layout, c, from, to);
      this.context = context;
      indexer = new AlphabetIndexer(c, c.getColumnIndexOrThrow(SearchResultDbHelper.SORT_NAME_COLUMN),
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
      sectionToPosition = new TreeMap<Integer, Integer>(); //use a TreeMap because we are going to iterate over its keys in sorted order
      sectionToOffset = new HashMap<Integer, Integer>();

      final int count = super.getCount();
      Log.i(SearchResultAlphabetizedAdapter.TAG, "---->" + count);
      int i;

      //temporarily have a map alphabet section to first index it appears
      //(this map is going to be doing something else later)
      for (i = count - 1; i >= 0; i--)
         sectionToPosition.put(indexer.getSectionForPosition(i), i);

      i = 0;
      usedSectionNumbers = new int[sectionToPosition.keySet().size()];

      //note that for each section that appears before a position, we must offset our
      //indices by 1, to make room for an alphabetical header in our list
      for (final Integer section : sectionToPosition.keySet()) {
         sectionToOffset.put(section, i);
         usedSectionNumbers[i] = section;
         i++;
      }

      //use offset to map the alphabet sections to their actual indices in the list
      for (final Integer section : sectionToPosition.keySet())
         sectionToPosition.put(section, sectionToPosition.get(section) + sectionToOffset.get(section));
   }

   @Override
   public int getCount() {
      if (super.getCount() != 0)
         //sometimes your data set gets invalidated. In this case getCount()
         //should return 0 and not our adjusted count for the headers.
         //The only way to know if data is invalidated is to check if
         //super.getCount() is 0.
         return super.getCount() + usedSectionNumbers.length;

      return 0;
   }

   @Override
   public Object getItem(final int position) {
      if (getItemViewType(position) == SearchResultAlphabetizedAdapter.TYPE_NORMAL)
         //if the list item is not a header, then we fetch the data set item with the same position
         //off-setted by the number of headers that appear before the item in the list
         return super.getItem(position - sectionToOffset.get(getSectionForPosition(position)) - 1);

      return null;
   }

   @Override
   public int getPositionForSection(final int section) {
      if (!sectionToOffset.containsKey(section)) {
         //This is only the case when the FastScroller is scrolling,
         //and so this section doesn't appear in our data set. The implementation
         //of Fastscroller requires that missing sections have the same index as the
         //beginning of the next non-missing section (or the end of the the list if
         //if the rest of the sections are missing).
         //So, in pictorial example, the sections D and E would appear at position 9
         //and G to Z appear in position 11.
         int i = 0;
         final int maxLength = usedSectionNumbers.length;

         //linear scan over the sections (constant number of these) that appear in the
         //data set to find the first used section that is greater than the given section, so in the
         //example D and E correspond to F
         while (i < maxLength && section > usedSectionNumbers[i])
            i++;
         if (i == maxLength)
            return getCount(); //the given section is past all our data

         return indexer.getPositionForSection(usedSectionNumbers[i]) + sectionToOffset.get(usedSectionNumbers[i]);
      }

      return indexer.getPositionForSection(section) + sectionToOffset.get(section);
   }

   @Override
   public int getSectionForPosition(final int position) {
      int i = 0;
      final int maxLength = usedSectionNumbers.length;

      //linear scan over the used alphabetical sections' positions
      //to find where the given section fits in
      while (i < maxLength && position >= sectionToPosition.get(usedSectionNumbers[i]))
         i++;
      return usedSectionNumbers[i - 1];
   }

   @Override
   public Object[] getSections() {
      return indexer.getSections();
   }

   //nothing much to this: headers have positions that the sectionIndexer manages.

   @Override
   public int getItemViewType(final int position) {
      if (position == getPositionForSection(getSectionForPosition(position)))
         return SearchResultAlphabetizedAdapter.TYPE_HEADER;
      return SearchResultAlphabetizedAdapter.TYPE_NORMAL;
   }

   @Override
   public int getViewTypeCount() {
      return SearchResultAlphabetizedAdapter.TYPE_COUNT;
   }

   //return the header view, if it's in a section header position

   @Override
   public View getView(final int position, View convertView, final ViewGroup parent) {
      Log.i(SearchResultAlphabetizedAdapter.TAG, "--->position: " + position);
      final int type = getItemViewType(position);
      if (type == SearchResultAlphabetizedAdapter.TYPE_HEADER) {
         if (convertView == null) {
            final LayoutInflater inlater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inlater.inflate(R.layout.alphabetized_header, parent, false);
         }
         ((TextView) convertView.findViewById(R.id.header))
               .setText((String) getSections()[getSectionForPosition(position)]);
         return convertView;
      }
      return super.getView(position - sectionToOffset.get(getSectionForPosition(position)) - 1, convertView, parent);
   }

   //these two methods just disable the headers

   @Override
   public boolean areAllItemsEnabled() {
      return false;
   }

   @Override
   public boolean isEnabled(final int position) {
      if (getItemViewType(position) == SearchResultAlphabetizedAdapter.TYPE_HEADER)
         return false;
      return true;
   }

   /**
    * Gets the real position.
    * 
    * @param position the position
    * @return the real position
    */
   public int getRealPosition(final int position) {
      return position - sectionToOffset.get(getSectionForPosition(position)) - 1;
   }

}
