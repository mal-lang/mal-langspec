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
import org.mal_lang.langspec.Variable;
import org.mal_lang.langspec.builders.step.StepUnionBuilder;

/**
 * Immutable class representing a union step in a MAL language.
 *
 * @since 1.0.0
 */
public final class StepUnion extends StepBinaryOperation {
  private StepUnion(Asset sourceAsset, Asset targetAsset, StepExpression lhs, StepExpression rhs) {
    super(sourceAsset, targetAsset, lhs, rhs);
  }

  @Override
  public JsonObject toJson() {
    return Json.createObjectBuilder()
        .add("type", "union")
        .add("lhs", this.getLhs().toJson())
        .add("rhs", this.getRhs().toJson())
        .build();
  }

  static StepUnion fromBuilder(
      StepUnionBuilder builder,
      Asset sourceAsset,
      Map<String, Asset> assets,
      Map<Variable, Asset> variableTargets) {
    requireNonNull(builder);
    requireNonNull(sourceAsset);
    requireNonNull(assets);
    requireNonNull(variableTargets);
    var lhs = StepExpression.fromBuilder(builder.getLhs(), sourceAsset, assets, variableTargets);
    var rhs = StepExpression.fromBuilder(builder.getRhs(), sourceAsset, assets, variableTargets);
    var targetAsset = Asset.leastUpperBound(lhs.getTargetAsset(), rhs.getTargetAsset());
    return new StepUnion(sourceAsset, targetAsset, lhs, rhs);
  }
}
