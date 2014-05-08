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

import static org.assertj.guava.error.OptionalShouldBeAbsent.shouldBeAbsent;
import static org.assertj.guava.error.OptionalShouldBePresent.shouldBePresent;
import static org.assertj.guava.error.OptionalShouldBePresentWithValue.shouldBePresentWithValue;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.assertj.core.util.VisibleForTesting;

import com.google.common.base.Optional;

/**
 * 
 * Assertions for guava {@link Optional}.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(Optional)}</code>
 * <p>
 * 
 * @param <T> the type of elements of the tested Optional value
 * 
 * @author Kornel Kiełczewski
 */
public class OptionalAssert<T> extends AbstractAssert<OptionalAssert<T>, Optional<T>> {

  @VisibleForTesting
  Failures failures = Failures.instance();

  protected OptionalAssert(final Optional<T> actual) {
   super(actual, OptionalAssert.class);
  }

  // visible for test
  protected Optional<T> getActual() {
   return actual;
  }

  /**
   * Verifies that the actual {@link Optional} contains the given value.<br>
   * <p>
   * Example :
   * 
   * <pre>
   * Optional&lt;String&gt; optional = Optional.of(&quot;Test&quot;);
   *
   * assertThat(optional).hasValue(&quot;Test&quot;);
   * </pre>
   * 
   * @param value the value to look for in actual {@link Optional}.
   * @return this {@link OptionalAssert} for assertions chaining.
   * 
   * @throws AssertionError if the actual {@link Optional} is {@code null}.
   * @throws AssertionError if the actual {@link Optional} contains nothing or does not have the given value.
   */
  public OptionalAssert<T> contains(final Object value) {
   Objects.instance().assertNotNull(info, actual);
   if (!actual.isPresent()) {
     throw failures.failure(info, shouldBePresentWithValue(value));
   }
   if (!actual.get().equals(value)) {
     throw failures.failure(info, shouldBePresentWithValue(actual, value));
   }
   return this;
  }

  /**
   * Verifies that the actual {@link Optional} contained instance is absent/null (ie. not {@link Optional#isPresent()}).<br>
   * <p>
   * Example :
   * 
   * <pre>
   * Optional&lt;String&gt; optional = Optional.absent();
   *
   * assertThat(optional).isAbsent();
   * </pre>
   * 
   * @return this {@link OptionalAssert} for assertions chaining.
   * 
   * @throws AssertionError if the actual {@link Optional} is {@code null}.
   * @throws AssertionError if the actual {@link Optional} contains a (non-null) instance.
   */
  public OptionalAssert<T> isAbsent() {
   Objects.instance().assertNotNull(info, actual);
   if (actual.isPresent()) {
     throw failures.failure(info, shouldBeAbsent(actual));
   }
   return this;
  }

  /**
   * Verifies that the actual {@link Optional} contains a (non-null) instance.<br>
   * <p>
   * Example :
   * 
   * <pre>
   * Optional&lt;String&gt; optional = Optional.of(&quot;value&quot;);
   *
   * assertThat(optional).isPresent();
   * </pre>
   * 
   * @return this {@link OptionalAssert} for assertions chaining.
   * 
   * @throws AssertionError if the actual {@link Optional} is {@code null}.
   * @throws AssertionError if the actual {@link Optional} contains a null instance.
   */
  public OptionalAssert<T> isPresent() {
   Objects.instance().assertNotNull(info, actual);
   if (!actual.isPresent()) {
     throw failures.failure(info, shouldBePresent(actual));
   }
   return this;
  }

  /**
   * Verifies that the actual {@link Optional} equals expected {@link Optional} instance.<br>
   * <p>
   * Example :
   *
   * <pre>
   * Optional&lt;String&gt; optional = Optional.of(&quot;value&quot;);
   * Optional&lt;String&gt; equalOptional = Optional.of(&quot;value&quot;);
   *
   * assertThat(optional).equals(equalOptional);
   * </pre><br>
   *
   * Or :
   *
   * <pre>
   * Optional&lt;String&gt; optional = Optional.absent();
   * Optional&lt;String&gt; equalOptional = Optional.absent();
   *
   * assertThat(optional).equals(equalOptional);
   * </pre>
   *
   * @return this {@link OptionalAssert} for assertions chaining.
   *
   * @throws AssertionError if the actual {@link Optional} is {@code null}.
   * @throws AssertionError if the expected {@link Optional} is {@code null}.
   * @throws AssertionError if the actual {@link Optional} is absent while the expected value contains a (non null) instance.
   * @throws AssertionError if the actual {@link Optional} contains a (non null) instance while the expected value is absent.
   */
  public OptionalAssert<T> isEqualTo(Optional<T> expected) {
   Objects.instance().assertNotNull(info, actual);
   if(expected == null){
     failWithMessage("\nExpected Optional was null", expected);
   }
   if (!expected.isPresent() && actual.isPresent()){
     throw failures.failure(info, shouldBeAbsent(actual));
   }
   if (expected.isPresent() && !actual.isPresent()){
     throw failures.failure(info, shouldBePresent(actual));
   }
   if (actual.isPresent() && expected.isPresent()){
     T actualValue = actual.get();
     T expectedValue = expected.get();
     if (!actualValue.equals(expectedValue)){
       throw failures.failure(info, shouldBePresentWithValue(actual, expectedValue));
     }
   }
   return this;
  }
}
