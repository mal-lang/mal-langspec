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

import jakarta.json.Json;
import jakarta.json.JsonArray;
import java.util.LinkedHashMap;
import java.util.Map;

/** Immutable class representing the risk of an attack step in a MAL language. */
public final class Risk {
  private static final String CONFIDENTIALITY = "confidentiality";
  private static final String INTEGRITY = "integrity";
  private static final String AVAILABILITY = "availability";

  private final boolean isConfidentiality;
  private final boolean isIntegrity;
  private final boolean isAvailability;

  /**
   * Construct a new {@code Risk} object.
   *
   * @param isConfidentiality whether this {@code Risk} object is confidentiality
   * @param isIntegrity whether this {@code Risk} object is integrity
   * @param isAvailability whether this {@code Risk} object is availability
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
   */
  public boolean isConfidentiality() {
    return isConfidentiality;
  }

  /**
   * Returns whether this {@code Risk} object is integrity.
   *
   * @return whether this {@code Risk} object is integrity
   */
  public boolean isIntegrity() {
    return isIntegrity;
  }

  /**
   * Returns whether this {@code Risk} object is availability.
   *
   * @return whether this {@code Risk} object is availability
   */
  public boolean isAvailability() {
    return isAvailability;
  }

  /**
   * Returns the JSON representation of this {@code Risk} object.
   *
   * @return the JSON representation of this {@code Risk} object
   */
  public JsonArray toJson() {
    var jsonRisk = Json.createArrayBuilder();
    if (isConfidentiality) {
      jsonRisk.add(CONFIDENTIALITY);
    }
    if (isIntegrity) {
      jsonRisk.add(INTEGRITY);
    }
    if (isAvailability) {
      jsonRisk.add(AVAILABILITY);
    }
    return jsonRisk.build();
  }

  static Risk fromJson(JsonArray jsonRisk) {
    Map<String, Boolean> riskMap =
        new LinkedHashMap<>(
            Map.ofEntries(
                Map.entry(CONFIDENTIALITY, false),
                Map.entry(INTEGRITY, false),
                Map.entry(AVAILABILITY, false)));
    for (int i = 0; i < jsonRisk.size(); i++) {
      var risk = jsonRisk.getString(i);
      riskMap.put(risk, true);
    }
    return new Risk(
        riskMap.get(CONFIDENTIALITY), riskMap.get(INTEGRITY), riskMap.get(AVAILABILITY));
  }
}
