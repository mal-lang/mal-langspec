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

package org.mal_lang.langspec.builders.step;

import static java.util.Objects.requireNonNull;

import jakarta.json.JsonObject;

/**
 * A builder for creating {@link org.mal_lang.langspec.step.StepBinaryOperation} objects.
 *
 * @since 1.0.0
 */
public abstract class StepBinaryOperationBuilder extends StepExpressionBuilder {
  private final StepExpressionBuilder lhs;
  private final StepExpressionBuilder rhs;

  /**
   * Constructs a new {@code StepBinaryOperationBuilder} object.
   *
   * @param lhs the left-hand side of the binary operation
   * @param rhs the right-hand side of the binary operation
   * @throws java.lang.NullPointerException if {@code lhs} or {@code rhs} is {@code null}
   * @since 1.0.0
   */
  protected StepBinaryOperationBuilder(StepExpressionBuilder lhs, StepExpressionBuilder rhs) {
    this.lhs = requireNonNull(lhs);
    this.rhs = requireNonNull(rhs);
  }

  /**
   * Returns the left-hand side of this {@code StepBinaryOperationBuilder} object.
   *
   * @return the left-hand side of this {@code StepBinaryOperationBuilder} object
   * @since 1.0.0
   */
  public StepExpressionBuilder getLhs() {
    return this.lhs;
  }

  /**
   * Returns the right-hand side of this {@code StepBinaryOperationBuilder} object.
   *
   * @return the right-hand side of this {@code StepBinaryOperationBuilder} object
   * @since 1.0.0
   */
  public StepExpressionBuilder getRhs() {
    return this.rhs;
  }

  /**
   * Creates a new {@code StepBinaryOperationBuilder} from a {@link jakarta.json.JsonObject}.
   *
   * @param jsonStepBinaryOperation the {@link jakarta.json.JsonObject}
   * @return a new {@code StepBinaryOperationBuilder}
   * @throws java.lang.NullPointerException if {@code jsonStepBinaryOperation} is {@code null}
   * @since 1.0.0
   */
  public static StepBinaryOperationBuilder fromJson(JsonObject jsonStepBinaryOperation) {
    requireNonNull(jsonStepBinaryOperation);
    var lhs = StepExpressionBuilder.fromJson(jsonStepBinaryOperation.getJsonObject("lhs"));
    var rhs = StepExpressionBuilder.fromJson(jsonStepBinaryOperation.getJsonObject("rhs"));
    switch (jsonStepBinaryOperation.getString("type")) {
      case "union":
        return new StepUnionBuilder(lhs, rhs);
      case "intersection":
        return new StepIntersectionBuilder(lhs, rhs);
      case "difference":
        return new StepDifferenceBuilder(lhs, rhs);
      case "collect":
        return new StepCollectBuilder(lhs, rhs);
      default:
        throw new RuntimeException(
            String.format(
                "Invalid step expression type \"%s\"", jsonStepBinaryOperation.getString("type")));
    }
  }
}
