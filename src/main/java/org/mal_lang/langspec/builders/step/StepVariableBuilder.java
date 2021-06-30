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

import java.util.List;
import java.util.Map;
import org.mal_lang.langspec.Asset;
import org.mal_lang.langspec.builders.AssetBuilder;
import org.mal_lang.langspec.builders.VariableBuilder;

/**
 * A builder for creating {@link org.mal_lang.langspec.step.StepVariable} objects.
 *
 * @since 1.0.0
 */
public final class StepVariableBuilder extends StepReferenceBuilder {
  /**
   * Constructs a new {@code StepVariableBuilder} object.
   *
   * @param name the name of the variable
   * @throws java.lang.NullPointerException if {@code name} is {@code null}
   * @throws java.lang.IllegalArgumentException if {@code name} is not a valid identifier
   * @since 1.0.0
   */
  public StepVariableBuilder(String name) {
    super(name);
  }

  private static AssetBuilder getAssetBuilder(String assetName, List<AssetBuilder> assetBuilders) {
    for (var assetBuilder : assetBuilders) {
      if (assetBuilder.getName().equals(assetName)) {
        return assetBuilder;
      }
    }
    throw new IllegalStateException();
  }

  private static VariableBuilder getVariableBuilder(
      String assetName, String variableName, List<AssetBuilder> assetBuilders) {
    var assetBuilder = StepVariableBuilder.getAssetBuilder(assetName, assetBuilders);
    for (var variableBuilder : assetBuilder.getVariables()) {
      if (variableBuilder.getName().equals(variableName)) {
        return variableBuilder;
      }
    }
    var superAsset = assetBuilder.getSuperAsset();
    if (superAsset != null) {
      return StepVariableBuilder.getVariableBuilder(superAsset, variableName, assetBuilders);
    }
    throw new IllegalStateException();
  }

  @Override
  public Asset getTarget(
      Asset sourceAsset, Map<String, Asset> assets, List<AssetBuilder> assetBuilders) {
    if (!sourceAsset.hasVariable(this.getName())) {
      throw new IllegalArgumentException(
          String.format("Variable %s.%s not found", sourceAsset.getName(), this.getName()));
    }
    return StepVariableBuilder.getVariableBuilder(
            sourceAsset.getName(), this.getName(), assetBuilders)
        .getStepExpression()
        .getTarget(sourceAsset, assets, assetBuilders);
  }
}
