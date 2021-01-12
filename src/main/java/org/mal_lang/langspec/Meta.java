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
import java.util.Map;

/**
 * Immutable class representing meta info of {@link Category}, {@link Asset}, {@link AttackStep},
 * and {@link Association} objects.
 */
public final class Meta {
  private final Map<String, String> entries;

  private Meta(Map<String, String> entries) {
    this.entries = Map.copyOf(entries);
  }

  /**
   * Returns whether {@code key} is the key of an entry in this {@code Meta} object.
   *
   * @param key the key of the entry
   * @return whether {@code key} is the key of an entry in this {@code Meta} object
   * @throws NullPointerException if {@code key} is {@code null}
   */
  public boolean hasEntry(String key) {
    checkNotNull(key);
    return entries.containsKey(key);
  }

  /**
   * Returns the value of the entry in this {@code Meta} object with the key {@code key}.
   *
   * @param key the key of the entry
   * @return the value of the entry in this {@code Meta} object with the key {@code key}
   * @throws NullPointerException if {@code key} is {@code null}
   * @throws IllegalArgumentException if {@code key} is not the key of an entry in this {@code Meta}
   *     object
   */
  public String getEntry(String key) {
    if (!hasEntry(key)) {
      throw new IllegalArgumentException(String.format("Entry \"%s\" not found", key));
    }
    return entries.get(key);
  }

  /**
   * Returns all entries in this {@code Meta} object.
   *
   * @return all entries in this {@code Meta} object
   */
  public Map<String, String> getEntries() {
    return entries;
  }

  /**
   * Returns the JSON representation of this {@code Meta} object.
   *
   * @return the JSON representation of this {@code Meta} object
   */
  public JsonObject toJson() {
    var jsonMeta = Json.createObjectBuilder();
    for (var entry : entries.entrySet()) {
      jsonMeta.add(entry.getKey(), entry.getValue());
    }
    return jsonMeta.build();
  }

  /** A builder for creating {@link Meta} objects. */
  public static final class Builder {
    private final Map<String, String> entries = new LinkedHashMap<>();

    private Builder() {}

    /**
     * Adds an entry to this {@code Meta.Builder} object.
     *
     * @param key the key of the entry
     * @param value the value of the entry
     * @return this {@code Meta.Builder} object
     * @throws NullPointerException if {@code key} or {@code value} is {@code null}
     * @throws IllegalArgumentException if {@code key} is not a valid identifier
     */
    public Builder addEntry(String key, String value) {
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

    static Builder fromJson(JsonObject jsonMeta) {
      var metaBuilder = Meta.builder();
      for (var key : jsonMeta.keySet()) {
        metaBuilder.addEntry(key, jsonMeta.getString(key));
      }
      return metaBuilder;
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
