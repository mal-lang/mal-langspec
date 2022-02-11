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

package org.mal_lang.langspec.builders;

import static java.util.Objects.requireNonNull;
import static org.mal_lang.langspec.Utils.requireIdentifier;

import jakarta.json.JsonObject;
import org.mal_lang.langspec.builders.step.StepExpressionBuilder;

/**
 * A builder for creating {@link org.mal_lang.langspec.Variable} objects.
 *
 * @since 1.0.0
 */
public final class VariableBuilder {
  private final String name;
  private final StepExpressionBuilder stepExpression;

  /**
   * Constructs a new {@code VariableBuilder} object.
   *
   * @param name the name of the variable
   * @param stepExpression the step expression of the variable
   * @throws java.lang.NullPointerException if {@code name} or {@code stepExpression} is {@code
   *     null}
   * @throws java.lang.IllegalArgumentException if {@code name} is not a valid identifier
   * @since 1.0.0
   */
  public VariableBuilder(String name, StepExpressionBuilder stepExpression) {
    this.name = requireIdentifier(name);
    this.stepExpression = requireNonNull(stepExpression);
  }

  /**
   * Returns the name of this {@code VariableBuilder} object.
   *
   * @return the name of this {@code VariableBuilder} object
   * @since 1.0.0
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the step expression of this {@code VariableBuilder} object.
   *
   * @return the step expression of this {@code VariableBuilder} object
   * @since 1.0.0
   */
  public StepExpressionBuilder getStepExpression() {
    return this.stepExpression;
  }

  /**
   * Creates a new {@code VariableBuilder} from a {@link jakarta.json.JsonObject}.
   *
   * @param jsonVariable the {@link jakarta.json.JsonObject}
   * @return a new {@code VariableBuilder} from a {@link jakarta.json.JsonObject}
   * @throws java.lang.NullPointerException if {@code jsonVariable} is {@code null}
   * @since 1.0.0
   */
  public static VariableBuilder fromJson(JsonObject jsonVariable) {
    requireNonNull(jsonVariable);
    return new VariableBuilder(
        jsonVariable.getString("name"),
        StepExpressionBuilder.fromJson(jsonVariable.getJsonObject("stepExpression")));
  }
}
