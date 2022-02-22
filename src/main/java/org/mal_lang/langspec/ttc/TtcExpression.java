/*
 * Copyright 2020-2022 Foreseeti AB <https://foreseeti.com>
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

import jakarta.json.JsonObject;

/**
 * Immutable class representing a TTC expression in a MAL language.
 *
 * @since 1.0.0
 */
public abstract class TtcExpression {
  /**
   * Returns the mean TTC of this {@code TtcExpression} object.
   *
   * @return the mean TTC of this {@code TtcExpression} object
   * @throws java.lang.UnsupportedOperationException if this {@code TtcExpression} does not support
   *     mean TTC
   * @since 1.0.0
   */
  public double getMeanTtc() {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the mean probability of this {@code TtcExpression} object.
   *
   * @return the mean probability of this {@code TtcExpression} object
   * @throws java.lang.UnsupportedOperationException if this {@code TtcExpression} does not support
   *     mean probability
   * @since 1.0.0
   */
  public double getMeanProbability() {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the JSON representation of this {@code TtcExpression} object.
   *
   * @return the JSON representation of this {@code TtcExpression} object
   * @since 1.0.0
   */
  public abstract JsonObject toJson();

  /**
   * Creates a new {@code TtcExpression} from a {@link jakarta.json.JsonObject}.
   *
   * @param jsonTtcExpression the {@link jakarta.json.JsonObject}
   * @return a new {@code TtcExpression}
   * @throws java.lang.NullPointerException if {@code jsonTtcExpression} is {@code null}
   * @since 1.0.0
   */
  public static TtcExpression fromJson(JsonObject jsonTtcExpression) {
    requireNonNull(jsonTtcExpression);
    switch (jsonTtcExpression.getString("type")) {
      case "addition":
      case "subtraction":
      case "multiplication":
      case "division":
      case "exponentiation":
        return TtcBinaryOperation.fromJson(jsonTtcExpression);
      case "function":
        return TtcFunction.fromJson(jsonTtcExpression);
      case "number":
        return TtcNumber.fromJson(jsonTtcExpression);
      default:
        throw new RuntimeException(
            String.format(
                "Invalid TTC expression type \"%s\"", jsonTtcExpression.getString("type")));
    }
  }
}
