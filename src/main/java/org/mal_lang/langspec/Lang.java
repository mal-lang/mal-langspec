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

package org.mal_lang.langspec;

import static java.util.Objects.requireNonNull;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.mal_lang.langspec.builders.LangBuilder;
import org.mal_lang.langspec.step.StepExpression;

/**
 * Immutable class representing a MAL language.
 *
 * @since 1.0.0
 */
public final class Lang {
  private final Map<String, String> defines;
  private final Map<String, Category> categories;
  private final Map<String, Asset> assets;
  private final List<Association> associations;
  private final String license;
  private final String notice;

  private Lang(
      Map<String, String> defines,
      Map<String, Category> categories,
      Map<String, Asset> assets,
      List<Association> associations,
      String license,
      String notice) {
    this.defines = Map.copyOf(requireNonNull(defines));
    this.categories = new LinkedHashMap<>(requireNonNull(categories));
    this.assets = new LinkedHashMap<>(requireNonNull(assets));
    this.associations = List.copyOf(requireNonNull(associations));
    this.license = license;
    this.notice = notice;
  }

  /**
   * Returns whether {@code key} is the key of a define in this {@code Lang} object.
   *
   * @param key the key of the define
   * @return whether {@code key} is the key of a define in this {@code Lang} object
   * @throws java.lang.NullPointerException if {@code key} is {@code null}
   * @since 1.0.0
   */
  public boolean hasDefine(String key) {
    return this.defines.containsKey(requireNonNull(key));
  }

  /**
   * Returns the value of the define with the key {@code key} in this {@code Lang} object.
   *
   * @param key the key of the define
   * @return the value of the define with the key {@code key} in this {@code Lang} object
   * @throws java.lang.NullPointerException if {@code key} is {@code null}
   * @throws java.lang.IllegalArgumentException if {@code key} is not the key of a define in this
   *     {@code Lang} object
   * @since 1.0.0
   */
  public String getDefine(String key) {
    if (!this.hasDefine(key)) {
      throw new IllegalArgumentException(String.format("Define \"%s\" not found", key));
    }
    return this.defines.get(key);
  }

  /**
   * Returns all defines in this {@code Lang} object.
   *
   * @return all defines in this {@code Lang} object
   * @since 1.0.0
   */
  public Map<String, String> getDefines() {
    return this.defines;
  }

  /**
   * Returns whether {@code name} is the name of a category in this {@code Lang} object.
   *
   * @param name the name of the category
   * @return whether {@code name} is the name of a category in this {@code Lang} object
   * @throws java.lang.NullPointerException if {@code name} is {@code null}
   * @since 1.0.0
   */
  public boolean hasCategory(String name) {
    return this.categories.containsKey(requireNonNull(name));
  }

  /**
   * Returns the category with the name {@code name} in this {@code Lang} object.
   *
   * @param name the name of the category
   * @return the category with the name {@code name} in this {@code Lang} object
   * @throws java.lang.NullPointerException if {@code name} is {@code null}
   * @throws java.lang.IllegalArgumentException if {@code name} is not the name of a category in
   *     this {@code Lang} object
   * @since 1.0.0
   */
  public Category getCategory(String name) {
    if (!this.hasCategory(name)) {
      throw new IllegalArgumentException(String.format("Category \"%s\" not found", name));
    }
    return this.categories.get(name);
  }

  /**
   * Returns a list of all categories in this {@code Lang} object.
   *
   * @return a list of all categories in this {@code Lang} object
   * @since 1.0.0
   */
  public List<Category> getCategories() {
    return List.copyOf(this.categories.values());
  }

  /**
   * Returns whether {@code name} is the name of an asset in this {@code Lang} object.
   *
   * @param name the name of the asset
   * @return whether {@code name} is the name of an asset in this {@code Lang} object
   * @throws java.lang.NullPointerException if {@code name} is {@code null}
   * @since 1.0.0
   */
  public boolean hasAsset(String name) {
    return this.assets.containsKey(requireNonNull(name));
  }

  /**
   * Returns the asset with the name {@code name} in this {@code Lang} object.
   *
   * @param name the name of the asset
   * @return the asset with the name {@code name} in this {@code Lang} object
   * @throws java.lang.NullPointerException if {@code name} is {@code null}
   * @throws java.lang.IllegalArgumentException if {@code name} is not the name of an asset in this
   *     {@code Lang} object
   * @since 1.0.0
   */
  public Asset getAsset(String name) {
    if (!this.hasAsset(name)) {
      throw new IllegalArgumentException(String.format("Asset \"%s\" not found", name));
    }
    return this.assets.get(name);
  }

  /**
   * Returns a list of all assets in this {@code Lang} object.
   *
   * @return a list of all assets in this {@code Lang} object
   * @since 1.0.0
   */
  public List<Asset> getAssets() {
    return List.copyOf(this.assets.values());
  }

  /**
   * Returns a list of all associations in this {@code Lang} object.
   *
   * @return a list of all associations in this {@code Lang} object
   * @since 1.0.0
   */
  public List<Association> getAssociations() {
    return this.associations;
  }

  /**
   * Returns whether this {@code Lang} object has a license.
   *
   * @return whether this {@code Lang} object has a license
   * @since 1.0.0
   */
  public boolean hasLicense() {
    return this.license != null;
  }

  /**
   * Returns the license of this {@code Lang} object.
   *
   * @return the license of this {@code Lang} object
   * @throws java.lang.UnsupportedOperationException if this {@code Lang} object does not have a
   *     license
   * @since 1.0.0
   */
  public String getLicense() {
    if (!this.hasLicense()) {
      throw new UnsupportedOperationException("License not found");
    }
    return this.license;
  }

  /**
   * Returns whether this {@code Lang} object has a notice.
   *
   * @return whether this {@code Lang} object has a notice
   * @since 1.0.0
   */
  public boolean hasNotice() {
    return this.notice != null;
  }

  /**
   * Returns the notice of this {@code Lang} object.
   *
   * @return the notice of this {@code Lang} object
   * @throws java.lang.UnsupportedOperationException if this {@code Lang} object does not have a
   *     notice
   * @since 1.0.0
   */
  public String getNotice() {
    if (!this.hasNotice()) {
      throw new UnsupportedOperationException("Notice not found");
    }
    return this.notice;
  }

  /**
   * Returns the JSON representation of this {@code Lang} object.
   *
   * @return the JSON representation of this {@code Lang} object
   * @since 1.0.0
   */
  public JsonObject toJson() {
    // Defines
    var jsonDefines = Json.createObjectBuilder();
    for (var define : this.defines.entrySet()) {
      jsonDefines.add(define.getKey(), define.getValue());
    }

    // Categories
    var jsonCategories = Json.createArrayBuilder();
    for (var category : this.categories.values()) {
      jsonCategories.add(category.toJson());
    }

    // Assets
    var jsonAssets = Json.createArrayBuilder();
    for (var asset : this.assets.values()) {
      jsonAssets.add(asset.toJson());
    }

    // Associations
    var jsonAssociations = Json.createArrayBuilder();
    for (var association : this.associations) {
      jsonAssociations.add(association.toJson());
    }

    return Json.createObjectBuilder()
        .add("formatVersion", Utils.getFormatVersion())
        .add("defines", jsonDefines)
        .add("categories", jsonCategories)
        .add("assets", jsonAssets)
        .add("associations", jsonAssociations)
        .build();
  }

  /**
   * Creates a new {@code Lang} object from a {@link org.mal_lang.langspec.builders.LangBuilder}.
   *
   * @param builder the {@link org.mal_lang.langspec.builders.LangBuilder}
   * @return a new {@code Lang} object
   * @throws java.lang.NullPointerException if {@code builder} is {@code null}
   * @since 1.0.0
   */
  public static Lang fromBuilder(LangBuilder builder) {
    requireNonNull(builder);

    // Categories
    Map<String, Category> categories = new LinkedHashMap<>();
    for (var categoryBuilder : builder.getCategories()) {
      categories.put(categoryBuilder.getName(), Category.fromBuilder(categoryBuilder));
    }

    // Assets
    Map<String, Asset> assets = new LinkedHashMap<>();
    for (var assetBuilder : builder.getAssets()) {
      assets.put(assetBuilder.getName(), Asset.fromBuilder(assetBuilder, categories));
    }
    for (var assetBuilder : builder.getAssets()) {
      if (assetBuilder.getSuperAsset() == null) {
        continue;
      }
      if (!assets.containsKey(assetBuilder.getSuperAsset())) {
        throw new IllegalArgumentException(
            String.format("Asset \"%s\" not found", assetBuilder.getSuperAsset()));
      }
      assets.get(assetBuilder.getName()).setSuperAsset(assets.get(assetBuilder.getSuperAsset()));
    }

    // Associations
    List<Association> associations = new ArrayList<>();
    for (var associationBuilder : builder.getAssociations()) {
      associations.add(Association.fromBuilder(associationBuilder, assets));
    }

    // Variable targets
    var variableTargets = new LinkedHashMap<Variable, Asset>();
    for (var assetBuilder : builder.getAssets()) {
      var asset = assets.get(assetBuilder.getName());
      for (var variableBuilder : assetBuilder.getVariables()) {
        var variable = asset.getLocalVariable(variableBuilder.getName());
        var targetAsset =
            variableBuilder.getStepExpression().getTarget(asset, assets, builder.getAssets());
        variableTargets.put(variable, targetAsset);
      }
    }

    // Variables and attack steps
    for (var assetBuilder : builder.getAssets()) {
      var asset = assets.get(assetBuilder.getName());
      for (var variableBuilder : assetBuilder.getVariables()) {
        var variable = asset.getLocalVariable(variableBuilder.getName());
        variable.setStepExpression(
            StepExpression.fromBuilder(
                variableBuilder.getStepExpression(), asset, assets, variableTargets));
      }
      for (var attackStepBuilder : assetBuilder.getAttackSteps()) {
        var attackStep = asset.getLocalAttackStep(attackStepBuilder.getName());
        if (attackStepBuilder.getRequires() != null) {
          for (var stepExpressionBuilder : attackStepBuilder.getRequires().getStepExpressions()) {
            attackStep
                .getLocalRequires()
                .addStepExpression(
                    StepExpression.fromBuilder(
                        stepExpressionBuilder, asset, assets, variableTargets));
          }
        }
        if (attackStepBuilder.getReaches() != null) {
          for (var stepExpressionBuilder : attackStepBuilder.getReaches().getStepExpressions()) {
            attackStep
                .getLocalReaches()
                .addStepExpression(
                    StepExpression.fromBuilder(
                        stepExpressionBuilder, asset, assets, variableTargets));
          }
        }
      }
    }

    return new Lang(
        builder.getDefines(),
        categories,
        assets,
        associations,
        builder.getLicense(),
        builder.getNotice());
  }
}
