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

import static org.mal_lang.langspec.Utils.checkIdentifier;
import static org.mal_lang.langspec.Utils.checkNotNull;

import jakarta.json.JsonObject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** A builder for creating {@link Lang} objects. */
public final class LangBuilder {
  private final Map<String, String> defines = new LinkedHashMap<>();
  private final Map<String, CategoryBuilder> categories = new LinkedHashMap<>();
  private final Map<String, AssetBuilder> assets = new LinkedHashMap<>();
  private final List<AssociationBuilder> associations = new ArrayList<>();

  LangBuilder() {}

  /**
   * Adds a define to this {@code LangBuilder} object.
   *
   * @param key the key of the define
   * @param value the value of the define
   * @return this {@code LangBuilder} object
   * @throws NullPointerException if {@code key} or {@code value} is {@code null}
   * @throws IllegalArgumentException if {@code key} is not a valid identifier
   */
  public LangBuilder addDefine(String key, String value) {
    checkIdentifier(key);
    checkNotNull(value);
    defines.put(key, value);
    return this;
  }

  /**
   * Adds a category to this {@code LangBuilder} object.
   *
   * @param category the category to add
   * @return this {@code LangBuilder} object
   * @throws NullPointerException if {@code category} is {@code null}
   */
  public LangBuilder addCategory(CategoryBuilder category) {
    checkNotNull(category);
    categories.put(category.getName(), category);
    return this;
  }

  /**
   * Adds an asset to this {@code LangBuilder} object.
   *
   * @param asset the asset to add
   * @return this {@code LangBuilder} object
   * @throws NullPointerException if {@code asset} is {@code null}
   */
  public LangBuilder addAsset(AssetBuilder asset) {
    checkNotNull(asset);
    assets.put(asset.getName(), asset);
    return this;
  }

  /**
   * Adds an association to this {@code LangBuilder} object.
   *
   * @param association the association to add
   * @return this {@code LangBuilder} object
   * @throws NullPointerException if {@code association} is {@code null}
   */
  public LangBuilder addAssociation(AssociationBuilder association) {
    checkNotNull(association);
    associations.add(association);
    return this;
  }

  /**
   * Creates a new {@link Lang} object.
   *
   * @return a new {@link Lang} object
   */
  public Lang build() {
    // Categories
    var langCategories = new LinkedHashMap<String, Category>();
    for (var category : categories.values()) {
      var langCategory = category.build();
      langCategories.put(langCategory.getName(), langCategory);
    }

    // Assets
    var langAssets = new LinkedHashMap<String, Asset>();
    for (var asset : assets.values()) {
      var langAsset = asset.build(langCategories);
      langAssets.put(langAsset.getName(), langAsset);
    }
    for (var asset : assets.values()) {
      var langAsset = langAssets.get(asset.getName());
      asset.connectSuperAsset(langAssets);
      asset.buildVariables(langAsset);
      asset.buildAttackSteps(langAsset);
    }

    // Associations
    var langAssociations = new ArrayList<Association>();
    for (var association : associations) {
      var langAssociation = association.build(langAssets);
      langAssociations.add(langAssociation);
    }

    return new Lang(defines, langCategories, langAssets, langAssociations);
  }

  static LangBuilder fromJson(JsonObject jsonLang) {
    var langBuilder = Lang.builder();

    // Defines
    var jsonDefines = jsonLang.getJsonObject("defines");
    for (var key : jsonDefines.keySet()) {
      langBuilder.addDefine(key, jsonDefines.getString(key));
    }

    // Categories
    var jsonCategories = jsonLang.getJsonArray("categories");
    for (int i = 0; i < jsonCategories.size(); i++) {
      var jsonCategory = jsonCategories.getJsonObject(i);
      var categoryBuilder = CategoryBuilder.fromJson(jsonCategory);
      langBuilder.addCategory(categoryBuilder);
    }

    // Assets
    var jsonAssets = jsonLang.getJsonArray("assets");
    for (int i = 0; i < jsonAssets.size(); i++) {
      var jsonAsset = jsonAssets.getJsonObject(i);
      var assetBuilder = AssetBuilder.fromJson(jsonAsset);
      langBuilder.addAsset(assetBuilder);
    }

    // Associations
    var jsonAssociations = jsonLang.getJsonArray("associations");
    for (int i = 0; i < jsonAssociations.size(); i++) {
      var jsonAssociation = jsonAssociations.getJsonObject(i);
      var associationBuilder = AssociationBuilder.fromJson(jsonAssociation);
      langBuilder.addAssociation(associationBuilder);
    }

    return langBuilder;
  }
}
