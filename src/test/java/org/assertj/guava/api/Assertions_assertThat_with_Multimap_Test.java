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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.assertj.guava.api.Assertions;
import org.assertj.guava.api.MultimapAssert;
import org.junit.Test;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * Tests for <code>{@link Assertions#assertThat(Multimap)}</code>.
 * 
 * @author Joel Costigliola
 */
public class Assertions_assertThat_with_Multimap_Test {

  @Test
  public void should_create_Assert() {
    Multimap<String, String> actual = HashMultimap.create();
    assertNotNull(Assertions.assertThat(actual));
    assertEquals(MultimapAssert.class, Assertions.assertThat(actual).getClass());
  }

  @Test
  public void should_pass_actual() {
    Multimap<String, String> actual = HashMultimap.create();
    assertSame(actual, Assertions.assertThat(actual).getActual());
  }
}
