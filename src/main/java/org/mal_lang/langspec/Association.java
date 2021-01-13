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

/** Immutable class representing an association in a MAL language. */
public final class Association {
  private final String name;
  private final Meta meta;
  private final Field leftField;
  private final Field rightField;

  Association(String name, Meta meta, Field leftField, Field rightField) {
    this.name = name;
    this.meta = meta;
    this.leftField = leftField;
    this.rightField = rightField;
    leftField.setAssociation(this);
    rightField.setAssociation(this);
    leftField.setTarget(rightField);
    rightField.setTarget(leftField);
  }

  /**
   * Returns the name of this {@code Association} object.
   *
   * @return the name of this {@code Association} object
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the meta info of this {@code Association} object.
   *
   * @return the meta info of this {@code Association} object
   */
  public Meta getMeta() {
    return meta;
  }

  /**
   * Returns the left field of this {@code Association} object.
   *
   * @return the left field of this {@code Association} object
   */
  public Field getLeftField() {
    return leftField;
  }

  /**
   * Returns the right field of this {@code Association} object.
   *
   * @return the right field of this {@code Association} object
   */
  public Field getRightField() {
    return rightField;
  }

  /**
   * Returns the JSON representation of this {@code Association} object.
   *
   * @return the JSON representation of this {@code Association} object
   */
  public JsonObject toJson() {
    return Json.createObjectBuilder()
        .add("name", name)
        .add("meta", meta.toJson())
        .add("leftAsset", leftField.getAsset().getName())
        .add("leftField", leftField.getName())
        .add("leftMultiplicity", leftField.getMultiplicity().toJson())
        .add("rightAsset", rightField.getAsset().getName())
        .add("rightField", rightField.getName())
        .add("rightMultiplicity", rightField.getMultiplicity().toJson())
        .build();
  }

  /**
   * Creates a new {@link AssociationBuilder} object.
   *
   * @param name the name of the category
   * @param leftAsset the left asset
   * @param leftField the left field
   * @param leftMultiplicity the left multiplicity
   * @param rightAsset the right asset
   * @param rightField the right field
   * @param rightMultiplicity the right multiplicity
   * @return a new {@link AssociationBuilder} object
   * @throws NullPointerException if {@code name}, {@code leftAsset}, {@code leftField}, {@code
   *     leftMultiplicity}, {@code rightAsset}, {@code rightField}, or {@code rightMultiplicity} is
   *     {@code null}
   * @throws IllegalArgumentException if {@code name}, {@code leftAsset}, {@code leftField}, {@code
   *     rightAsset}, or {@code rightField} is not a valid identifier
   */
  public static AssociationBuilder builder(
      String name,
      String leftAsset,
      String leftField,
      Multiplicity leftMultiplicity,
      String rightAsset,
      String rightField,
      Multiplicity rightMultiplicity) {
    checkNotNull(leftMultiplicity, rightMultiplicity);
    checkIdentifier(name, leftAsset, leftField, rightAsset, rightField);
    return new AssociationBuilder(
        name, leftAsset, leftField, leftMultiplicity, rightAsset, rightField, rightMultiplicity);
  }
}
