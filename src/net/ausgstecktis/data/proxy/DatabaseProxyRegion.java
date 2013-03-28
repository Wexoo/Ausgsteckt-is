/**
 * 
 */
package net.ausgstecktis.data.proxy;

import net.ausgstecktis.data.dataobject.Region;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * @author siyb
 */
public class DatabaseProxyRegion extends DatabaseProxy<Region> {

   public static final String TABLE = "region";
   public static final String _ID = "_id";
   public static final String NAME = "name";

   @Override
   public ContentValues serialize() {
      if (dataObject == null)
         throw new DatabaseProxyException();
      ContentValues cv = new ContentValues();
      cv.put(NAME, dataObject.getName());
      return cv;
   }

   @Override
   public Region deserialize(Cursor c) {
      dataObject = new Region();
      dataObject.set_id(c.getInt(c.getColumnIndexOrThrow(_ID)));
      dataObject.setName(c.getString(c.getColumnIndexOrThrow(NAME)));
      return dataObject;
   }

   @Override
   public int insert(Context c) {
      String id = c.getContentResolver().insert(null, serialize()).getLastPathSegment();
      return Integer.parseInt(id);
   }

   @Override
   public int update(Context c) {
      return 0;
   }

   @Override
   public int delete(Context c) {
      return 0;
   }

}
