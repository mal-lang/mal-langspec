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

import jakarta.json.Json;
import jakarta.json.JsonObject;

/**
 * Immutable class representing a binary TTC operation in a MAL language.
 *
 * @since 1.0.0
 */
public abstract class TtcBinaryOperation extends TtcExpression {
  private final TtcExpression lhs;
  private final TtcExpression rhs;

  TtcBinaryOperation(TtcExpression lhs, TtcExpression rhs) {
    this.lhs = requireNonNull(lhs);
    this.rhs = requireNonNull(rhs);
  }

  /**
   * Returns the left-hand side of this {@code TtcBinaryOperation} object.
   *
   * @return the left-hand side of this {@code TtcBinaryOperation} object
   * @since 1.0.0
   */
  public TtcExpression getLhs() {
    return this.lhs;
  }

  /**
   * Returns the right-hand side of this {@code TtcBinaryOperation} object.
   *
   * @return the right-hand side of this {@code TtcBinaryOperation} object
   * @since 1.0.0
   */
  public TtcExpression getRhs() {
    return this.rhs;
  }

  JsonObject toJson(String type) {
    return Json.createObjectBuilder()
        .add("type", type)
        .add("lhs", this.lhs.toJson())
        .add("rhs", this.rhs.toJson())
        .build();
  }

  /**
   * Creates a new {@code TtcBinaryOperation} from a {@link jakarta.json.JsonObject}.
   *
   * @param jsonTtcBinaryOperation the {@link jakarta.json.JsonObject}
   * @return a new {@code TtcBinaryOperation}
   * @throws java.lang.NullPointerException if {@code jsonTtcBinaryOperation} is {@code null}
   * @since 1.0.0
   */
  public static TtcBinaryOperation fromJson(JsonObject jsonTtcBinaryOperation) {
    requireNonNull(jsonTtcBinaryOperation);
    var lhs = TtcExpression.fromJson(jsonTtcBinaryOperation.getJsonObject("lhs"));
    var rhs = TtcExpression.fromJson(jsonTtcBinaryOperation.getJsonObject("rhs"));
    switch (jsonTtcBinaryOperation.getString("type")) {
      case "addition":
        return new TtcAddition(lhs, rhs);
      case "subtraction":
        return new TtcSubtraction(lhs, rhs);
      case "multiplication":
        return new TtcMultiplication(lhs, rhs);
      case "division":
        return new TtcDivision(lhs, rhs);
      case "exponentiation":
        return new TtcExponentiation(lhs, rhs);
      default:
        throw new RuntimeException(
            String.format(
                "Invalid TTC expression type \"%s\"", jsonTtcBinaryOperation.getString("type")));
    }
  }
}
