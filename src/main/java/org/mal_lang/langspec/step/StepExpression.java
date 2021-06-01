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

import jakarta.json.JsonObject;
import java.util.Map;
import org.mal_lang.langspec.Asset;
import org.mal_lang.langspec.Variable;
import org.mal_lang.langspec.builders.step.StepAttackStepBuilder;
import org.mal_lang.langspec.builders.step.StepCollectBuilder;
import org.mal_lang.langspec.builders.step.StepDifferenceBuilder;
import org.mal_lang.langspec.builders.step.StepExpressionBuilder;
import org.mal_lang.langspec.builders.step.StepFieldBuilder;
import org.mal_lang.langspec.builders.step.StepIntersectionBuilder;
import org.mal_lang.langspec.builders.step.StepSubTypeBuilder;
import org.mal_lang.langspec.builders.step.StepTransitiveBuilder;
import org.mal_lang.langspec.builders.step.StepUnionBuilder;
import org.mal_lang.langspec.builders.step.StepVariableBuilder;

/**
 * Immutable class representing a step expression in a MAL language.
 *
 * @since 1.0.0
 */
public abstract class StepExpression {
  private final Asset sourceAsset;
  private final Asset targetAsset;

  StepExpression(Asset sourceAsset, Asset targetAsset) {
    this.sourceAsset = requireNonNull(sourceAsset);
    this.targetAsset = requireNonNull(targetAsset);
  }

  /**
   * Returns the source asset of this {@code StepExpression} object.
   *
   * @return the source asset of this {@code StepExpression} object
   * @since 1.0.0
   */
  public Asset getSourceAsset() {
    return this.sourceAsset;
  }

  /**
   * Returns the target asset of this {@code StepExpression} object.
   *
   * @return the target asset of this {@code StepExpression} object
   * @since 1.0.0
   */
  public Asset getTargetAsset() {
    return this.targetAsset;
  }

  /**
   * Returns the JSON representation of this {@code StepExpression} object.
   *
   * @return the JSON representation of this {@code StepExpression} object
   * @since 1.0.0
   */
  public abstract JsonObject toJson();

  /**
   * Creates a new {@code StepExpression} object from a {@link
   * org.mal_lang.langspec.builders.step.StepExpressionBuilder}.
   *
   * @param builder the {@link org.mal_lang.langspec.builders.step.StepExpressionBuilder}
   * @param sourceAsset the source asset of the step expression
   * @param assets all assets of the language
   * @param variableTargets a map of target assets of variables
   * @return a new {@code StepExpression} object
   * @throws java.lang.NullPointerException if {@code builder}, {@code sourceAsset}, or {@code
   *     assets} is {@code null}
   * @since 1.0.0
   */
  public static StepExpression fromBuilder(
      StepExpressionBuilder builder,
      Asset sourceAsset,
      Map<String, Asset> assets,
      Map<Variable, Asset> variableTargets) {
    requireNonNull(builder);
    requireNonNull(sourceAsset);
    requireNonNull(assets);
    requireNonNull(variableTargets);
    if (builder instanceof StepUnionBuilder) {
      return StepUnion.fromBuilder(
          (StepUnionBuilder) builder, sourceAsset, assets, variableTargets);
    }
    if (builder instanceof StepIntersectionBuilder) {
      return StepIntersection.fromBuilder(
          (StepIntersectionBuilder) builder, sourceAsset, assets, variableTargets);
    }
    if (builder instanceof StepDifferenceBuilder) {
      return StepDifference.fromBuilder(
          (StepDifferenceBuilder) builder, sourceAsset, assets, variableTargets);
    }
    if (builder instanceof StepCollectBuilder) {
      return StepCollect.fromBuilder(
          (StepCollectBuilder) builder, sourceAsset, assets, variableTargets);
    }
    if (builder instanceof StepTransitiveBuilder) {
      return StepTransitive.fromBuilder(
          (StepTransitiveBuilder) builder, sourceAsset, assets, variableTargets);
    }
    if (builder instanceof StepSubTypeBuilder) {
      return StepSubType.fromBuilder(
          (StepSubTypeBuilder) builder, sourceAsset, assets, variableTargets);
    }
    if (builder instanceof StepFieldBuilder) {
      return StepField.fromBuilder(
          (StepFieldBuilder) builder, sourceAsset, assets, variableTargets);
    }
    if (builder instanceof StepAttackStepBuilder) {
      return StepAttackStep.fromBuilder(
          (StepAttackStepBuilder) builder, sourceAsset, assets, variableTargets);
    }
    if (builder instanceof StepVariableBuilder) {
      return StepVariable.fromBuilder(
          (StepVariableBuilder) builder, sourceAsset, assets, variableTargets);
    }
    throw new RuntimeException(
        String.format(
            "Invalid %s sub-type %s",
            StepExpressionBuilder.class.getName(), builder.getClass().getName()));
  }
}
