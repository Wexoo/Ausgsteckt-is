/**
 * 
 */
package net.ausgstecktis.ui.slidemenu;

/**
 * @author WeixlbaumerP
 */
public class MenuItemBean {

   private int imageDrawable;
   private int title;
   private int tagId;

   public MenuItemBean(int imageDrawable, int title, int tagId) {
      this.imageDrawable = imageDrawable;
      this.title = title;
      this.tagId = tagId;
   }

   public int getImageDrawable() {
      return imageDrawable;
   }

   public void setImageDrawable(int imageDrawable) {
      this.imageDrawable = imageDrawable;
   }

   public int getTitle() {
      return title;
   }

   public void setTitle(int title) {
      this.title = title;
   }

   public int getTagId() {
      return tagId;
   }

   public void setTagId(int tagId) {
      this.tagId = tagId;
   }
}