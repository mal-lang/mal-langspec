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

import jakarta.json.Json;
import jakarta.json.JsonObject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Immutable class representing a MAL language. */
public final class Lang {
  private final Map<String, String> defines;
  private final Map<String, Category> categories;
  private final Map<String, Asset> assets;
  private final List<Association> associations;

  private Lang(
      Map<String, String> defines,
      Map<String, Category> categories,
      Map<String, Asset> assets,
      List<Association> associations) {
    this.defines = Map.copyOf(defines);
    this.categories = Map.copyOf(categories);
    this.assets = Map.copyOf(assets);
    this.associations = List.copyOf(associations);
  }

  /**
   * Returns whether {@code key} is the key of a define in this {@code Lang} object.
   *
   * @param key the key of the define
   * @return whether {@code key} is the key of a define in this {@code Lang} object
   * @throws NullPointerException if {@code key} is {@code null}
   */
  public boolean hasDefine(String key) {
    checkNotNull(key);
    return defines.containsKey(key);
  }

  /**
   * Returns the value of the define in this {@code Lang} object with the key {@code key}.
   *
   * @param key the key of the define
   * @return the value of the define in this {@code Lang} object with the key {@code key}
   * @throws NullPointerException if {@code key} is {@code null}
   * @throws IllegalArgumentException if {@code key} is not the key of a define in this {@code Lang}
   *     object
   */
  public String getDefine(String key) {
    if (!hasDefine(key)) {
      throw new IllegalArgumentException(String.format("Define \"%s\" not found", key));
    }
    return defines.get(key);
  }

  /**
   * Returns all defines in this {@code Lang} object.
   *
   * @return all defines in this {@code Lang} object
   */
  public Map<String, String> getDefines() {
    return defines;
  }

  /**
   * Returns whether {@code name} is the name of a category in this {@code Lang} object.
   *
   * @param name the name of the category
   * @return whether {@code name} is the name of a category in this {@code Lang} object
   * @throws NullPointerException if {@code name} is {@code null}
   */
  public boolean hasCategory(String name) {
    checkNotNull(name);
    return categories.containsKey(name);
  }

  /**
   * Returns the category in this {@code Lang} object with the name {@code name}.
   *
   * @param name the name of the category
   * @return the category in this {@code Lang} object with the name {@code name}
   * @throws NullPointerException if {@code name} is {@code null}
   * @throws IllegalArgumentException if {@code name} is not the name of a category in this {@code
   *     Lang} object
   */
  public Category getCategory(String name) {
    if (!hasCategory(name)) {
      throw new IllegalArgumentException(String.format("Category \"%s\" not found", name));
    }
    return categories.get(name);
  }

  /**
   * Returns a list of all categories in this {@code Lang} object.
   *
   * @return a list of all categories in this {@code Lang} object
   */
  public List<Category> getCategories() {
    return List.copyOf(categories.values());
  }

  /**
   * Returns whether {@code name} is the name of an asset in this {@code Lang} object.
   *
   * @param name the name of the asset
   * @return whether {@code name} is the name of an asset in this {@code Lang} object
   * @throws NullPointerException if {@code name} is {@code null}
   */
  public boolean hasAsset(String name) {
    checkNotNull(name);
    return assets.containsKey(name);
  }

  /**
   * Returns the asset in this {@code Lang} object with the name {@code name}.
   *
   * @param name the name of the asset
   * @return the asset in this {@code Lang} object with the name {@code name}
   * @throws NullPointerException if {@code name} is {@code null}
   * @throws IllegalArgumentException if {@code name} is not the name of an asset in this {@code
   *     Lang} object
   */
  public Asset getAsset(String name) {
    if (!hasAsset(name)) {
      throw new IllegalArgumentException(String.format("Asset \"%s\" not found", name));
    }
    return assets.get(name);
  }

  /**
   * Returns a list of all assets in this {@code Lang} object.
   *
   * @return a list of all assets in this {@code Lang} object
   */
  public List<Asset> getAssets() {
    return List.copyOf(assets.values());
  }

  /**
   * Returns a list of all associations in this {@code Lang} object.
   *
   * @return a list of all associations in this {@code Lang} object
   */
  public List<Association> getAssociations() {
    return associations;
  }

  /**
   * Returns the JSON representation of this {@code Lang} object.
   *
   * @return the JSON representation of this {@code Lang} object
   */
  public JsonObject toJson() {
    // Defines
    var jsonDefines = Json.createObjectBuilder();
    for (var define : defines.entrySet()) {
      jsonDefines.add(define.getKey(), define.getValue());
    }

    // Categories
    var jsonCategories = Json.createArrayBuilder();
    for (var category : categories.values()) {
      jsonCategories.add(category.toJson());
    }

    // Assets
    var jsonAssets = Json.createArrayBuilder();
    for (var asset : assets.values()) {
      jsonAssets.add(asset.toJson());
    }

    // Associations
    var jsonAssociations = Json.createArrayBuilder();
    for (var association : associations) {
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
   * Creates a new {@code Lang} object from the JSON representation of a {@code Lang} object.
   *
   * @param jsonLang the JSON representation of a {@code Lang} object
   * @return a new {@code Lang} object from the JSON representation of a {@code Lang} object
   * @throws NullPointerException if {@code jsonLang} is {@code null}
   * @throws IllegalArgumentException if {@code jsonLang} does not comply with the schema
   */
  public static Lang fromJson(JsonObject jsonLang) {
    if (!Utils.isValidJsonLang(jsonLang)) {
      throw new IllegalArgumentException("Invalid jsonLang");
    }
    return Lang.Builder.fromJson(jsonLang).build();
  }

  /** A builder for creating {@link Lang} objects. */
  public static final class Builder {
    private final Map<String, String> defines = new LinkedHashMap<>();
    private final Map<String, Category.Builder> categories = new LinkedHashMap<>();
    private final Map<String, Asset.Builder> assets = new LinkedHashMap<>();
    private final List<Association.Builder> associations = new ArrayList<>();

    private Builder() {}

    /**
     * Adds a define to this {@code Lang.Builder} object.
     *
     * @param key the key of the define
     * @param value the value of the define
     * @return this {@code Lang.Builder} object
     * @throws NullPointerException if {@code key} or {@code value} is {@code null}
     * @throws IllegalArgumentException if {@code key} is not a valid identifier
     */
    public Builder addDefine(String key, String value) {
      checkIdentifier(key);
      checkNotNull(value);
      defines.put(key, value);
      return this;
    }

    /**
     * Adds a category to this {@code Lang.Builder} object.
     *
     * @param category the category to add
     * @return this {@code Lang.Builder} object
     * @throws NullPointerException if {@code category} is {@code null}
     */
    public Builder addCategory(Category.Builder category) {
      checkNotNull(category);
      categories.put(category.getName(), category);
      return this;
    }

    /**
     * Adds an asset to this {@code Lang.Builder} object.
     *
     * @param asset the asset to add
     * @return this {@code Lang.Builder} object
     * @throws NullPointerException if {@code asset} is {@code null}
     */
    public Builder addAsset(Asset.Builder asset) {
      checkNotNull(asset);
      assets.put(asset.getName(), asset);
      return this;
    }

    /**
     * Adds an association to this {@code Lang.Builder} object.
     *
     * @param association the association to add
     * @return this {@code Lang.Builder} object
     * @throws NullPointerException if {@code association} is {@code null}
     */
    public Builder addAssociation(Association.Builder association) {
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

    static Builder fromJson(JsonObject jsonLang) {
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
        var categoryBuilder = Category.Builder.fromJson(jsonCategory);
        langBuilder.addCategory(categoryBuilder);
      }

      // Assets
      var jsonAssets = jsonLang.getJsonArray("assets");
      for (int i = 0; i < jsonAssets.size(); i++) {
        var jsonAsset = jsonAssets.getJsonObject(i);
        var assetBuilder = Asset.Builder.fromJson(jsonAsset);
        langBuilder.addAsset(assetBuilder);
      }

      // Associations
      var jsonAssociations = jsonLang.getJsonArray("associations");
      for (int i = 0; i < jsonAssociations.size(); i++) {
        var jsonAssociation = jsonAssociations.getJsonObject(i);
        var associationBuilder = Association.Builder.fromJson(jsonAssociation);
        langBuilder.addAssociation(associationBuilder);
      }

      return langBuilder;
    }
  }

  /**
   * Creates a new {@link Builder} object.
   *
   * @return a new {@link Builder} object
   */
  public static Builder builder() {
    return new Builder();
  }
}
