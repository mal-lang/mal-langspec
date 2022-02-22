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
import java.util.Map;
import org.mal_lang.langspec.builders.AssociationBuilder;

/**
 * Immutable class representing an association in a MAL language.
 *
 * @since 1.0.0
 */
public final class Association {
  private final String name;
  private final Meta meta;
  private final Field leftField;
  private final Field rightField;

  private Association(String name, Meta meta, Field leftField, Field rightField) {
    this.name = requireNonNull(name);
    this.meta = requireNonNull(meta);
    this.leftField = requireNonNull(leftField);
    this.rightField = requireNonNull(rightField);
    leftField.setAssociation(this);
    rightField.setAssociation(this);
  }

  /**
   * Returns the name of this {@code Association} object.
   *
   * @return the name of this {@code Association} object
   * @since 1.0.0
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the meta info of this {@code Association} object.
   *
   * @return the meta info of this {@code Association} object
   * @since 1.0.0
   */
  public Meta getMeta() {
    return this.meta;
  }

  /**
   * Returns the left field of this {@code Association} object.
   *
   * @return the left field of this {@code Association} object
   * @since 1.0.0
   */
  public Field getLeftField() {
    return this.leftField;
  }

  /**
   * Returns the right field of this {@code Association} object.
   *
   * @return the right field of this {@code Association} object
   * @since 1.0.0
   */
  public Field getRightField() {
    return this.rightField;
  }

  JsonObject toJson() {
    return Json.createObjectBuilder()
        .add("name", this.name)
        .add("meta", this.meta.toJson())
        .add("leftAsset", this.rightField.getAsset().getName())
        .add("leftField", this.leftField.getName())
        .add("leftMultiplicity", this.leftField.getMultiplicity().toJson())
        .add("rightAsset", this.leftField.getAsset().getName())
        .add("rightField", this.rightField.getName())
        .add("rightMultiplicity", this.rightField.getMultiplicity().toJson())
        .build();
  }

  static Association fromBuilder(AssociationBuilder builder, Map<String, Asset> assets) {
    requireNonNull(builder);
    requireNonNull(assets);
    if (!assets.containsKey(builder.getLeftAsset())) {
      throw new IllegalArgumentException(
          String.format("Asset \"%s\" not found", builder.getLeftAsset()));
    }
    if (!assets.containsKey(builder.getRightAsset())) {
      throw new IllegalArgumentException(
          String.format("Asset \"%s\" not found", builder.getRightAsset()));
    }
    var leftField =
        new Field(
            builder.getLeftField(),
            assets.get(builder.getRightAsset()),
            builder.getLeftMultiplicity());
    var rightField =
        new Field(
            builder.getRightField(),
            assets.get(builder.getLeftAsset()),
            builder.getRightMultiplicity());
    leftField.setTarget(rightField);
    rightField.setTarget(leftField);
    return new Association(
        builder.getName(), Meta.fromBuilder(builder.getMeta()), leftField, rightField);
  }
}
