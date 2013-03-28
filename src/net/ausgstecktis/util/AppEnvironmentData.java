package net.ausgstecktis.util;

import java.util.EnumMap;
import java.util.Properties;

/**
 * Stores a environment data with {@link AppEnvironmentField} enum values as keys.
 * This is basically the source of {@link Properties} adapted to extend an
 * EnumMap instead of Hashtable and with a few tweaks to avoid losing crazy
 * amounts of android time in the generation of a date comment when storing to
 * file.
 * AppEnvironmentData.java
 * 
 * @author naikon
 * @since 1.0.0 Jul 10, 2011
 * @version 1.0.0 Jul 10, 2011
 */
public class AppEnvironmentData extends EnumMap<AppEnvironmentField, String> {

   /** The Constant serialVersionUID. */
   private static final long serialVersionUID = -2968448448559351368L;

   /**
    * Constructs a new {@code Properties} object.
    */
   public AppEnvironmentData() {
      super(AppEnvironmentField.class);
   }

}
