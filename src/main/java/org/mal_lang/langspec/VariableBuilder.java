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

import static org.mal_lang.langspec.Utils.checkNotNull;

import jakarta.json.JsonObject;

/** A builder for creating {@link Variable} objects. */
public final class VariableBuilder {
  private final String name;

  VariableBuilder(String name) {
    this.name = name;
  }

  /**
   * Returns the name of this {@code VariableBuilder} object.
   *
   * @return the name of this {@code VariableBuilder} object
   */
  public String getName() {
    return name;
  }

  /**
   * Creates a new {@link Variable} object.
   *
   * @param asset the built asset
   * @return a new {@link Variable} object
   * @throws NullPointerException if {@code asset} is {@code null}
   */
  public Variable build(Asset asset) {
    checkNotNull(asset);
    return new Variable(name, asset);
  }

  static VariableBuilder fromJson(JsonObject jsonVariable) {
    return Variable.builder(jsonVariable.getString("name"));
  }
}
