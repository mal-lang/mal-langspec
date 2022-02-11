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

/**
 * Immutable class representing the risk of an attack step in a MAL language.
 *
 * @since 1.0.0
 */
public final class Risk {
  private final boolean isConfidentiality;
  private final boolean isIntegrity;
  private final boolean isAvailability;

  /**
   * Constructs a new {@code Risk} object.
   *
   * @param isConfidentiality whether this {@code Risk} object is confidentiality
   * @param isIntegrity whether this {@code Risk} object is integrity
   * @param isAvailability whether this {@code Risk} object is availability
   * @since 1.0.0
   */
  public Risk(boolean isConfidentiality, boolean isIntegrity, boolean isAvailability) {
    this.isConfidentiality = isConfidentiality;
    this.isIntegrity = isIntegrity;
    this.isAvailability = isAvailability;
  }

  /**
   * Returns whether this {@code Risk} object is confidentiality.
   *
   * @return whether this {@code Risk} object is confidentiality
   * @since 1.0.0
   */
  public boolean isConfidentiality() {
    return this.isConfidentiality;
  }

  /**
   * Returns whether this {@code Risk} object is integrity.
   *
   * @return whether this {@code Risk} object is integrity
   * @since 1.0.0
   */
  public boolean isIntegrity() {
    return this.isIntegrity;
  }

  /**
   * Returns whether this {@code Risk} object is availability.
   *
   * @return whether this {@code Risk} object is availability
   * @since 1.0.0
   */
  public boolean isAvailability() {
    return this.isAvailability;
  }

  JsonObject toJson() {
    return Json.createObjectBuilder()
        .add("isConfidentiality", this.isConfidentiality)
        .add("isIntegrity", this.isIntegrity)
        .add("isAvailability", this.isAvailability)
        .build();
  }

  /**
   * Creates a new {@code Risk} from a {@link jakarta.json.JsonObject}.
   *
   * @param jsonRisk the {@link jakarta.json.JsonObject}
   * @return a new {@code Risk}
   * @throws java.lang.NullPointerException if {@code jsonRisk} is {@code null}
   * @since 1.0.0
   */
  public static Risk fromJson(JsonObject jsonRisk) {
    requireNonNull(jsonRisk);
    return new Risk(
        jsonRisk.getBoolean("isConfidentiality"),
        jsonRisk.getBoolean("isIntegrity"),
        jsonRisk.getBoolean("isAvailability"));
  }
}
