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
 * SQLiteCondition.java
 * 
 * @author wexoo
 * @version Aug 27, 2011
 */
public class SQLiteCondition {

   private LogicalOperator operator;

   private String condition;

   private ParenthesisBehaviour parenthesis;

   public SQLiteCondition(final LogicalOperator operator, final String condition) {
      this.operator = operator;
      this.condition = condition;
      parenthesis = ParenthesisBehaviour.NONE;
   }

   public SQLiteCondition(final LogicalOperator operator, final String condition, final ParenthesisBehaviour parenthesis) {
      this.operator = operator;
      this.condition = condition;
      this.parenthesis = parenthesis;
   }

   public LogicalOperator getOperator() {
      return operator;
   }

   public String getCondition() {
      return condition;
   }

   public ParenthesisBehaviour getParenthesis() {
      return parenthesis;
   }

   public void setOperator(final LogicalOperator operator) {
      this.operator = operator;
   }

   public void setCondition(final String condition) {
      this.condition = condition;
   }

   public void setParenthesis(final ParenthesisBehaviour parenthesis) {
      this.parenthesis = parenthesis;
   }
}
