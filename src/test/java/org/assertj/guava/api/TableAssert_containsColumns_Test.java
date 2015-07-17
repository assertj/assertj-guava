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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.guava.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;

import org.junit.Test;

/**
 * @author Jan Gorman
 */
public class TableAssert_containsColumns_Test extends TableAssertBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    expectException(AssertionError.class, actualIsNull());
    actual = null;
    assertThat(actual).containsColumns(1, 2);
  }

  @Test
  public void should_pass_if_actual_contains_columns() {
    assertThat(actual).containsColumns(3, 4);
  }

  @Test
  public void should_fail_if_columns_to_look_for_are_null() {
    expectException(IllegalArgumentException.class, "The columns to look for should not be null");
    assertThat(actual).containsColumns((Integer[]) null);
  }

  @Test
  public void should_fail_if_columns_to_look_for_are_empty() {
    expectException(IllegalArgumentException.class, "The columns to look for should not be empty");
    assertThat(actual).containsColumns();
  }

  @Test
  public void should_fail_if_actual_does_not_contain_columns() {
    try {
      assertThat(actual).containsColumns(9, 6);
    } catch (AssertionError e) {
      assertThat(e).hasMessage(String.format("%nExpecting:%n" +
                               "  <{1={4=Franklin Pierce, 3=Millard Fillmore}, 2={5=Grover Cleveland}}>%n" +
                               "to contain columns:%n" +
                               "  <[9, 6]>%n" +
                               "but could not find:%n" +
                               "  <[6, 9]>"));
      return;
    }
    fail("Assertion error expected.");
  }

}
