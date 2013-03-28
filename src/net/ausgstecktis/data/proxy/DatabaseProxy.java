/**
 * 
 */
package net.ausgstecktis.data.proxy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * @author siyb
 */
public abstract class DatabaseProxy<T> {
   protected T dataObject;

   public abstract ContentValues serialize();

   public abstract T deserialize(Cursor c);

   public abstract int insert(Context c);

   public abstract int update(Context c);

   public abstract int delete(Context c);

   public void setDataObject(T dataObject) {
      this.dataObject = dataObject;
   }

}
