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
import jakarta.json.JsonObject;

/**
 * Immutable class representing a TTC division in a MAL language.
 *
 * @since 1.0.0
 */
public final class TtcDivision extends TtcBinaryOperation {
  /**
   * Constructs a new {@code TtcDivision} object.
   *
   * @param lhs the left-hand side of the division
   * @param rhs the right-hand side of the division
   * @throws java.lang.NullPointerException if {@code lhs} or {@code rhs} is {@code null}
   * @since 1.0.0
   */
  public TtcDivision(TtcExpression lhs, TtcExpression rhs) {
    super(lhs, rhs);
  }

  @Override
  public double getMeanTtc() {
    double lhsMean = getLhs().getMeanTtc();
    double rhsMean = getRhs().getMeanTtc();
    return lhsMean / rhsMean;
  }

  @Override
  public JsonObject toJson() {
    return Json.createObjectBuilder()
        .add("type", "division")
        .add("lhs", this.getLhs().toJson())
        .add("rhs", this.getRhs().toJson())
        .build();
  }
}
