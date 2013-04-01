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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import net.ausgstecktis.ui.HeurigenApp;
import net.ausgstecktis.util.Log;
import net.ausgstecktis.util.UIUtils;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;

/**
 * ExportDatabaseFileTask.java
 * 
 * @author wexoo
 * @version Aug 27, 2011
 */
public class ExportDatabaseFileTask extends AsyncTask<String, Void, Boolean> {

   private static final String TAG = ExportDatabaseFileTask.class.getSimpleName();

   private static final String DB_PATH = Environment.getDataDirectory() + "/data/net.ausgstecktis/databases/";

   private final ProgressDialog dialog = new ProgressDialog(HeurigenApp.mainContext);

   /**
    * @see android.os.AsyncTask#onPreExecute()
    */
   @Override
   protected void onPreExecute() {
      dialog.setMessage("Exporting database...");
      dialog.show();
   }

   // automatically done on worker thread (separate from UI thread)
   /**
    * @see android.os.AsyncTask#doInBackground(Params[])
    */
   @Override
   protected Boolean doInBackground(final String... args) {

      final File dbFile = new File(DB_PATH + HeurigenApp.getConfig().databaseName());

      // path on sd by convention
      File exportDir =
            new File(Environment.getExternalStorageDirectory(), "/exported_db/");

      if (!exportDir.exists()) {
         boolean result = exportDir.mkdirs();
         Log.i(TAG, "create directory " + (result ? "succesful" : "failed"));
      }

      final File file = new File(exportDir, HeurigenApp.getConfig().databaseName());

      try {
         file.createNewFile();
         copyFile(dbFile, file);
         return true;
      } catch (final IOException e) {
         Log.e(ExportDatabaseFileTask.TAG, e.getMessage());
         return false;
      }
   }

   // can use UI thread here
   /**
    * {@inheritDoc}
    * 
    * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
    */
   @Override
   protected void onPostExecute(final Boolean success) {
      if (dialog.isShowing())
         dialog.dismiss();

      if (success)
         UIUtils.showShortToast("Export successful!");
      else
         UIUtils.showShortToast("Export failed");
   }

   /**
    * Copy file.
    * 
    * @throws IOException Signals that an I/O exception has occurred.
    */
   void copyFile(final File src, final File dst) throws IOException {
      final FileInputStream inStream = new FileInputStream(src);
      final FileChannel inChannel = inStream.getChannel();

      final FileOutputStream outStream = new FileOutputStream(dst);
      final FileChannel outChannel = outStream.getChannel();

      try {
         inChannel.transferTo(0, inChannel.size(), outChannel);
      } finally {
         if (inStream != null)
            inStream.close();
         if (outStream != null)
            outStream.close();
      }
   }
}
