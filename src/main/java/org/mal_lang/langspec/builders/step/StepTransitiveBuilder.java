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
 * A builder for creating {@link org.mal_lang.langspec.step.StepTransitive} objects.
 *
 * @since 1.0.0
 */
public final class StepTransitiveBuilder extends StepExpressionBuilder {
  private final StepExpressionBuilder stepExpression;

  /**
   * Constructs a new {@code StepTransitiveBuilder} object.
   *
   * @param stepExpression the step expression of the transitive step
   * @throws java.lang.NullPointerException if {@code stepExpression} is {@code null}
   * @since 1.0.0
   */
  public StepTransitiveBuilder(StepExpressionBuilder stepExpression) {
    this.stepExpression = requireNonNull(stepExpression);
  }

  /**
   * Returns the step expression of this {@code StepTransitiveBuilder} object.
   *
   * @return the step expression of this {@code StepTransitiveBuilder} object
   * @since 1.0.0
   */
  public StepExpressionBuilder getStepExpression() {
    return this.stepExpression;
  }

  @Override
  public Asset getTarget(
      Asset sourceAsset, Map<String, Asset> assets, List<AssetBuilder> assetBuilders) {
    return sourceAsset;
  }

  /**
   * Creates a new {@code StepTransitiveBuilder} from a {@link jakarta.json.JsonObject}.
   *
   * @param jsonStepTransitive the {@link jakarta.json.JsonObject}
   * @return a new {@code StepTransitiveBuilder}
   * @throws java.lang.NullPointerException if {@code jsonStepTransitive} is {@code null}
   * @since 1.0.0
   */
  public static StepTransitiveBuilder fromJson(JsonObject jsonStepTransitive) {
    requireNonNull(jsonStepTransitive);
    var stepExpression =
        StepExpressionBuilder.fromJson(jsonStepTransitive.getJsonObject("stepExpression"));
    return new StepTransitiveBuilder(stepExpression);
  }
}
