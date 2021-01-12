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

import java.util.LinkedHashMap;
import java.util.Map;

/** Enum representing the type of an attack step in a MAL language. */
public enum AttackStepType {
  /** Enum constant representing AND-attack steps, {@code "&"}. */
  AND("and"),

  /** Enum constant representing OR-attack steps, {@code "|"}. */
  OR("or"),

  /** Enum constant representing defenses, {@code "#"}. */
  DEFENSE("defense"),

  /** Enum constant representing exist steps, {@code "E"}. */
  EXIST("exist"),

  /** Enum constant representing not-exist steps, {@code "!E"}. */
  NOT_EXIST("notExist");

  private static final Map<String, AttackStepType> TYPE_MAP = new LinkedHashMap<>();

  static {
    for (var type : values()) {
      TYPE_MAP.put(type.name, type);
    }
  }

  private final String name;

  AttackStepType(String name) {
    this.name = name;
  }

  /**
   * Returns the name of this {@code AttackStepType} object.
   *
   * @return the name of this {@code AttackStepType} object
   */
  @Override
  public String toString() {
    return name;
  }

  /**
   * Returns the attack step type with the name {@code name}.
   *
   * @param name the name of the attack step type
   * @return the attack step type with the name {@code name}
   * @throws NullPointerException if {@code name} is {@code null}
   * @throws IllegalArgumentException if {@code name} is not the name of an attack step type
   */
  public static AttackStepType fromString(String name) {
    checkNotNull(name);
    if (!TYPE_MAP.containsKey(name)) {
      throw new IllegalArgumentException(String.format("Invalid attack step type \"%s\"", name));
    }
    return TYPE_MAP.get(name);
  }
}
