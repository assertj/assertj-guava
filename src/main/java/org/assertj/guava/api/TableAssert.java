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
package org.assertj.guava.api;

import static com.google.common.base.Preconditions.checkArgument;

import static org.assertj.guava.error.ShouldContainValues.shouldContainValues;
import static org.assertj.guava.error.TableShouldContainColumns.tableShouldContainColumns;
import static org.assertj.guava.error.TableShouldContainRows.tableShouldContainRows;
import static org.assertj.guava.error.TableShouldHaveRowCount.tableShouldHaveRowCount;
import static org.assertj.guava.error.TableShouldHaveColumnCount.tableShouldHaveColumnCount;

import java.util.Set;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.error.ShouldBeEmpty;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.assertj.core.util.VisibleForTesting;

import com.google.common.collect.Sets;
import com.google.common.collect.Table;

/**
 * @author Jan Gorman
 */
public class TableAssert<R, C, V> extends AbstractAssert<TableAssert<R, C, V>, Table<R, C, V>> {

  @VisibleForTesting
  Failures failures = Failures.instance();

  protected TableAssert(Table<R, C, V> actual) {
    super(actual, TableAssert.class);
  }

  // visible for test
  protected Table<R, C, V> getActual() {
    return actual;
  }

  /**
   * Verifies that the actual {@link Table} has the expected number of rows.
   *
   * <p>
   * Example :
   *
   * <pre>
   * Table<Integer, Integer, String> actual actual = HashBasedTable.create();;
   *
   * actual.put(1, 3, "Millard Fillmore");
   * actual.put(1, 4, "Franklin Pierce");
   * actual.put(2, 5, "Grover Cleveland");
   *
   * assertThat(actual).hasRowCount(2);
   * </pre>
   *
   * @param rows The columns to look for in the actual {@link Table}
   * @return this {@link TableAssert} for assertion chaining.
   * @throws IllegalArgumentException if the expected size is negative
   * @throws AssertionError           if the actual {@link Table} is {@code null}.
   * @throws AssertionError           if the actual {@link Table} does not have the expected row size.
   */  
  public TableAssert<R, C, V> hasRowCount(int expectedSize) {
    Objects.instance().assertNotNull(info, actual);
    checkArgument(expectedSize >= 0, "The expected size should not be negative.");

    if (actual.rowKeySet().size() != expectedSize) {
      throw failures.failure(info, tableShouldHaveRowCount(actual, actual.rowKeySet().size(), expectedSize));
    }
    return myself;
  }

  /**
   * Verifies that the actual {@link Table} has the expected number of rows.
   *
   * <p>
   * Example :
   *
   * <pre>
   * Table<Integer, Integer, String> actual actual = HashBasedTable.create();;
   *
   * actual.put(1, 3, "Millard Fillmore");
   * actual.put(1, 4, "Franklin Pierce");
   * actual.put(2, 5, "Grover Cleveland");
   *
   * assertThat(actual).hasColumnCount(3);
   * </pre>
   *
   * @param rows The columns to look for in the actual {@link Table}
   * @return this {@link TableAssert} for assertion chaining.
   * @throws IllegalArgumentException if the expected size is negative
   * @throws AssertionError           if the actual {@link Table} is {@code null}.
   * @throws AssertionError           if the actual {@link Table} does not have the expected column size.
   */  
  public TableAssert<R, C, V> hasColumnCount(int expectedSize) {
    Objects.instance().assertNotNull(info, actual);
    checkArgument(expectedSize >= 0, "The expected size should not be negative.");

    if (actual.columnKeySet().size() != expectedSize) {
      throw failures.failure(info, tableShouldHaveColumnCount(actual, actual.columnKeySet().size(), expectedSize));
    }
    return myself;
  }

  /**
   * Verifies that the actual {@link Table} contains the given rows.
   *
   * <p>
   * Example :
   *
   * <pre>
   * Table<Integer, Integer, String> actual actual = HashBasedTable.create();;
   *
   * actual.put(1, 3, "Millard Fillmore");
   * actual.put(1, 4, "Franklin Pierce");
   * actual.put(2, 5, "Grover Cleveland");
   *
   * assertThat(actual).containsRows(1, 2);
   * </pre>
   *
   * @param rows The columns to look for in the actual {@link Table}
   * @return this {@link TableAssert} for assertion chaining.
   * @throws IllegalArgumentException if no param rows have been set.
   * @throws AssertionError           if the actual {@link Table} is {@code null}.
   * @throws AssertionError           if the actual {@link Table} does not contain the given rows.
   */
  public TableAssert<R, C, V> containsRows(R... rows) {
    Objects.instance().assertNotNull(info, actual);
    checkArgument(rows != null, "The rows to look for should not be null.");
    checkArgument(rows.length > 0, "The rows to look for should not be empty.");

    Set<R> rowsNotFound = Sets.newHashSet();
    for (R row : rows) {
      if (!actual.containsRow(row)) {
        rowsNotFound.add(row);
      }
    }

    if (!rowsNotFound.isEmpty()) {
      throw failures.failure(info, tableShouldContainRows(actual, rows, rowsNotFound));
    }
    return myself;
  }

  /**
   * Verifies that the actual {@link Table} contains the given columns.
   *
   * <p>
   * Example :
   *
   * <pre>
   * Table<Integer, Integer, String> actual actual = HashBasedTable.create();;
   *
   * actual.put(1, 3, "Millard Fillmore");
   * actual.put(1, 4, "Franklin Pierce");
   * actual.put(2, 5, "Grover Cleveland");
   *
   * assertThat(actual).containsColumns(3, 4);
   * </pre>
   *
   * @param columns The columns to look for in the actual {@link Table}
   * @return this {@link TableAssert} for assertion chaining.
   * @throws IllegalArgumentException if no param columns have been set.
   * @throws AssertionError           if the actual {@link Table} is {@code null}.
   * @throws AssertionError           if the actual {@link Table} does not contain the given columns.
   */
  public TableAssert<R, C, V> containsColumns(C... columns) {
    Objects.instance().assertNotNull(info, actual);
    checkArgument(columns != null, "The columns to look for should not be null.");
    checkArgument(columns.length > 0, "The columns to look for should not be empty.");

    Set<C> columnsNotFound = Sets.newHashSet();
    for (C column : columns) {
      if (!actual.containsColumn(column)) {
        columnsNotFound.add(column);
      }
    }

    if (!columnsNotFound.isEmpty()) {
      throw failures.failure(info, tableShouldContainColumns(actual, columns, columnsNotFound));
    }

    return myself;
  }

  /**
   * Verifies that the actual {@link Table} contains the given values for any key.
   *
   * <p>
   * Example :
   *
   * <pre>
   * Table<Integer, Integer, String> actual actual = HashBasedTable.create();;
   *
   * actual.put(1, 3, "Millard Fillmore");
   * actual.put(1, 4, "Franklin Pierce");
   * actual.put(2, 5, "Grover Cleveland");
   *
   * assertThat(actual).containsValues("Franklin Pierce", "Millard Fillmore");
   * </pre>
   *
   * @param values The values to look for in the actual {@link Table}
   * @return this {@link TableAssert} for assertion chaining.
   * @throws IllegalArgumentException if no param values have been set.
   * @throws AssertionError           if the actual {@link Table} is {@code null}.
   * @throws AssertionError           if the actual {@link Table} does not contain the given values.
   */
  public TableAssert<R, C, V> containsValues(V... values) {
    Objects.instance().assertNotNull(info, actual);
    checkArgument(values != null, "The values to look for should not be null.");
    checkArgument(values.length > 0, "The values to look for should not be empty.");

    Set<V> valuesNotFound = Sets.newHashSet();
    for (V value : values) {
      if (!actual.containsValue(value)) {
        valuesNotFound.add(value);
      }
    }

    if (!valuesNotFound.isEmpty()) {
      throw failures.failure(info, shouldContainValues(actual, values, valuesNotFound));
    }

    return myself;
  }

  /**
   * Verifies that the actual {@link Table} is empty.
   *
   * <p>
   * Example :
   *
   * <pre>
   * Table<Integer, Integer, String> actual actual = HashBasedTable.create();;
   *
   * assertThat(actual).isEmpty();
   * </pre>
   *
   * @throws AssertionError if the actual {@link Table} is {@code null}.
   * @throws AssertionError if the actual {@link Table} is not empty.
   */
  public void isEmpty() {
    Objects.instance().assertNotNull(info, actual);
    if (!actual.isEmpty()) {
      throw failures.failure(info, ShouldBeEmpty.shouldBeEmpty(actual));
    }
  }
}
