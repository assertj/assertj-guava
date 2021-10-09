/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.guava.api;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldBeNullOrEmpty.shouldBeNullOrEmpty;
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.error.ShouldHaveSize.shouldHaveSize;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.IterableUtil.toArray;

import java.util.List;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.util.VisibleForTesting;
import org.assertj.guava.internal.RangeSets;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;

/**
 * Assertion for guava {@link com.google.common.collect.RangeSet}.
 * <p>
 * To create an instance of this class, invoke <code>{@link
 * org.assertj.guava.api.Assertions#assertThat(com.google.common.collect.RangeSet)}</code>
 * <p>
 *
 * @param <T> the type of the tested RangeSet elements
 * @author Ilya Koshaleu
 */
public class RangeSetAssert<T extends Comparable<T>> extends AbstractAssert<RangeSetAssert<T>, RangeSet<T>> {

  @VisibleForTesting
  RangeSets rangeSets = RangeSets.instance();

  protected RangeSetAssert(RangeSet<T> actual) {
    super(actual, RangeSetAssert.class);
  }

  /**
   * Verifies that the given {@code RangeSet} has specific {@code size} of disconnected {@code Range} elements.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).hasSize(3);</code></pre>
   *
   * @param size expected amount of disconnected {@code Range} elements.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual size of {@code RangeSet} is different from the expected {@code size}.
   */
  public RangeSetAssert<T> hasSize(int size) {
    isNotNull();
    assertHasSize(size);
    return myself;
  }

  private void assertHasSize(int expectedSize) {
    int actualSize = actual.asRanges().size();
    if (actualSize != expectedSize) throwAssertionError(shouldHaveSize(actual, actualSize, expectedSize));
  }

  /**
   * Verifies that the given {@code RangeSet} contains the given values.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).contains(50, 270, 550);</code></pre>
   *
   * @param values the values to look for in actual {@code RangeSet}.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not contain the given {@code values}.
   * @throws IllegalArgumentException if values are null or values are empty while actual is not empty.
   */
  @SafeVarargs
  public final RangeSetAssert<T> contains(T... values) {
    isNotNull();
    assertContains(values);
    return myself;
  }

  private void assertContains(T[] values) {
    requireNonNull(values, shouldNotBeNull("values")::create);
    if (actual.isEmpty() && values.length == 0) return;
    failIfEmpty(values);
    assertRangeSetContainsGivenValues(actual, values);
  }

  /**
   * Verifies that the given {@code RangeSet} contains all the given values.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).containsAll(Arrays.asList(50, 270, 550));</code></pre>
   *
   * @param values the values to look for in actual {@code RangeSet}.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not contain all the given {@code values}.
   * @throws IllegalArgumentException if values are null or values are empty while actual is not empty.
   */
  public RangeSetAssert<T> containsAll(Iterable<T> values) {
    isNotNull();
    assertContainsAll(values);
    return myself;
  }

  private void assertContainsAll(Iterable<T> values) {
    requireNonNull(values, shouldNotBeNull("values")::create);
    if (actual.isEmpty() && !values.iterator().hasNext()) return;
    failIfEmpty(values);
    assertRangeSetContainsGivenValues(actual, toArray(values, Comparable.class));
  }

  /**
   * Verifies that the given {@code RangeSet} contains at least one of the given ranges.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).containsAnyOf(150, 250, 700);</code></pre>
   *
   * @param ranges the ranges to look for in actual {@code RangeSet}.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not contain at least one of the given {@code ranges}.
   * @throws IllegalArgumentException if ranges are null or ranges are empty while actual is not empty.
   */
  @SafeVarargs
  public final RangeSetAssert<T> containsAnyOf(final T... ranges) {
    rangeSets.assertContainsAnyOf(info, actual, ranges);
    return myself;
  }

  /**
   * Verifies that the given {@code RangeSet} contains at least one of the given ranges.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).containsAnyRangesOf(Arrays.asList(150, 250, 700));</code></pre>
   *
   * @param ranges the ranges to look for in actual {@code RangeSet}.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not contain at least one of the given {@code ranges}.
   * @throws IllegalArgumentException if ranges are null or ranges are empty while actual is not empty.
   */
  public RangeSetAssert<T> containsAnyRangesOf(final Iterable<T> ranges) {
    rangeSets.assertContainsAnyRangesOf(info, actual, ranges);
    return myself;
  }

  /**
   * Verifies that the given {@code RangeSet} does not contain any of the given ranges.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).doesNotContain(150, 320, 650);</code></pre>
   *
   * @param ranges the ranges that should not be present in actual {@code RangeSet}
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} contains any of the given {@code ranges}.
   * @throws IllegalArgumentException if ranges are null or ranges are empty.
   */
  @SafeVarargs
  public final RangeSetAssert<T> doesNotContain(final T... ranges) {
    rangeSets.assertDoesNotContain(info, actual, ranges);
    return myself;
  }

  /**
   * Verifies that the given {@code RangeSet} does not contain any of the given ranges.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).doesNotContain(Arrays.asList(150, 320, 650));</code></pre>
   *
   * @param ranges the ranges that should not be present in actual {@code RangeSet}
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} contains any of the given {@code ranges}.
   * @throws IllegalArgumentException if ranges are null or ranges are empty.
   */
  public RangeSetAssert<T> doesNotContainAll(final Iterable<T> ranges) {
    rangeSets.assertDoesNotContainAll(info, actual, ranges);
    return myself;
  }

  /**
   * Verifies that the actual {@code RangeSet} is empty.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * assertThat(rangeSet).isEmpty();</code></pre>
   *
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} is not empty.
   */
  public RangeSetAssert<T> isEmpty() {
    isNotNull();
    assertEmpty();
    return myself;
  }

  private void assertEmpty() {
    if (!actual.isEmpty()) throwAssertionError(shouldBeEmpty(actual));
  }

  /**
   * Verifies that the actual {@code RangeSet} is not empty.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).isNotEmpty();</code></pre>
   *
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} is empty.
   */
  public RangeSetAssert<T> isNotEmpty() {
    isNotNull();
    assertNotEmpty();
    return myself;
  }

  private void assertNotEmpty() {
    if (actual.isEmpty()) throwAssertionError(shouldNotBeEmpty());
  }

  /**
   * Verifies that the actual {@code RangeSet} is {@code null} or empty.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * assertThat(rangeSet).isNullOrEmpty();</code></pre>
   *
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is not {@code null} or not empty.
   */
  public RangeSetAssert<T> isNullOrEmpty() {
    assertNullOrEmpty();
    return myself;
  }

  private void assertNullOrEmpty() {
    if (actual != null && !actual.isEmpty()) throwAssertionError(shouldBeNullOrEmpty(actual));
  }

  /**
   * Verifies that the given {@code RangeSet} intersects all the given ranges.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).intersects(Range.closed(50, 150),
   *                                 Range.openClosed(170, 220),
   *                                 Range.open(520, 570));</code></pre>
   *
   * @param ranges the ranges to check whether they intersect the given {@code RangeSet}.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not intersect all the given ranges.
   * @throws IllegalArgumentException if ranges are null or ranges are empty while actual is not empty.
   */
  @SafeVarargs
  public final RangeSetAssert<T> intersects(final Range<T>... ranges) {
    rangeSets.assertIntersects(info, actual, ranges);
    return myself;
  }

  /**
   * Verifies that the given {@code RangeSet} intersects all the given range set.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).intersectsAll(ImmutableRangeSet.of(Range.closed(50, 250)));</code></pre>
   *
   * @param rangeSet the range set to check whether it intersects the actual {@code RangeSet}.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not intersect all the ranges from the given range set.
   * @throws IllegalArgumentException if range set is null or it is empty while actual is not empty.
   */
  public RangeSetAssert<T> intersectsAll(final RangeSet<T> rangeSet) {
    rangeSets.assertIntersectsAll(info, actual, rangeSet);
    return myself;
  }

  /**
   * Verifies that the given {@code RangeSet} intersects all the given ranges.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).intersectsAll(Arrays.asList(Range.closed(50, 150),
   *                                                  Range.openClosed(170, 220),
   *                                                  Range.open(520, 570)));</code></pre>
   *
   * @param ranges the ranges to check whether they all intersect the given {@code RangeSet}.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not intersect all the given ranges.
   * @throws IllegalArgumentException if ranges are null or ranges are empty while actual is not empty.
   */
  public RangeSetAssert<T> intersectsAll(final Iterable<Range<T>> ranges) {
    rangeSets.assertIntersectsAll(info, actual, ranges);
    return myself;
  }

  /**
   * Verifies that the given {@link RangeSet} intersects at least one of the given ranges.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).intersectsAnyOf(Range.closed(50, 150),
   *                                      Range.open(170, 190),
   *                                      Range.open(600, 670));</code></pre>
   *
   * @param ranges the ranges to check whether the actual {@code RangeSet} intersects at least one of them.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not intersect any of the given ranges.
   * @throws IllegalArgumentException if ranges are null or ranges are empty while actual is not empty.
   */
  @SafeVarargs
  public final RangeSetAssert<T> intersectsAnyOf(final Range<T>... ranges) {
    rangeSets.assertIntersectsAnyOf(info, actual, ranges);
    return myself;
  }

  /**
   * Verifies that the given {@code RangeSet} intersects at least one of the given ranges.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).intersectsAnyRangesOf(Arrays.asList(Range.closed(50, 150),
   *                                                          Range.open(170, 190),
   *                                                          Range.open(600, 670));</code></pre>
   *
   * @param ranges the ranges to check whether the actual {@code RangeSet} intersects at least one of them.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not intersect any of the given ranges.
   * @throws IllegalArgumentException if ranges are null or ranges are empty while actual is not empty.
   */
  public RangeSetAssert<T> intersectsAnyRangesOf(final Iterable<Range<T>> ranges) {
    rangeSets.assertIntersectsAnyRangesOf(info, actual, ranges);
    return myself;
  }

  /**
   * Verifies that the given {@code RangeSet} intersects at least one range of the given range set.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).intersectsAnyRangesOf(ImmutableRangeSet.of(Range.close(50, 150)));</code></pre>
   *
   * @param rangeSet the range set with ranges to check whether the actual {@code RangeSet} intersects at least one of
   *                 them.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not intersect any of the ranges from the given ranges
   *         set.
   * @throws IllegalArgumentException if range set is null or it is empty while actual is not empty.
   */
  public RangeSetAssert<T> intersectsAnyRangesOf(final RangeSet<T> rangeSet) {
    rangeSets.assertIntersectsAnyRangesOf(info, actual, rangeSet);
    return myself;
  }

  /**
   * Verifies that the given {@code RangeSet} does not intersect the given ranges.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).doesNotIntersect(Range.closed(120, 150),
   *                                       Range.open(302, 490),
   *                                       Range.open(600, 670));</code></pre>
   *
   * @param ranges the ranges to check whether the actual {@code RangeSet} does not intersect them.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} intersects the given ranges.
   * @throws IllegalArgumentException if ranges are null or ranges are empty while actual is not empty.
   */
  @SafeVarargs
  public final RangeSetAssert<T> doesNotIntersect(final Range<T>... ranges) {
    rangeSets.assertDoesNotIntersect(info, actual, ranges);
    return myself;
  }

  /**
   * Verifies that the given {@code RangeSet} does not intersect ranges from the given range set.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).doesNotIntersectAnyRangeFrom(ImmutableRangeSet.of(Range.close(120, 170)));</code></pre>
   *
   * @param rangeSet the range set to check whether the actual {@code RangeSet} does not intersect ranges from it.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} intersects the ranges from the given range set.
   * @throws IllegalArgumentException if range set is null or it is empty while actual is not empty.
   */
  public RangeSetAssert<T> doesNotIntersectAnyRangeFrom(final RangeSet<T> rangeSet) {
    rangeSets.assertDoesNotIntersectAnyRangeFrom(info, actual, rangeSet);
    return myself;
  }

  /**
   * Verifies that the given {@code RangeSet} does not intersect all the given ranges.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).doesNotIntersectAnyRangeFrom(Arrays.asList(Range.closed(120, 150),
   *                                                                 Range.open(302, 490),
   *                                                                 Range.open(600, 670));</code></pre>
   *
   * @param ranges the ranges to check whether the actual {@code RangeSet} does not intersect them.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} intersects all the given ranges.
   * @throws IllegalArgumentException if ranges are null or ranges are empty while actual is not empty.
   */
  public RangeSetAssert<T> doesNotIntersectAnyRangeFrom(final Iterable<Range<T>> ranges) {
    rangeSets.assertDoesNotIntersectAnyRangeFrom(info, actual, ranges);
    return myself;
  }

  /**
   * Verifies that the given {@code RangeSet} encloses the given ranges.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).encloses(Range.closed(0, 10),
   *                               Range.open(50, 60),
   *                               Range.open(90, 100));</code></pre>
   *
   * @param ranges the ranges to check whether the actual {@code RangeSet} encloses them.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not enclose the given ranges.
   * @throws IllegalArgumentException if ranges are null or ranges are empty while actual is not empty.
   */
  @SafeVarargs
  public final RangeSetAssert<T> encloses(final Range<T>... ranges) {
    rangeSets.assertEncloses(info, actual, ranges);
    return myself;
  }

  /**
   * Verifies that the given {@code RangeSet} encloses all the given ranges.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).enclosesAll(Arrays.asList(Range.closed(0, 10),
   *                                                Range.open(50, 60),
   *                                                Range.open(90, 100)));</code></pre>
   *
   * @param ranges the ranges to check whether the actual {@code RangeSet} encloses all of them.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not enclose all the given ranges.
   * @throws IllegalArgumentException if ranges are null or ranges are empty while actual is not empty.
   */
  public RangeSetAssert<T> enclosesAll(final Iterable<Range<T>> ranges) {
    rangeSets.assertEnclosesAll(info, actual, ranges);
    return myself;
  }

  /**
   * Verifies that the given {@code RangeSet} encloses all ranges from the given range set.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).enclosesAll(ImmutableRangeSet.of(Range.closed(0, 50));</code></pre>
   *
   * @param rangeSet the range set to check whether the actual {@code RangeSet} encloses all range from it.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not enclose all ranges from the given range set.
   * @throws IllegalArgumentException if range set is null or it is empty while actual is not empty.
   */
  public RangeSetAssert<T> enclosesAll(final RangeSet<T> rangeSet) {
    rangeSets.assertEnclosesAll(info, actual, rangeSet);
    return myself;
  }

  /**
   * Verifies that the given {@code RangeSet} encloses at least one of the given ranges.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).enclosesAnyOf(Range.closed(-10, 10),
   *                                    Range.open(150, 260),
   *                                    Range.open(290, 296));</code></pre>
   *
   * @param ranges the ranges to check whether the actual {@code RangeSet} encloses at least one of them.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not enclose at least one of the given ranges.
   * @throws IllegalArgumentException if ranges are null or ranges are empty while actual is not empty.
   */
  @SafeVarargs
  public final RangeSetAssert<T> enclosesAnyOf(final Range<T>... ranges) {
    rangeSets.assertEnclosesAnyOf(info, actual, ranges);
    return myself;
  }

  /**
   * Verifies that the given {@code RangeSet} encloses at least one range of the given ranges.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).enclosesAnyRangesOf(Arrays.asList(Range.closed(-10, 10),
   *                                                        Range.open(150, 260),
   *                                                        Range.open(290, 296)));</code></pre>
   *
   * @param ranges the ranges to check whether the actual {@code RangeSet} encloses at least one of them.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not enclose at least one of the given ranges.
   * @throws IllegalArgumentException if ranges are null or ranges are empty while actual is not empty.
   */
  public RangeSetAssert<T> enclosesAnyRangesOf(final Iterable<Range<T>> ranges) {
    rangeSets.assertEnclosesAnyRangesOf(info, actual, ranges);
    return myself;
  }

  /**
   * Verifies that the given {@code RangeSet} encloses at least one range from the given range set.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * RangeSet&lt;Integer&gt; enclosedSet = TreeRangeSet.create();
   *
   * enclosedSet.add(Range.closed(-10, 10));
   * enclosedSet.add(Range.open(150, 260));
   * enclosedSet.add(Range.open(290, 296));
   *
   * assertThat(rangeSet).enclosesAll(enclosedSet);</code></pre>
   *
   * @param rangeSet the range set to check whether the actual {@code RangeSet} encloses at least one range from it.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not enclose at least one range from the given range set.
   * @throws IllegalArgumentException if range set is null or it is empty while actual is not empty.
   */
  public RangeSetAssert<T> enclosesAnyRangesOf(final RangeSet<T> rangeSet) {
    rangeSets.assertEnclosesAnyRangesOf(info, actual, rangeSet);
    return myself;
  }

  /**
   * Verifies that the given {@code RangeSet} does not enclose the given ranges.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).doesNotEnclose(Range.closed(-10, 10),
   *                                     Range.open(150, 160),
   *                                     Range.open(590, 700));</code></pre>
   *
   * @param ranges the ranges to check whether the actual {@code RangeSet} does not enclose them.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} encloses any of the given ranges.
   * @throws IllegalArgumentException if ranges are null or ranges are empty while actual is not empty.
   */
  @SafeVarargs
  public final RangeSetAssert<T> doesNotEnclose(final Range<T>... ranges) {
    rangeSets.assertDoesNotEnclose(info, actual, ranges);
    return myself;
  }

  /**
   * Verifies that the given {@code RangeSet} does not enclose any of the given ranges.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).doesNotEncloseAnyRangesOf(Arrays.asList(Range.closed(-10, 10),
   *                                                              Range.open(150, 160),
   *                                                              Range.open(590, 700));</code></pre>
   *
   * @param ranges the ranges to check whether the actual {@code RangeSet} does not enclose any of them.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} encloses any of the given ranges.
   * @throws IllegalArgumentException if ranges are null or ranges are empty while actual is not empty.
   */
  public RangeSetAssert<T> doesNotEncloseAnyRangesOf(final Iterable<Range<T>> ranges) {
    rangeSets.doesNotEncloseAnyRangesOf(info, actual, ranges);
    return myself;
  }

  /**
   * Verifies that the given {@code RangeSet} does not enclose any range from the given range set.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * RangeSet&lt;Integer&gt; enclosedSet = TreeRangeSet.create();
   *
   * enclosedSet.add(Range.closed(-10, 10));
   * enclosedSet.add(Range.open(150, 360));
   * enclosedSet.add(Range.open(590, 690));
   *
   * assertThat(rangeSet).doesNotEncloseAnyRangesOf(enclosedSet);</code></pre>
   *
   * @param rangeSet the range set to check whether the actual {@code RangeSet} does not enclose any ranges from it.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} encloses any range from the given range set.
   * @throws IllegalArgumentException if range set is null or it is empty while actual is not empty.
   */
  public RangeSetAssert<T> doesNotEncloseAnyRangesOf(final RangeSet<T> rangeSet) {
    rangeSets.doesNotEncloseAnyRangesOf(info, actual, rangeSet);
    return myself;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private void assertRangeSetContainsGivenValues(RangeSet actual, Comparable[] values) {
    final List<?> elementsNotFound = stream(values).filter(value -> !actual.contains(value)).collect(toList());
    if (!elementsNotFound.isEmpty()) {
      throwAssertionError(shouldContain(actual, values, elementsNotFound));
    }
  }

  private static <T> void failIfEmpty(T[] array) {
    if (array.length == 0) throw new IllegalArgumentException("Expecting values not to be empty");
  }

  private static <T> void failIfEmpty(Iterable<T> iterable) {
    if (!iterable.iterator().hasNext()) throw new IllegalArgumentException("Expecting values not to be empty");
  }

}
