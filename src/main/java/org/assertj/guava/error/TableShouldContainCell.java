/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright 2012-2013 the original author or authors.
 */
package org.assertj.guava.error;

import static java.lang.String.format;

import com.google.common.collect.Table;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

/**
 * @author David Harris
 */
public class TableShouldContainCell extends BasicErrorMessageFactory {

  /**
   * Creates a new </code>{@link TableShouldContainCell}</code>.
   * @param actual the actual value in the failed assertion.
   * @param actualSize the number of row keys in {@code actual}.
   * @param expectedSize the expected number of row keys.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static <R,C,V> ErrorMessageFactory tableShouldContainCell(Table<R,C,V> actual, R row, C column, V expectedValue, V actualValue) {
    return new TableShouldContainCell(actual, row, column, expectedValue, actualValue);
  }

  private <R,C,V> TableShouldContainCell(Table<R,C,V> actual, R row, C column, V expectedValue, V actualValue) {
    // format the sizes in a standard way, otherwise if we use (for ex) an Hexadecimal representation
    // it will format sizes in hexadecimal while we only want actual to be formatted in hexadecimal
    super(format("\nExpecting row:<%s> and column:<%s> to have value:\n  <%s>\nbut was:\n  <%s>\nin:\n  <%s>", row, column, expectedValue, actualValue, "%s"), actual);
  }

}