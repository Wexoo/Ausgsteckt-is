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
 * The Class SQLiteCondition.
 * 
 * @author wexoo
 * @version Aug 27, 2011
 */
public class SQLiteCondition {

   /** The operator. */
   private LogicalOperator operator;

   /** The condition. */
   private String condition;

   /** The parenthesis. */
   private ParenthesisBehaviour parenthesis;

   /**
    * Instantiates a new sQ lite condition.
    * 
    * @param operator the operator
    * @param condition the condition
    */
   public SQLiteCondition(final LogicalOperator operator, final String condition) {
      this.operator = operator;
      this.condition = condition;
      this.parenthesis = ParenthesisBehaviour.NONE;
   }

   /**
    * Instantiates a new sQ lite condition.
    * 
    * @param operator the operator
    * @param condition the condition
    * @param parenthesis the parenthesis
    */
   public SQLiteCondition(final LogicalOperator operator, final String condition, final ParenthesisBehaviour parenthesis) {
      this.operator = operator;
      this.condition = condition;
      this.parenthesis = parenthesis;
   }

   /**
    * Gets the operator.
    * 
    * @return the operator
    */
   public LogicalOperator getOperator() {
      return this.operator;
   }

   /**
    * Gets the condition.
    * 
    * @return the condition
    */
   public String getCondition() {
      return this.condition;
   }

   /**
    * Gets the parenthesis.
    * 
    * @return the parenthesis
    */
   public ParenthesisBehaviour getParenthesis() {
      return this.parenthesis;
   }

   /**
    * Sets the operator.
    * 
    * @param operator the new operator
    */
   public void setOperator(final LogicalOperator operator) {
      this.operator = operator;
   }

   /**
    * Sets the condition.
    * 
    * @param condition the new condition
    */
   public void setCondition(final String condition) {
      this.condition = condition;
   }

   /**
    * Sets the parenthesis.
    * 
    * @param parenthesis the new parenthesis
    */
   public void setParenthesis(final ParenthesisBehaviour parenthesis) {
      this.parenthesis = parenthesis;
   }
}
