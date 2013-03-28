/**
 * 
 */
package net.ausgstecktis.data.proxy;

import net.ausgstecktis.data.dataobject.Heuriger;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * @author siyb
 */
public class DatabaseProxyHeuriger extends DatabaseProxy<Heuriger> {

   @Override
   public ContentValues serialize() {

      return null;
   }

   @Override
   public Heuriger deserialize(Cursor c) {
      return null;
   }

   @Override
   public int insert(Context c) {
      return 0;
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
