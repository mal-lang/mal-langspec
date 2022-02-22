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

package org.mal_lang.langspec;

import static java.util.Objects.requireNonNull;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import org.mal_lang.langspec.builders.StepsBuilder;
import org.mal_lang.langspec.step.StepExpression;

/**
 * Immutable class representing steps of an attack step in a MAL language.
 *
 * @since 1.0.0
 */
public final class Steps {
  private final boolean overrides;
  private final List<StepExpression> stepExpressions = new ArrayList<>();

  private Steps(boolean overrides) {
    this.overrides = overrides;
  }

  /**
   * Returns whether this {@code Steps} object overrides.
   *
   * @return whether this {@code Steps} object overrides
   * @since 1.0.0
   */
  public boolean overrides() {
    return this.overrides;
  }

  /**
   * Returns a list of all step expressions in this {@code Steps} object.
   *
   * @return a list of all step expressions in this {@code Steps} object
   * @since 1.0.0
   */
  public List<StepExpression> getStepExpressions() {
    return List.copyOf(this.stepExpressions);
  }

  void addStepExpression(StepExpression stepExpression) {
    this.stepExpressions.add(requireNonNull(stepExpression));
  }

  JsonObject toJson() {
    var jsonStepExpressions = Json.createArrayBuilder();
    for (var stepExpression : this.stepExpressions) {
      jsonStepExpressions.add(stepExpression.toJson());
    }
    return Json.createObjectBuilder()
        .add("overrides", this.overrides)
        .add("stepExpressions", jsonStepExpressions)
        .build();
  }

  static Steps fromBuilder(StepsBuilder builder) {
    requireNonNull(builder);
    return new Steps(builder.overrides());
  }
}
