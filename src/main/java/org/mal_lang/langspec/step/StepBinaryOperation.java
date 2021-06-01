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

package org.mal_lang.langspec.step;

import static java.util.Objects.requireNonNull;

import org.mal_lang.langspec.Asset;

/**
 * Immutable class representing a binary operation step in a MAL language.
 *
 * @since 1.0.0
 */
public abstract class StepBinaryOperation extends StepExpression {
  private final StepExpression lhs;
  private final StepExpression rhs;

  StepBinaryOperation(
      Asset sourceAsset, Asset targetAsset, StepExpression lhs, StepExpression rhs) {
    super(sourceAsset, targetAsset);
    this.lhs = requireNonNull(lhs);
    this.rhs = requireNonNull(rhs);
  }

  /**
   * Returns the left-hand side of this {@code StepBinaryOperation} object.
   *
   * @return the left-hand side of this {@code StepBinaryOperation} object
   * @since 1.0.0
   */
  public StepExpression getLhs() {
    return this.lhs;
  }

  /**
   * Returns the right-hand side of this {@code StepBinaryOperation} object.
   *
   * @return the right-hand side of this {@code StepBinaryOperation} object
   * @since 1.0.0
   */
  public StepExpression getRhs() {
    return this.rhs;
  }
}
