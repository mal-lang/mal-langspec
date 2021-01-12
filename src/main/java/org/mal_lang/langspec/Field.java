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

/** Immutable class representing a field of an asset in a MAL language. */
public final class Field {
  private final String name;
  private final Asset asset;
  private Association association;
  private Field target;
  private final Multiplicity multiplicity;

  Field(String name, Asset asset, Multiplicity multiplicity) {
    this.name = name;
    this.asset = asset;
    this.multiplicity = multiplicity;
    asset.addField(this);
  }

  /**
   * Returns the name of this {@code Field} object.
   *
   * @return the name of this {@code Field} object
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the asset of this {@code Field} object.
   *
   * @return the asset of this {@code Field} object
   */
  public Asset getAsset() {
    return asset;
  }

  void setAssociation(Association association) {
    this.association = association;
  }

  /**
   * Returns the association of this {@code Field} object.
   *
   * @return the association of this {@code Field} object
   */
  public Association getAssociation() {
    return association;
  }

  void setTarget(Field target) {
    this.target = target;
  }

  /**
   * Returns the target of this {@code Field} object.
   *
   * @return the target of this {@code Field} object
   */
  public Field getTarget() {
    return target;
  }

  /**
   * Returns the multiplicity of this {@code Field} object.
   *
   * @return the multiplicity of this {@code Field} object
   */
  public Multiplicity getMultiplicity() {
    return multiplicity;
  }
}
