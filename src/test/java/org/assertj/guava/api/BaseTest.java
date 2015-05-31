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
 * Copyright 2012-2014 the original author or authors.
 */
package org.assertj.guava.api;

import static java.lang.String.format;
import static org.junit.rules.ExpectedException.none;

import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 * 
 * Base test class for all assertj-guava unit tests.
 *
 * @author Joel Costigliola
 *
 */
public class BaseTest {

  @Rule
  public ExpectedException thrown = none();

  public BaseTest() {
    super();
  }

  protected void expectException(Class<? extends Throwable> type, String message) {
    thrown.expect(type);
    thrown.expectMessage(format(message));
  }

}