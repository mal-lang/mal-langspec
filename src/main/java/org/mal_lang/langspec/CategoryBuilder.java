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

/** A builder for creating {@link Category} objects. */
public final class CategoryBuilder {
  private final String name;
  private MetaBuilder meta = Meta.builder();

  CategoryBuilder(String name) {
    this.name = name;
  }

  /**
   * Returns the name of this {@code CategoryBuilder} object.
   *
   * @return the name of this {@code CategoryBuilder} object
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the meta info of this {@code CategoryBuilder} object.
   *
   * @return the meta info of this {@code CategoryBuilder} object
   */
  public MetaBuilder getMeta() {
    return meta;
  }

  /**
   * Sets the meta info of this {@code CategoryBuilder} object.
   *
   * @param meta the meta info to set
   * @return this {@code CategoryBuilder} object
   * @throws NullPointerException if {@code meta} is {@code null}
   */
  public CategoryBuilder setMeta(MetaBuilder meta) {
    checkNotNull(meta);
    this.meta = meta;
    return this;
  }

  /**
   * Creates a new {@link Category} object.
   *
   * @return a new {@link Category} object
   */
  public Category build() {
    return new Category(name, meta.build());
  }

  static CategoryBuilder fromJson(JsonObject jsonCategory) {
    return Category.builder(jsonCategory.getString("name"))
        .setMeta(MetaBuilder.fromJson(jsonCategory.getJsonObject("meta")));
  }
}
