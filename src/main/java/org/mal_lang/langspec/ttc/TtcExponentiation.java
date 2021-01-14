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

/**
 * Immutable class representing a TTC exponentiation of an attack step or a defense in a MAL
 * language.
 */
public final class TtcExponentiation extends TtcBinaryOperation {
  /**
   * Constructs a new {@code TtcExponentiation} object.
   *
   * @param lhs the left-hand side of the new {@code TtcExponentiation} object
   * @param rhs the right-hand side of the new {@code TtcExponentiation} object
   * @throws NullPointerException if {@code lhs} or {@code rhs} is {@code null}
   */
  public TtcExponentiation(TtcExpression lhs, TtcExpression rhs) {
    super(TtcExpression.EXPONENTIATION, lhs, rhs);
  }

  @Override
  public double getMeanTtc() {
    double lhsMean = getLhs().getMeanTtc();
    double rhsMean = getRhs().getMeanTtc();
    return Math.pow(lhsMean, rhsMean);
  }
}