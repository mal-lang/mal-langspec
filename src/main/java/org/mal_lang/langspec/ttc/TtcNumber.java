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

import static java.util.Objects.requireNonNull;

import jakarta.json.Json;
import jakarta.json.JsonObject;

/**
 * Immutable class representing a TTC number in a MAL language.
 *
 * @since 1.0.0
 */
public final class TtcNumber extends TtcExpression {
  private final double value;

  /**
   * Constructs a new {@code TtcNumber} object.
   *
   * @param value the value of the new {@code TtcNumber} object
   * @since 1.0.0
   */
  public TtcNumber(double value) {
    this.value = value;
  }

  /**
   * Returns the value of this {@code TtcNumber} object.
   *
   * @return the value of this {@code TtcNumber} object
   * @since 1.0.0
   */
  public double getValue() {
    return this.value;
  }

  @Override
  public double getMeanTtc() {
    return this.value;
  }

  @Override
  public JsonObject toJson() {
    return Json.createObjectBuilder().add("type", "number").add("value", this.value).build();
  }

  /**
   * Creates a new {@code TtcNumber} from a {@link jakarta.json.JsonObject}.
   *
   * @param jsonTtcNumber the {@link jakarta.json.JsonObject}
   * @return a new {@code TtcNumber}
   * @throws java.lang.NullPointerException if {@code jsonTtcNumber} is {@code null}
   * @since 1.0.0
   */
  public static TtcNumber fromJson(JsonObject jsonTtcNumber) {
    requireNonNull(jsonTtcNumber);
    return new TtcNumber(jsonTtcNumber.getJsonNumber("value").doubleValue());
  }
}
