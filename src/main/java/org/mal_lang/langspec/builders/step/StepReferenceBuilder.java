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

package org.mal_lang.langspec.builders.step;

import static java.util.Objects.requireNonNull;
import static org.mal_lang.langspec.Utils.requireIdentifier;

import jakarta.json.JsonObject;

/**
 * A builder for creating {@link org.mal_lang.langspec.step.StepReference} objects.
 *
 * @since 1.0.0
 */
public abstract class StepReferenceBuilder extends StepExpressionBuilder {
  private final String name;

  /**
   * Constructs a new {@code StepReferenceBuilder} object.
   *
   * @param name the name of the reference step
   * @throws java.lang.NullPointerException if {@code name} is {@code null}
   * @throws java.lang.IllegalArgumentException if {@code name} is not a valid identifier
   * @since 1.0.0
   */
  protected StepReferenceBuilder(String name) {
    this.name = requireIdentifier(name);
  }

  /**
   * Returns the name of this {@code StepReferenceBuilder} object.
   *
   * @return the name of this {@code StepReferenceBuilder} object
   * @since 1.0.0
   */
  public String getName() {
    return this.name;
  }

  /**
   * Creates a new {@code StepReferenceBuilder} from a {@link jakarta.json.JsonObject}.
   *
   * @param jsonStepReference the {@link jakarta.json.JsonObject}
   * @return a new {@code StepReferenceBuilder}
   * @throws java.lang.NullPointerException if {@code jsonStepReference} is {@code null}
   * @since 1.0.0
   */
  public static StepReferenceBuilder fromJson(JsonObject jsonStepReference) {
    requireNonNull(jsonStepReference);
    var name = jsonStepReference.getString("name");
    switch (jsonStepReference.getString("type")) {
      case "field":
        return new StepFieldBuilder(name);
      case "attackStep":
        return new StepAttackStepBuilder(name);
      case "variable":
        return new StepVariableBuilder(name);
      default:
        throw new RuntimeException(
            String.format(
                "Invalid step expression type \"%s\"", jsonStepReference.getString("type")));
    }
  }
}
