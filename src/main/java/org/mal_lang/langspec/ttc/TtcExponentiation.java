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

import jakarta.json.JsonObject;

/**
 * Immutable class representing a TTC exponentiation in a MAL language.
 *
 * @since 1.0.0
 */
public final class TtcExponentiation extends TtcBinaryOperation {
  /**
   * Constructs a new {@code TtcExponentiation} object.
   *
   * @param lhs the left-hand side of the exponentiation
   * @param rhs the right-hand side of the exponentiation
   * @throws java.lang.NullPointerException if {@code lhs} or {@code rhs} is {@code null}
   */
  public TtcExponentiation(TtcExpression lhs, TtcExpression rhs) {
    super(lhs, rhs);
  }

  @Override
  public double getMeanTtc() {
    double lhsMean = getLhs().getMeanTtc();
    double rhsMean = getRhs().getMeanTtc();
    return Math.pow(lhsMean, rhsMean);
  }

  @Override
  public JsonObject toJson() {
    return this.toJson("exponentiation");
  }
}
