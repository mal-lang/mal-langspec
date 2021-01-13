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

import jakarta.json.Json;
import jakarta.json.JsonObject;

/** Immutable class representing a variable of an asset in a MAL language. */
public final class Variable {
  private final String name;
  private final Asset asset;

  Variable(String name, Asset asset) {
    this.name = name;
    this.asset = asset;
    asset.addVariable(this);
  }

  /**
   * Returns the name of this {@code Variable} object.
   *
   * @return the name of this {@code Variable} object
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the asset of this {@code Variable} object.
   *
   * @return the asset of this {@code Variable} object
   */
  public Asset getAsset() {
    return asset;
  }

  /**
   * Returns the JSON representation of this {@code Variable} object.
   *
   * @return the JSON representation of this {@code Variable} object
   */
  public JsonObject toJson() {
    return Json.createObjectBuilder().add("name", name).build();
  }

  /**
   * Creates a new {@link VariableBuilder} object.
   *
   * @param name the name of the variable
   * @return a new {@link VariableBuilder} object
   * @throws NullPointerException if {@code name} is {@code null}
   * @throws IllegalArgumentException if {@code name} is not a valid identifier
   */
  public static VariableBuilder builder(String name) {
    checkIdentifier(name);
    return new VariableBuilder(name);
  }
}
