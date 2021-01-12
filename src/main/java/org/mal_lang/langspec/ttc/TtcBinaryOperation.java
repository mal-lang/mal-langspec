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

import static org.mal_lang.langspec.Utils.checkNotNull;

import jakarta.json.Json;
import jakarta.json.JsonValue;

/**
 * Immutable class representing a TTC binary operation of an attack step or a defense in a MAL
 * language.
 */
public abstract class TtcBinaryOperation extends TtcExpression {
  private final TtcExpression lhs;
  private final TtcExpression rhs;

  TtcBinaryOperation(String type, TtcExpression lhs, TtcExpression rhs) {
    super(type);
    checkNotNull(lhs, rhs);
    this.lhs = lhs;
    this.rhs = rhs;
  }

  /**
   * Returns the left-hand side of this {@code TtcBinaryOperation} object.
   *
   * @return the left-hand side of this {@code TtcBinaryOperation} object
   */
  public TtcExpression getLhs() {
    return lhs;
  }

  /**
   * Returns the right-hand side of this {@code TtcBinaryOperation} object.
   *
   * @return the right-hand side of this {@code TtcBinaryOperation} object
   */
  public TtcExpression getRhs() {
    return rhs;
  }

  @Override
  public double getMeanProbability() {
    throw new UnsupportedOperationException();
  }

  @Override
  public JsonValue toJson() {
    return Json.createObjectBuilder()
        .add("type", getType())
        .add("lhs", lhs.toJson())
        .add("rhs", rhs.toJson())
        .build();
  }
}
