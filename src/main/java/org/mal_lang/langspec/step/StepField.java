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

import jakarta.json.Json;
import jakarta.json.JsonObject;
import java.util.Map;
import org.mal_lang.langspec.Asset;
import org.mal_lang.langspec.Field;
import org.mal_lang.langspec.Variable;
import org.mal_lang.langspec.builders.step.StepFieldBuilder;

/**
 * Immutable class representing a field step in a MAL language.
 *
 * @since 1.0.0
 */
public final class StepField extends StepReference {
  private final Field field;

  private StepField(Asset sourceAsset, Asset targetAsset, Field field) {
    super(sourceAsset, targetAsset);
    this.field = requireNonNull(field);
  }

  /**
   * Returns the field of this {@code StepField} object.
   *
   * @return the field of this {@code StepField} object
   * @since 1.0.0
   */
  public Field getField() {
    return this.field;
  }

  @Override
  public JsonObject toJson() {
    return Json.createObjectBuilder()
        .add("type", "field")
        .add("name", this.field.getName())
        .build();
  }

  static StepField fromBuilder(
      StepFieldBuilder builder,
      Asset sourceAsset,
      Map<String, Asset> assets,
      Map<Variable, Asset> variableTargets) {
    requireNonNull(builder);
    requireNonNull(sourceAsset);
    requireNonNull(assets);
    requireNonNull(variableTargets);
    if (!sourceAsset.hasField(builder.getName())) {
      throw new IllegalArgumentException(
          String.format("Field \"%s\" not found", builder.getName()));
    }
    var field = sourceAsset.getField(builder.getName());
    return new StepField(sourceAsset, field.getTarget().getAsset(), field);
  }
}
