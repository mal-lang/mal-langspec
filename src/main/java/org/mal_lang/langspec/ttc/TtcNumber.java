/*
 * Copyright 2020-2021 Foreseeti AB <https://foreseeti.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mal_lang.langspec.ttc;

import jakarta.json.Json;
import jakarta.json.JsonValue;

/** Immutable class representing a TTC number of an attack step or a defense in a MAL language. */
public final class TtcNumber extends TtcExpression {
  private final double value;

  /**
   * Constructs a new {@code TtcNumber} object.
   *
   * @param value the value of the new {@code TtcNumber} object
   */
  public TtcNumber(double value) {
    super(TtcExpression.NUMBER);
    this.value = value;
  }

  /**
   * Returns the value of this {@code TtcNumber} object.
   *
   * @return the value of this {@code TtcNumber} object
   */
  public double getValue() {
    return value;
  }

  @Override
  public double getMeanTtc() {
    return value;
  }

  @Override
  public double getMeanProbability() {
    throw new UnsupportedOperationException();
  }

  @Override
  public JsonValue toJson() {
    return Json.createObjectBuilder().add("type", getType()).add("value", value).build();
  }
}
