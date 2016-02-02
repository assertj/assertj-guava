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

import com.google.common.collect.Multiset;
import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Integers;
import org.assertj.core.internal.Objects;
import org.assertj.core.util.VisibleForTesting;

import static com.google.common.base.Preconditions.checkArgument;
import static org.assertj.guava.error.MultisetShouldContainAtLeastTimes.shouldContainAtLeastTimes;
import static org.assertj.guava.error.MultisetShouldContainAtMostTimes.shouldContainAtMostTimes;
import static org.assertj.guava.error.MultisetShouldContainTimes.shouldContainTimes;

/**
 *
 * Assertions for guava {@link Multiset}.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(Multiset)}</code>
 * <p>
 *
 * @param <T> the type of elements contained in the tested Multiset value
 *
 * @author Max Daniline
 */
public class MultisetAssert<T> extends AbstractIterableAssert<MultisetAssert<T>, Multiset<? extends T>, T> {

  @VisibleForTesting
  Failures failures = Failures.instance();

  protected MultisetAssert(Multiset<? extends T> actual) {
    super(actual, MultisetAssert.class);
  }

  /**
   * Verifies the actual {@link Multiset} contains the given value exactly the given number of times.
   *
   * @param expectedCount the exact number of times the given value should appear in the set
   * @param expected the value which to expect
   *
   * @return this {@link MultisetAssert} for fluent chaining
   *
   * @throws AssertionError if the actual {@link Multiset} is null
   * @throws AssertionError if the actual {@link Multiset} contains the given value a number of times different to the given count
   */
  public MultisetAssert<T> contains(int expectedCount, T expected) {
    Objects.instance().assertNotNull(info, actual);
    checkArgument(expectedCount >= 0, "The expected count should not be negative.");
    int actualCount = actual.count(expected);
    if (actualCount != expectedCount) {
      throw failures.failure(info, shouldContainTimes(actual, expected, expectedCount, actualCount));
    }
    return myself;
  }

  /**
   * Verifies the actual {@link Multiset} contains the given value exactly the given number of times.
   *
   * @param minimumCount the minimum number of times the given value should appear in the set
   *
   * @param expected the value which to expect
   * @return this {@link MultisetAssert} for fluent chaining
   *
   * @throws AssertionError if the actual {@link Multiset} is null
   * @throws AssertionError if the actual {@link Multiset} contains the given value fewer times than the given count
   */
  public MultisetAssert<T> containsAtLeast(int minimumCount, T expected) {
    Objects.instance().assertNotNull(info, actual);
    checkArgument(minimumCount >= 0, "The minimum count should not be negative.");
    int actualCount = actual.count(expected);
    if (actualCount < minimumCount) {
      throw failures.failure(info, shouldContainAtLeastTimes(actual, expected, minimumCount, actualCount));
    }
    return myself;
  }

  /**
   * Verifies the actual {@link Multiset} contains the given value exactly the given number of times.
   *
   * @param maximumCount the maximum number of times the given value should appear in the set
   *
   * @param expected the value which to expect
   * @return this {@link MultisetAssert} for fluent chaining
   *
   * @throws AssertionError if the actual {@link Multiset} is null
   * @throws AssertionError if the actual {@link Multiset} contains the given value more times than the given count
   */
  public MultisetAssert<T> containsAtMost(int maximumCount, T expected) {
    Objects.instance().assertNotNull(info, actual);
    checkArgument(maximumCount >= 0, "The maximum count should not be negative.");
    int actualCount = actual.count(expected);
    if (actualCount > maximumCount) {
      throw failures.failure(info, shouldContainAtMostTimes(actual, expected, maximumCount, actualCount));
    }
    return myself;
  }
}
