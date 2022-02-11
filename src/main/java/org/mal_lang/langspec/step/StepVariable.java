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

package org.mal_lang.langspec.step;

import static java.util.Objects.requireNonNull;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import java.util.Map;
import org.mal_lang.langspec.Asset;
import org.mal_lang.langspec.Variable;
import org.mal_lang.langspec.builders.step.StepVariableBuilder;

/**
 * Immutable class representing a variable step in a MAL language.
 *
 * @since 1.0.0
 */
public final class StepVariable extends StepReference {
  private final Variable variable;

  private StepVariable(Asset sourceAsset, Asset targetAsset, Variable variable) {
    super(sourceAsset, targetAsset);
    this.variable = requireNonNull(variable);
  }

  /**
   * Returns the variable of this {@code StepVariable} object.
   *
   * @return the variable of this {@code StepVariable} object
   * @since 1.0.0
   */
  public Variable getVariable() {
    return this.variable;
  }

  @Override
  public JsonObject toJson() {
    return Json.createObjectBuilder()
        .add("type", "variable")
        .add("name", this.variable.getName())
        .build();
  }

  static StepVariable fromBuilder(
      StepVariableBuilder builder,
      Asset sourceAsset,
      Map<String, Asset> assets,
      Map<Variable, Asset> variableTargets) {
    requireNonNull(builder);
    requireNonNull(sourceAsset);
    requireNonNull(assets);
    requireNonNull(variableTargets);
    if (!sourceAsset.hasVariable(builder.getName())) {
      throw new IllegalArgumentException(
          String.format("Variable \"%s\" not found", builder.getName()));
    }
    var variable = sourceAsset.getVariable(builder.getName());
    var targetAsset = variableTargets.get(variable);
    return new StepVariable(sourceAsset, targetAsset, variable);
  }
}
