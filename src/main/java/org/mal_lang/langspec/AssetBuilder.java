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

package org.mal_lang.langspec;

import static org.mal_lang.langspec.Utils.checkNotNull;

import jakarta.json.JsonObject;
import java.util.LinkedHashMap;
import java.util.Map;

/** A builder for creating {@link Asset} objects. */
public final class AssetBuilder {
  private final String name;
  private MetaBuilder meta = Meta.builder();
  private final String category;
  private boolean isAbstract = false;
  private String superAsset = null;
  private final Map<String, VariableBuilder> variables = new LinkedHashMap<>();
  private final Map<String, AttackStepBuilder> attackSteps = new LinkedHashMap<>();

  AssetBuilder(String name, String category) {
    this.name = name;
    this.category = category;
  }

  /**
   * Returns the name of this {@code AssetBuilder} object.
   *
   * @return the name of this {@code AssetBuilder} object
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the meta info of this {@code AssetBuilder} object.
   *
   * @return the meta info of this {@code AssetBuilder} object
   */
  public MetaBuilder getMeta() {
    return meta;
  }

  /**
   * Sets the meta info of this {@code AssetBuilder} object.
   *
   * @param meta the meta info to set
   * @return this {@code AssetBuilder} object
   * @throws NullPointerException if {@code meta} is {@code null}
   */
  public AssetBuilder setMeta(MetaBuilder meta) {
    checkNotNull(meta);
    this.meta = meta;
    return this;
  }

  /**
   * Returns the category of this {@code AssetBuilder} object.
   *
   * @return the category of this {@code AssetBuilder} object
   */
  public String getCategory() {
    return category;
  }

  /**
   * Returns whether this {@code AssetBuilder} object is abstract.
   *
   * @return whether this {@code AssetBuilder} object is abstract
   */
  public boolean isAbstract() {
    return isAbstract;
  }

  /**
   * Sets whether this {@code AssetBuilder} object is abstract.
   *
   * @param isAbstract whether this {@code AssetBuilder} object is abstract
   * @return this {@code AssetBuilder} object
   */
  public AssetBuilder setAbstract(boolean isAbstract) {
    this.isAbstract = isAbstract;
    return this;
  }

  /**
   * Returns the super asset of this {@code AssetBuilder} object, or {@code null}.
   *
   * @return the super asset of this {@code AssetBuilder} object, or {@code null}
   */
  public String getSuperAsset() {
    return superAsset;
  }

  /**
   * Sets the super asset of this {@code AssetBuilder} object.
   *
   * @param superAsset the super asset to set, or {@code null}
   * @return this {@code AssetBuilder} object
   */
  public AssetBuilder setSuperAsset(String superAsset) {
    this.superAsset = superAsset;
    return this;
  }

  /**
   * Adds a variable to this {@code AssetBuilder} object.
   *
   * @param variable the variable to add
   * @return this {@code AssetBuilder} object
   * @throws NullPointerException if {@code variable} is {@code null}
   */
  public AssetBuilder addVariable(VariableBuilder variable) {
    checkNotNull(variable);
    variables.put(variable.getName(), variable);
    return this;
  }

  /**
   * Adds an attack step to this {@code AssetBuilder} object.
   *
   * @param attackStep the attack step to add
   * @return this {@code AssetBuilder} object
   * @throws NullPointerException if {@code attackStep} is {@code null}
   */
  public AssetBuilder addAttackStep(AttackStepBuilder attackStep) {
    checkNotNull(attackStep);
    attackSteps.put(attackStep.getName(), attackStep);
    return this;
  }

  /**
   * Creates a new {@link Asset} object.
   *
   * @param categories a map of all categories in the language
   * @return a new {@link Asset} object
   * @throws NullPointerException if {@code categories} is {@code null}
   * @throws IllegalArgumentException if {@code categories} does not contain the category of this
   *     {@code AssetBuilder} object
   */
  public Asset build(Map<String, Category> categories) {
    checkNotNull(categories);
    if (!categories.containsKey(category)) {
      throw new IllegalArgumentException(String.format("Category \"%s\" not found", category));
    }
    return new Asset(name, meta.build(), categories.get(category), isAbstract);
  }

  /**
   * Connects the super asset to a built {@link Asset} object.
   *
   * @param assets a map of all assets in the language
   * @throws NullPointerException if {@code assets} is {@code null}
   * @throws IllegalArgumentException if {@code assets} does not contain this asset or the super
   *     asset of this {@code AssetBuilder} object
   */
  public void connectSuperAsset(Map<String, Asset> assets) {
    checkNotNull(assets);
    if (!assets.containsKey(name)) {
      throw new IllegalArgumentException(String.format("Asset \"%s\" not found", name));
    }
    if (superAsset != null) {
      if (!assets.containsKey(superAsset)) {
        throw new IllegalArgumentException(String.format("Asset \"%s\" not found", superAsset));
      }
      assets.get(name).setSuperAsset(assets.get(superAsset));
    }
  }

  /**
   * Builds the variables of this {@code AssetBuilder} object.
   *
   * @param asset the built asset
   * @throws NullPointerException if {@code asset} is {@code null}
   */
  public void buildVariables(Asset asset) {
    checkNotNull(asset);
    for (var variable : variables.values()) {
      variable.build(asset);
    }
  }

  /**
   * Builds the attack steps of this {@code AssetBuilder} object.
   *
   * @param asset the built asset
   * @throws NullPointerException if {@code asset} is {@code null}
   */
  public void buildAttackSteps(Asset asset) {
    checkNotNull(asset);
    for (var attackStep : attackSteps.values()) {
      attackStep.build(asset);
    }
  }

  static AssetBuilder fromJson(JsonObject jsonAsset) {
    var assetBuilder =
        Asset.builder(jsonAsset.getString("name"), jsonAsset.getString("category"))
            .setMeta(MetaBuilder.fromJson(jsonAsset.getJsonObject("meta")))
            .setAbstract(jsonAsset.getBoolean("isAbstract"))
            .setSuperAsset(
                jsonAsset.isNull("superAsset") ? null : jsonAsset.getString("superAsset"));

    var jsonVariables = jsonAsset.getJsonArray("variables");
    for (int i = 0; i < jsonVariables.size(); i++) {
      var jsonVariable = jsonVariables.getJsonObject(i);
      var variableBuilder = VariableBuilder.fromJson(jsonVariable);
      assetBuilder.addVariable(variableBuilder);
    }

    var jsonAttackSteps = jsonAsset.getJsonArray("attackSteps");
    for (int i = 0; i < jsonAttackSteps.size(); i++) {
      var jsonAttackStep = jsonAttackSteps.getJsonObject(i);
      var attackStepBuilder = AttackStepBuilder.fromJson(jsonAttackStep);
      assetBuilder.addAttackStep(attackStepBuilder);
    }

    return assetBuilder;
  }
}
