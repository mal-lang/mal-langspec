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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Immutable class representing a category in a MAL language. */
public final class Category {
  private final String name;
  private final Meta meta;
  private final Map<String, Asset> assets = new LinkedHashMap<>();

  Category(String name, Meta meta) {
    this.name = name;
    this.meta = meta;
  }

  /**
   * Returns the name of this {@code Category} object.
   *
   * @return the name of this {@code Category} object
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the meta info of this {@code Category} object.
   *
   * @return the meta info of this {@code Category} object
   */
  public Meta getMeta() {
    return meta;
  }

  void addAsset(Asset asset) {
    assets.put(asset.getName(), asset);
  }

  /**
   * Returns whether {@code name} is the name of an asset in this {@code Category} object.
   *
   * @param name the name of the asset
   * @return whether {@code name} is the name of an asset in this {@code Category} object
   * @throws NullPointerException if {@code name} is {@code null}
   */
  public boolean hasAsset(String name) {
    checkNotNull(name);
    return assets.containsKey(name);
  }

  /**
   * Returns the asset in this {@code Category} object with the name {@code name}.
   *
   * @param name the name of the asset
   * @return the asset in this {@code Category} object with the name {@code name}
   * @throws NullPointerException if {@code name} is {@code null}
   * @throws IllegalArgumentException if {@code name} is not the name of an asset in this {@code
   *     Category} object
   */
  public Asset getAsset(String name) {
    if (!hasAsset(name)) {
      throw new IllegalArgumentException(String.format("Asset \"%s\" not found", name));
    }
    return assets.get(name);
  }

  /**
   * Returns a list of all assets in this {@code Category} object.
   *
   * @return a list of all assets in this {@code Category} object
   */
  public List<Asset> getAssets() {
    return List.copyOf(assets.values());
  }

  /**
   * Returns the JSON representation of this {@code Category} object.
   *
   * @return the JSON representation of this {@code Category} object
   */
  public JsonObject toJson() {
    return Json.createObjectBuilder().add("name", name).add("meta", meta.toJson()).build();
  }

  /**
   * Creates a new {@link CategoryBuilder} object.
   *
   * @param name the name of the category
   * @return a new {@link CategoryBuilder} object
   * @throws NullPointerException if {@code name} is {@code null}
   * @throws IllegalArgumentException if {@code name} is not a valid identifier
   */
  public static CategoryBuilder builder(String name) {
    checkIdentifier(name);
    return new CategoryBuilder(name);
  }
}
