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
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A builder for creating {@link org.mal_lang.langspec.Meta} objects.
 *
 * @since 1.0.0
 */
public final class MetaBuilder {
  private final Map<String, String> entries = new LinkedHashMap<>();

  /**
   * Constructs a new {@code MetaBuilder} object.
   *
   * @since 1.0.0
   */
  public MetaBuilder() {}

  /**
   * Returns all entries in this {@code MetaBuilder} object.
   *
   * @return all entries in this {@code MetaBuilder} object
   * @since 1.0.0
   */
  public Map<String, String> getEntries() {
    return Map.copyOf(this.entries);
  }

  /**
   * Adds an entry to this {@code MetaBuilder} object.
   *
   * @param key the key of the entry
   * @param value the value of the entry
   * @return this {@code MetaBuilder} object
   * @throws java.lang.NullPointerException if {@code key} or {@code value} is {@code null}
   * @throws java.lang.IllegalArgumentException if {@code key} is not a valid identifier
   * @since 1.0.0
   */
  public MetaBuilder addEntry(String key, String value) {
    this.entries.put(requireIdentifier(key), requireNonNull(value));
    return this;
  }

  /**
   * Adds entries to this {@code MetaBuilder} from a {@link jakarta.json.JsonObject}.
   *
   * @param jsonMeta the {@link jakarta.json.JsonObject}
   * @return this {@code MetaBuilder} object
   * @throws java.lang.NullPointerException if {@code jsonMeta} is {@code null}
   * @since 1.0.0
   */
  public MetaBuilder fromJson(JsonObject jsonMeta) {
    requireNonNull(jsonMeta);
    for (var key : jsonMeta.keySet()) {
      this.addEntry(key, jsonMeta.getString(key));
    }
    return this;
  }
}
