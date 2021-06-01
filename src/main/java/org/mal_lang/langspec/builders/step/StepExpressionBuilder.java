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

import jakarta.json.JsonObject;
import java.util.List;
import java.util.Map;
import org.mal_lang.langspec.Asset;
import org.mal_lang.langspec.builders.AssetBuilder;

/**
 * A builder for creating {@link org.mal_lang.langspec.step.StepExpression} objects.
 *
 * @since 1.0.0
 */
public abstract class StepExpressionBuilder {
  /**
   * Constructs a new {@code StepExpressionBuilder} object.
   *
   * @since 1.0.0
   */
  protected StepExpressionBuilder() {}

  /**
   * Returns the target asset of this {@code StepExpressionBuilder}.
   *
   * @param sourceAsset the source asset
   * @param assets all assets of the language
   * @param assetBuilders all asset builders of the language
   * @return the target asset of this {@code StepExpressionBuilder}
   * @since 1.0.0
   */
  public abstract Asset getTarget(
      Asset sourceAsset, Map<String, Asset> assets, List<AssetBuilder> assetBuilders);

  /**
   * Creates a new {@code StepExpressionBuilder} from a {@link jakarta.json.JsonObject}.
   *
   * @param jsonStepExpression the {@link jakarta.json.JsonObject}
   * @return a new {@code StepExpressionBuilder}
   * @throws java.lang.NullPointerException if {@code jsonStepExpression} is {@code null}
   * @since 1.0.0
   */
  public static StepExpressionBuilder fromJson(JsonObject jsonStepExpression) {
    requireNonNull(jsonStepExpression);
    switch (jsonStepExpression.getString("type")) {
      case "union":
      case "intersection":
      case "difference":
      case "collect":
        return StepBinaryOperationBuilder.fromJson(jsonStepExpression);
      case "transitive":
        return StepTransitiveBuilder.fromJson(jsonStepExpression);
      case "subType":
        return StepSubTypeBuilder.fromJson(jsonStepExpression);
      case "field":
      case "attackStep":
      case "variable":
        return StepReferenceBuilder.fromJson(jsonStepExpression);
      default:
        throw new RuntimeException(
            String.format(
                "Invalid step expression type \"%s\"", jsonStepExpression.getString("type")));
    }
  }
}
