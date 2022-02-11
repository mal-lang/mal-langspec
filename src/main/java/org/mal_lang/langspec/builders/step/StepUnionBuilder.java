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

package org.mal_lang.langspec.builders.step;

import java.util.List;
import java.util.Map;
import org.mal_lang.langspec.Asset;
import org.mal_lang.langspec.builders.AssetBuilder;

/**
 * A builder for creating {@link org.mal_lang.langspec.step.StepUnion} objects.
 *
 * @since 1.0.0
 */
public final class StepUnionBuilder extends StepBinaryOperationBuilder {
  /**
   * Constructs a new {@code StepUnionBuilder} object.
   *
   * @param lhs the left-hand side of the union
   * @param rhs the right-hand side of the union
   * @throws java.lang.NullPointerException if {@code lhs} or {@code rhs} is {@code null}
   * @since 1.0.0
   */
  public StepUnionBuilder(StepExpressionBuilder lhs, StepExpressionBuilder rhs) {
    super(lhs, rhs);
  }

  @Override
  public Asset getTarget(
      Asset sourceAsset, Map<String, Asset> assets, List<AssetBuilder> assetBuilders) {
    return Asset.leastUpperBound(
        this.getLhs().getTarget(sourceAsset, assets, assetBuilders),
        this.getRhs().getTarget(sourceAsset, assets, assetBuilders));
  }
}
