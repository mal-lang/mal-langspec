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

/**
 * Enum representing the multiplicity of a field of an asset in a MAL language.
 *
 * @since 1.0.0
 */
public enum Multiplicity {
  /**
   * Enum constant representing the multiplicity {@code 0..1}.
   *
   * @since 1.0.0
   */
  ZERO_OR_ONE(0, 1),

  /**
   * Enum constant representing the multiplicity {@code 0..*} / {@code *}.
   *
   * @since 1.0.0
   */
  ZERO_OR_MORE(0, Integer.MAX_VALUE),

  /**
   * Enum constant representing the multiplicity {@code 1..1} / {@code 1}.
   *
   * @since 1.0.0
   */
  ONE(1, 1),

  /**
   * Enum constant representing the multiplicity {@code 1..*}.
   *
   * @since 1.0.0
   */
  ONE_OR_MORE(1, Integer.MAX_VALUE);

  private final int min;
  private final int max;

  Multiplicity(int min, int max) {
    this.min = min;
    this.max = max;
  }

  /**
   * Returns the minimum of this {@code Multiplicity} object.
   *
   * @return the minimum of this {@code Multiplicity} object
   * @since 1.0.0
   */
  public int getMin() {
    return this.min;
  }

  /**
   * Returns the maximum of this {@code Multiplicity} object.
   *
   * @return the maximum of this {@code Multiplicity} object
   * @since 1.0.0
   */
  public int getMax() {
    return this.max;
  }

  JsonObject toJson() {
    var jsonMultiplicity = Json.createObjectBuilder().add("min", this.min);
    if (this.max == Integer.MAX_VALUE) {
      jsonMultiplicity.addNull("max");
    } else {
      jsonMultiplicity.add("max", this.max);
    }
    return jsonMultiplicity.build();
  }

  /**
   * Creates a new {@code Multiplicity} from a {@link jakarta.json.JsonObject}.
   *
   * @param jsonMultiplicity the {@link jakarta.json.JsonObject}
   * @return a new {@code Multiplicity}
   * @throws java.lang.NullPointerException if {@code jsonMultiplicity} is {@code null}
   * @since 1.0.0
   */
  public static Multiplicity fromJson(JsonObject jsonMultiplicity) {
    requireNonNull(jsonMultiplicity);
    int min = jsonMultiplicity.getInt("min");
    int max = jsonMultiplicity.isNull("max") ? Integer.MAX_VALUE : jsonMultiplicity.getInt("max");
    if (min == 0 && max == 1) {
      return ZERO_OR_ONE;
    }
    if (min == 0 && max == Integer.MAX_VALUE) {
      return ZERO_OR_MORE;
    }
    if (min == 1 && max == 1) {
      return ONE;
    }
    if (min == 1 && max == Integer.MAX_VALUE) {
      return ONE_OR_MORE;
    }
    throw new RuntimeException(
        String.format("Invalid multiplicity {min = %d, max = %d}", min, max));
  }
}
