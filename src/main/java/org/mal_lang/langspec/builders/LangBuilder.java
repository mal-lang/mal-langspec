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

package org.mal_lang.langspec.builders;

import static java.util.Objects.requireNonNull;
import static org.mal_lang.langspec.Utils.requireIdentifier;

import jakarta.json.JsonObject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A builder for creating {@link org.mal_lang.langspec.Lang} objects.
 *
 * @since 1.0.0
 */
public final class LangBuilder {
  private final Map<String, String> defines = new LinkedHashMap<>();
  private final Map<String, CategoryBuilder> categories = new LinkedHashMap<>();
  private final Map<String, AssetBuilder> assets = new LinkedHashMap<>();
  private final List<AssociationBuilder> associations = new ArrayList<>();
  private String license = null;
  private String notice = null;

  /**
   * Constructs a new {@code LangBuilder} object.
   *
   * @since 1.0.0
   */
  public LangBuilder() {}

  /**
   * Returns all defines in this {@code LangBuilder} object.
   *
   * @return all defines in this {@code LangBuilder} object
   * @since 1.0.0
   */
  public Map<String, String> getDefines() {
    return Map.copyOf(this.defines);
  }

  /**
   * Adds a define to this {@code LangBuilder} object.
   *
   * @param key the key of the define
   * @param value the value of the define
   * @return this {@code LangBuilder} object
   * @throws java.lang.NullPointerException if {@code key} or {@code value} is {@code null}
   * @throws java.lang.IllegalArgumentException if {@code key} is not a valid identifier
   * @since 1.0.0
   */
  public LangBuilder addDefine(String key, String value) {
    this.defines.put(requireIdentifier(key), requireNonNull(value));
    return this;
  }

  /**
   * Returns a list of all categories in this {@code LangBuilder} object.
   *
   * @return a list of all categories in this {@code LangBuilder} object
   * @since 1.0.0
   */
  public List<CategoryBuilder> getCategories() {
    return List.copyOf(this.categories.values());
  }

  /**
   * Adds a category to this {@code LangBuilder} object.
   *
   * @param category the category to add
   * @return this {@code LangBuilder} object
   * @throws java.lang.NullPointerException if {@code category} is {@code null}
   * @since 1.0.0
   */
  public LangBuilder addCategory(CategoryBuilder category) {
    requireNonNull(category);
    this.categories.put(category.getName(), category);
    return this;
  }

  /**
   * Returns a list of all assets in this {@code LangBuilder} object.
   *
   * @return a list of all assets in this {@code LangBuilder} object
   * @since 1.0.0
   */
  public List<AssetBuilder> getAssets() {
    return List.copyOf(this.assets.values());
  }

  /**
   * Adds an asset to this {@code LangBuilder} object.
   *
   * @param asset the asset to add
   * @return this {@code LangBuilder} object
   * @throws java.lang.NullPointerException if {@code asset} is {@code null}
   * @since 1.0.0
   */
  public LangBuilder addAsset(AssetBuilder asset) {
    requireNonNull(asset);
    this.assets.put(asset.getName(), asset);
    return this;
  }

  /**
   * Returns a list of all associations in this {@code LangBuilder} object.
   *
   * @return a list of all associations in this {@code LangBuilder} object
   * @since 1.0.0
   */
  public List<AssociationBuilder> getAssociations() {
    return List.copyOf(this.associations);
  }

  /**
   * Adds an association to this {@code LangBuilder} object.
   *
   * @param association the association to add
   * @return this {@code LangBuilder} object
   * @throws java.lang.NullPointerException if {@code association} is {@code null}
   * @since 1.0.0
   */
  public LangBuilder addAssociation(AssociationBuilder association) {
    this.associations.add(requireNonNull(association));
    return this;
  }

  /**
   * Returns the license of this {@code LangBuilder} object, or {@code null} if no license has been
   * set.
   *
   * @return the license of this {@code LangBuilder} object, or {@code null} if no license has been
   *     set
   * @since 1.0.0
   */
  public String getLicense() {
    return this.license;
  }

  /**
   * Sets the license of this {@code LangBuilder} object.
   *
   * @param license the license to set
   * @return this {@code LangBuilder} object
   * @throws java.lang.NullPointerException if {@code license} is {@code null}
   * @since 1.0.0
   */
  public LangBuilder setLicense(String license) {
    this.license = requireNonNull(license);
    return this;
  }

  /**
   * Returns the notice of this {@code LangBuilder} object, or {@code null} if no notice has been
   * set.
   *
   * @return the notice of this {@code LangBuilder} object, or {@code null} if no notice has been
   *     set
   * @since 1.0.0
   */
  public String getNotice() {
    return this.notice;
  }

  /**
   * Sets the notice of this {@code LangBuilder} object.
   *
   * @param notice the notice to set
   * @return this {@code LangBuilder} object
   * @throws java.lang.NullPointerException if {@code notice} is {@code null}
   * @since 1.0.0
   */
  public LangBuilder setNotice(String notice) {
    this.notice = requireNonNull(notice);
    return this;
  }

  /**
   * Creates a new {@code LangBuilder} from a {@link jakarta.json.JsonObject}.
   *
   * @param jsonLang the {@link jakarta.json.JsonObject}
   * @return a new {@code LangBuilder}
   * @throws java.lang.NullPointerException if {@code jsonLang} is {@code null}
   * @since 1.0.0
   */
  public static LangBuilder fromJson(JsonObject jsonLang) {
    return LangBuilder.fromJson(jsonLang, Map.of(), Map.of(), null, null);
  }

  /**
   * Creates a new {@code LangBuilder} from a {@link jakarta.json.JsonObject}.
   *
   * @param jsonLang the {@link jakarta.json.JsonObject}
   * @param svgIcons the SVG icons of the language
   * @param pngIcons the PNG icons of the language
   * @return a new {@code LangBuilder}
   * @throws java.lang.NullPointerException if {@code jsonLang}, {@code svgIcons}, or {@code
   *     pngIcons} is {@code null}
   * @since 1.0.0
   */
  public static LangBuilder fromJson(
      JsonObject jsonLang, Map<String, byte[]> svgIcons, Map<String, byte[]> pngIcons) {
    return LangBuilder.fromJson(jsonLang, svgIcons, pngIcons, null, null);
  }

  /**
   * Creates a new {@code LangBuilder} from a {@link jakarta.json.JsonObject}.
   *
   * @param jsonLang the {@link jakarta.json.JsonObject}
   * @param license the license of the language, or {@code null}
   * @param notice the notice of the language, or {@code null}
   * @return a new {@code LangBuilder}
   * @throws java.lang.NullPointerException if {@code jsonLang} is {@code null}
   * @since 1.0.0
   */
  public static LangBuilder fromJson(JsonObject jsonLang, String license, String notice) {
    return LangBuilder.fromJson(jsonLang, Map.of(), Map.of(), license, notice);
  }

  /**
   * Creates a new {@code LangBuilder} from a {@link jakarta.json.JsonObject}.
   *
   * @param jsonLang the {@link jakarta.json.JsonObject}
   * @param svgIcons the SVG icons of the language
   * @param pngIcons the PNG icons of the language
   * @param license the license of the language, or {@code null}
   * @param notice the notice of the language, or {@code null}
   * @return a new {@code LangBuilder}
   * @throws java.lang.NullPointerException if {@code jsonLang}, {@code svgIcons}, or {@code
   *     pngIcons} is {@code null}
   * @since 1.0.0
   */
  public static LangBuilder fromJson(
      JsonObject jsonLang,
      Map<String, byte[]> svgIcons,
      Map<String, byte[]> pngIcons,
      String license,
      String notice) {
    requireNonNull(jsonLang);
    requireNonNull(svgIcons);
    requireNonNull(pngIcons);
    var builder = new LangBuilder();
    var jsonDefines = jsonLang.getJsonObject("defines");
    for (var key : jsonDefines.keySet()) {
      builder.addDefine(key, jsonDefines.getString(key));
    }
    for (var jsonCategory : jsonLang.getJsonArray("categories")) {
      builder.addCategory(CategoryBuilder.fromJson(jsonCategory.asJsonObject()));
    }
    for (var jsonAsset : jsonLang.getJsonArray("assets")) {
      builder.addAsset(AssetBuilder.fromJson(jsonAsset.asJsonObject(), svgIcons, pngIcons));
    }
    for (var jsonAssociation : jsonLang.getJsonArray("associations")) {
      builder.addAssociation(AssociationBuilder.fromJson(jsonAssociation.asJsonObject()));
    }
    if (license != null) {
      builder.setLicense(license);
    }
    if (notice != null) {
      builder.setNotice(notice);
    }
    return builder;
  }
}
