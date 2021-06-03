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

package org.mal_lang.langspec.builders;

import static java.util.Objects.requireNonNull;
import static org.mal_lang.langspec.Utils.requireIdentifier;

import jakarta.json.JsonObject;

/**
 * A builder for creating {@link org.mal_lang.langspec.Category} objects.
 *
 * @since 1.0.0
 */
public final class CategoryBuilder {
  private final String name;
  private final MetaBuilder meta = new MetaBuilder();

  /**
   * Constructs a new {@code CategoryBuilder} object.
   *
   * @param name the name of the category
   * @throws java.lang.NullPointerException if {@code name} is {@code null}
   * @throws java.lang.IllegalArgumentException if {@code name} is not a valid identifier
   * @since 1.0.0
   */
  public CategoryBuilder(String name) {
    this.name = requireIdentifier(name);
  }

  /**
   * Returns the name of this {@code CategoryBuilder} object.
   *
   * @return the name of this {@code CategoryBuilder} object
   * @since 1.0.0
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the meta info of this {@code CategoryBuilder} object.
   *
   * @return the meta info of this {@code CategoryBuilder} object
   * @since 1.0.0
   */
  public MetaBuilder getMeta() {
    return this.meta;
  }

  /**
   * Creates a new {@code CategoryBuilder} from a {@link jakarta.json.JsonObject}.
   *
   * @param jsonCategory the {@link jakarta.json.JsonObject}
   * @return a new {@code CategoryBuilder} from a {@link jakarta.json.JsonObject}
   * @throws java.lang.NullPointerException if {@code jsonCategory} is {@code null}
   * @since 1.0.0
   */
  public static CategoryBuilder fromJson(JsonObject jsonCategory) {
    requireNonNull(jsonCategory);
    var builder = new CategoryBuilder(jsonCategory.getString("name"));
    builder.getMeta().fromJson(jsonCategory.getJsonObject("meta"));
    return builder;
  }
}
