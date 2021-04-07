package org.assertj.guava.internal.rangesets;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

import org.assertj.guava.internal.RangeSets;
import org.junit.jupiter.api.Test;

class RangeSets_constructor_Test {

  @Test
  void should_fail_if_assertNotNull_is_null() {
    // WHEN
    Throwable thrown = catchThrowable(() -> new RangeSets(null));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class);
  }

}
