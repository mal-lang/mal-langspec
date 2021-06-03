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
import java.util.List;
import java.util.Map;
import org.mal_lang.langspec.Asset;
import org.mal_lang.langspec.builders.AssetBuilder;

/**
 * A builder for creating {@link org.mal_lang.langspec.step.StepSubType} objects.
 *
 * @since 1.0.0
 */
public final class StepSubTypeBuilder extends StepExpressionBuilder {
  private final String subType;
  private final StepExpressionBuilder stepExpression;

  /**
   * Constructs a new {@code StepSubTypeBuilder} object.
   *
   * @param subType the sub-type of the sub-type step
   * @param stepExpression the step expression of the sub-type step
   * @throws java.lang.NullPointerException if {@code subType} or {@code stepExpression} is {@code
   *     null}
   * @throws java.lang.IllegalArgumentException if {@code subType} is not a valid identifier
   * @since 1.0.0
   */
  public StepSubTypeBuilder(String subType, StepExpressionBuilder stepExpression) {
    this.subType = requireIdentifier(subType);
    this.stepExpression = requireNonNull(stepExpression);
  }

  /**
   * Returns the sub-type of this {@code StepSubTypeBuilder} object.
   *
   * @return the sub-type of this {@code StepSubTypeBuilder} object
   * @since 1.0.0
   */
  public String getSubType() {
    return this.subType;
  }

  /**
   * Returns the step expression of this {@code StepSubTypeBuilder} object.
   *
   * @return the step expression of this {@code StepSubTypeBuilder} object
   * @since 1.0.0
   */
  public StepExpressionBuilder getStepExpression() {
    return this.stepExpression;
  }

  @Override
  public Asset getTarget(
      Asset sourceAsset, Map<String, Asset> assets, List<AssetBuilder> assetBuilders) {
    if (!assets.containsKey(this.subType)) {
      throw new IllegalArgumentException(String.format("Asset %s not found", this.subType));
    }
    return assets.get(this.subType);
  }

  /**
   * Creates a new {@code StepSubTypeBuilder} from a {@link jakarta.json.JsonObject}.
   *
   * @param jsonStepSubType the {@link jakarta.json.JsonObject}
   * @return a new {@code StepSubTypeBuilder}
   * @throws java.lang.NullPointerException if {@code jsonStepSubType} is {@code null}
   * @since 1.0.0
   */
  public static StepSubTypeBuilder fromJson(JsonObject jsonStepSubType) {
    requireNonNull(jsonStepSubType);
    var subType = jsonStepSubType.getString("subType");
    var stepExpression =
        StepExpressionBuilder.fromJson(jsonStepSubType.getJsonObject("stepExpression"));
    return new StepSubTypeBuilder(subType, stepExpression);
  }
}
