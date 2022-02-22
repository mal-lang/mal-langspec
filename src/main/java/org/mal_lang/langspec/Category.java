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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.mal_lang.langspec.builders.CategoryBuilder;

/**
 * Immutable class representing a category in a MAL language.
 *
 * @since 1.0.0
 */
public final class Category {
  private final String name;
  private final Meta meta;
  private final Map<String, Asset> assets = new LinkedHashMap<>();

  private Category(String name, Meta meta) {
    this.name = requireNonNull(name);
    this.meta = requireNonNull(meta);
  }

  /**
   * Returns the name of this {@code Category} object.
   *
   * @return the name of this {@code Category} object
   * @since 1.0.0
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the meta info of this {@code Category} object.
   *
   * @return the meta info of this {@code Category} object
   * @since 1.0.0
   */
  public Meta getMeta() {
    return this.meta;
  }

  /**
   * Returns whether {@code name} is the name of an asset in this {@code Category} object.
   *
   * @param name the name of the asset
   * @return whether {@code name} is the name of an asset in this {@code Category} object
   * @throws java.lang.NullPointerException if {@code name} is {@code null}
   * @since 1.0.0
   */
  public boolean hasAsset(String name) {
    return this.assets.containsKey(requireNonNull(name));
  }

  /**
   * Returns the asset with the name {@code name} in this {@code Category} object.
   *
   * @param name the name of the asset
   * @return the asset with the name {@code name} in this {@code Category} object
   * @throws java.lang.NullPointerException if {@code name} is {@code null}
   * @throws java.lang.IllegalArgumentException if {@code name} is not the name of an asset in this
   *     {@code Category} object
   * @since 1.0.0
   */
  public Asset getAsset(String name) {
    if (!this.hasAsset(name)) {
      throw new IllegalArgumentException(String.format("Asset \"%s\" not found", name));
    }
    return this.assets.get(name);
  }

  /**
   * Returns a list of all assets in this {@code Category} object.
   *
   * @return a list of all assets in this {@code Category} object
   * @since 1.0.0
   */
  public List<Asset> getAssets() {
    return List.copyOf(this.assets.values());
  }

  void addAsset(Asset asset) {
    requireNonNull(asset);
    this.assets.put(asset.getName(), asset);
  }

  JsonObject toJson() {
    return Json.createObjectBuilder()
        .add("name", this.name)
        .add("meta", this.meta.toJson())
        .build();
  }

  static Category fromBuilder(CategoryBuilder builder) {
    requireNonNull(builder);
    return new Category(builder.getName(), Meta.fromBuilder(builder.getMeta()));
  }
}
