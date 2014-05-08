package org.assertj.guava.api;

import com.google.common.base.Optional;
import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;

public class OptionalAssert_equals_Test extends BaseTest {

  @Test
  public void should_pass_if_actual_equals_option() {
   // given
   Optional<String> expected = Optional.fromNullable("some value");
   Optional<String> actual = Optional.fromNullable("some value");
   // when
   assertThat(actual).isEqualTo(expected);
   // then
   // pass
  }

  @Test
  public void should_fail_if_actual_is_absent_and_expected_is_present() throws Exception {
   // given
   Optional<String> expected = Optional.fromNullable("X");
   Optional<String> actual = Optional.absent();
   // expect
   expectException(AssertionError.class,
     "Expecting Optional to contain a non-null instance but contained nothing (absent Optional)");
   // when
   assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void should_fail_if_actual_is_present_and_expected_is_absent() throws Exception {
   // given
   Optional<String> expected = Optional.absent();
   Optional<String> actual = Optional.fromNullable("X");
   // expect
   expectException(AssertionError.class,
     "Expecting Optional to contain nothing (absent Optional) but contained <\"X\">");
   // when
   assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void should_fail_if_actual_and_expected_values_are_not_equal() throws Exception {
   // given
   Optional<String> expected = Optional.fromNullable("X");
   Optional<String> actual = Optional.fromNullable("Y");
   // expect
   expectException(AssertionError.class,
     "\nExpecting Optional to contain value \n<\"X\">\n but contained \n<\"Y\">");
   // when
   assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void should_fail_if_actual_is_null() throws Exception {
   // given
   Optional<String> expected = Optional.fromNullable("X");
   Optional<String> actual = null;
   // expect
   expectException(AssertionError.class, actualIsNull());
   // when
   assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void should_fail_if_expected_is_null() throws Exception {
   // given
   Optional<String> actual = Optional.fromNullable("X");
   // expect
   expectException(AssertionError.class,
     "\nExpected Optional was null");
   // when
   assertThat(actual).isEqualTo(null);
  }
}
