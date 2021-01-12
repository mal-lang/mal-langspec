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

/** Immutable class representing a TTC addition of an attack step or a defense in a MAL language. */
public final class TtcAddition extends TtcBinaryOperation {
  /**
   * Constructs a new {@code TtcAddition} object.
   *
   * @param lhs the left-hand side of the new {@code TtcAddition} object
   * @param rhs the right-hand side of the new {@code TtcAddition} object
   * @throws NullPointerException if {@code lhs} or {@code rhs} is {@code null}
   */
  public TtcAddition(TtcExpression lhs, TtcExpression rhs) {
    super(TtcExpression.ADDITION, lhs, rhs);
  }

  @Override
  public double getMeanTtc() {
    double lhsMean = getLhs().getMeanTtc();
    double rhsMean = getRhs().getMeanTtc();
    return lhsMean + rhsMean;
  }
}
