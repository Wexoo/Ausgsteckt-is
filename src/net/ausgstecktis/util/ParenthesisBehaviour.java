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

/**
 * ParenthesisBehaviour for SQLiteCondition
 * 
 * @author wexoo
 * @version Aug 27, 2011
 */
public enum ParenthesisBehaviour {

   OPEN("("),
   CLOSE(")"),
   NONE("");

   private String name;

   private ParenthesisBehaviour(final String name) {
      this.name = name;
   }

   
   public String toString() {
      return name;
   }
}