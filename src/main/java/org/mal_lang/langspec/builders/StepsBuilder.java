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

package org.mal_lang.langspec.builders;

import static java.util.Objects.requireNonNull;

import jakarta.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import org.mal_lang.langspec.builders.step.StepExpressionBuilder;

/**
 * A builder for creating {@link org.mal_lang.langspec.Steps} objects.
 *
 * @since 1.0.0
 */
public final class StepsBuilder {
  private final boolean overrides;
  private final ArrayList<StepExpressionBuilder> stepExpressions = new ArrayList<>();

  /**
   * Constructs a new {@code StepsBuilder} object.
   *
   * @param overrides whether this {@code StepsBuilder} overrides
   * @since 1.0.0
   */
  public StepsBuilder(boolean overrides) {
    this.overrides = overrides;
  }

  /**
   * Returns whether this {@code StepsBuilder} object overrides.
   *
   * @return whether this {@code StepsBuilder} object overrides
   * @since 1.0.0
   */
  public boolean overrides() {
    return this.overrides;
  }

  /**
   * Returns a list of all step expressions in this {@code StepsBuilder} object.
   *
   * @return a list of all step expressions in this {@code StepsBuilder} object
   * @since 1.0.0
   */
  public List<StepExpressionBuilder> getStepExpressions() {
    return List.copyOf(this.stepExpressions);
  }

  /**
   * Adds a step expression to this {@code StepsBuilder} object.
   *
   * @param stepExpression the step expression to add
   * @return this {@code StepsBuilder} object
   * @throws java.lang.NullPointerException if {@code stepExpression} is {@code null}
   * @since 1.0.0
   */
  public StepsBuilder addStepExpression(StepExpressionBuilder stepExpression) {
    this.stepExpressions.add(requireNonNull(stepExpression));
    return this;
  }

  /**
   * Creates a new {@code StepsBuilder} from a {@link jakarta.json.JsonObject}.
   *
   * @param jsonSteps the {@link jakarta.json.JsonObject}
   * @return a new {@code StepsBuilder}
   * @throws java.lang.NullPointerException if {@code jsonSteps} is {@code null}
   * @since 1.0.0
   */
  public static StepsBuilder fromJson(JsonObject jsonSteps) {
    requireNonNull(jsonSteps);
    var builder = new StepsBuilder(jsonSteps.getBoolean("overrides"));
    for (var jsonStepExpression : jsonSteps.getJsonArray("stepExpressions")) {
      builder.addStepExpression(StepExpressionBuilder.fromJson(jsonStepExpression.asJsonObject()));
    }
    return builder;
  }
}
