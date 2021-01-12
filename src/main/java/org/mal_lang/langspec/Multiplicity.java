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
import jakarta.json.JsonObject;

/** Immutable class representing the multiplicity of a field of an asset in a MAL language. */
public final class Multiplicity {
  private final int min;
  private final int max;

  /**
   * Constructs a new {@code Multiplicity} object.
   *
   * @param min the minimum, must be {@code 0} or {@code 1}
   * @param max the maximum, must be {@code 1} or {@code Integer.MAX_VALUE}
   * @throws IllegalArgumentException if {@code min} or {@code max} is invalid
   */
  public Multiplicity(int min, int max) {
    if (min != 0 && min != 1) {
      throw new IllegalArgumentException(String.format("Invalid min = %d", min));
    }
    if (max != 1 && max != Integer.MAX_VALUE) {
      throw new IllegalArgumentException(String.format("Invalid max = %d", max));
    }
    this.min = min;
    this.max = max;
  }

  /**
   * Returns the minimum of this {@code Multiplicity} object.
   *
   * @return the minimum of this {@code Multiplicity} object
   */
  public int getMin() {
    return min;
  }

  /**
   * Returns the maximum of this {@code Multiplicity} object.
   *
   * @return the maximum of this {@code Multiplicity} object
   */
  public int getMax() {
    return max;
  }

  /**
   * Returns the JSON representation of this {@code Multiplicity} object.
   *
   * @return the JSON representation of this {@code Multiplicity} object
   */
  public JsonObject toJson() {
    var jsonMultiplicity = Json.createObjectBuilder().add("min", min);
    if (max < Integer.MAX_VALUE) {
      jsonMultiplicity.add("max", max);
    }
    return jsonMultiplicity.build();
  }

  static Multiplicity fromJson(JsonObject jsonMultiplicity) {
    int min = jsonMultiplicity.getInt("min");
    int max =
        jsonMultiplicity.containsKey("max") ? jsonMultiplicity.getInt("max") : Integer.MAX_VALUE;
    return new Multiplicity(min, max);
  }
}
