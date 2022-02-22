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
import org.mal_lang.langspec.builders.step.StepSubTypeBuilder;

/**
 * Immutable class representing a sub-type step in a MAL language.
 *
 * @since 1.0.0
 */
public final class StepSubType extends StepExpression {
  private final Asset subType;
  private final StepExpression stepExpression;

  private StepSubType(
      Asset sourceType, Asset targetType, Asset subType, StepExpression stepExpression) {
    super(sourceType, targetType);
    this.subType = requireNonNull(subType);
    this.stepExpression = requireNonNull(stepExpression);
  }

  /**
   * Returns the sub-type of this {@code StepSubType} object.
   *
   * @return the sub-type of this {@code StepSubType} object
   * @since 1.0.0
   */
  public Asset getSubType() {
    return this.subType;
  }

  /**
   * Returns the step expression of this {@code StepSubType} object.
   *
   * @return the step expression of this {@code StepSubType} object
   * @since 1.0.0
   */
  public StepExpression getStepExpression() {
    return this.stepExpression;
  }

  @Override
  public JsonObject toJson() {
    return Json.createObjectBuilder()
        .add("type", "subType")
        .add("subType", this.subType.getName())
        .add("stepExpression", this.stepExpression.toJson())
        .build();
  }

  static StepSubType fromBuilder(
      StepSubTypeBuilder builder,
      Asset sourceAsset,
      Map<String, Asset> assets,
      Map<Variable, Asset> variableTargets) {
    requireNonNull(builder);
    requireNonNull(sourceAsset);
    requireNonNull(assets);
    requireNonNull(variableTargets);
    if (!assets.containsKey(builder.getSubType())) {
      throw new IllegalArgumentException(
          String.format("Asset \"%s\" not found", builder.getSubType()));
    }
    var subType = assets.get(builder.getSubType());
    var stepExpression =
        StepExpression.fromBuilder(
            builder.getStepExpression(), sourceAsset, assets, variableTargets);
    return new StepSubType(sourceAsset, subType, subType, stepExpression);
  }
}
