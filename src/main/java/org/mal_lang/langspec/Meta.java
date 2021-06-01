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

import static java.util.Objects.requireNonNull;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import java.util.Map;
import org.mal_lang.langspec.builders.MetaBuilder;

/**
 * Immutable class representing meta info of {@link org.mal_lang.langspec.Category}, {@link
 * org.mal_lang.langspec.Asset}, {@link org.mal_lang.langspec.AttackStep}, and {@link
 * org.mal_lang.langspec.Association} objects.
 *
 * @since 1.0.0
 */
public final class Meta {
  private final Map<String, String> entries;

  private Meta(Map<String, String> entries) {
    this.entries = Map.copyOf(requireNonNull(entries));
  }

  /**
   * Returns whether {@code key} is the key of an entry in this {@code Meta} object.
   *
   * @param key the key of the entry
   * @return whether {@code key} is the key of an entry in this {@code Meta} object
   * @throws java.lang.NullPointerException if {@code key} is {@code null}
   * @since 1.0.0
   */
  public boolean hasEntry(String key) {
    return this.entries.containsKey(requireNonNull(key));
  }

  /**
   * Returns the value of the entry with the key {@code key} in this {@code Meta} object.
   *
   * @param key the key of the entry
   * @return the value of the entry with the key {@code key} in this {@code Meta} object
   * @throws java.lang.NullPointerException if {@code key} is {@code null}
   * @throws java.lang.IllegalArgumentException if {@code key} is not the key of an entry in this
   *     {@code Meta} object
   * @since 1.0.0
   */
  public String getEntry(String key) {
    if (!this.hasEntry(key)) {
      throw new IllegalArgumentException(String.format("Entry \"%s\" not found", key));
    }
    return this.entries.get(key);
  }

  /**
   * Returns all entries in this {@code Meta} object.
   *
   * @return all entries in this {@code Meta} object
   * @since 1.0.0
   */
  public Map<String, String> getEntries() {
    return this.entries;
  }

  JsonObject toJson() {
    var jsonMeta = Json.createObjectBuilder();
    for (var entry : this.entries.entrySet()) {
      jsonMeta.add(entry.getKey(), entry.getValue());
    }
    return jsonMeta.build();
  }

  static Meta fromBuilder(MetaBuilder builder) {
    return new Meta(requireNonNull(builder).getEntries());
  }
}
