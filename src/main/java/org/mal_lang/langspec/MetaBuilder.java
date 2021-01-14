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
import java.util.LinkedHashMap;
import java.util.Map;

/** A builder for creating {@link Meta} objects. */
public final class MetaBuilder {
  private final Map<String, String> entries = new LinkedHashMap<>();

  MetaBuilder() {}

  /**
   * Adds an entry to this {@code MetaBuilder} object.
   *
   * @param key the key of the entry
   * @param value the value of the entry
   * @return this {@code MetaBuilder} object
   * @throws NullPointerException if {@code key} or {@code value} is {@code null}
   * @throws IllegalArgumentException if {@code key} is not a valid identifier
   */
  public MetaBuilder addEntry(String key, String value) {
    checkIdentifier(key);
    checkNotNull(value);
    entries.put(key, value);
    return this;
  }

  /**
   * Creates a new {@link Meta} object.
   *
   * @return a new {@link Meta} object
   */
  public Meta build() {
    return new Meta(entries);
  }

  static MetaBuilder fromJson(JsonObject jsonMeta) {
    var metaBuilder = Meta.builder();
    for (var key : jsonMeta.keySet()) {
      metaBuilder.addEntry(key, jsonMeta.getString(key));
    }
    return metaBuilder;
  }
}
