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
import java.util.Map;

/** Immutable class representing an association in a MAL language. */
public final class Association {
  private final String name;
  private final Meta meta;
  private final Field leftField;
  private final Field rightField;

  private Association(String name, Meta meta, Field leftField, Field rightField) {
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

  /** A builder for creating {@link Association} objects. */
  public static final class Builder {
    private final String name;
    private Meta.Builder meta = Meta.builder();
    private final String leftAsset;
    private final String leftField;
    private final Multiplicity leftMultiplicity;
    private final String rightAsset;
    private final String rightField;
    private final Multiplicity rightMultiplicity;

    private Builder(
        String name,
        String leftAsset,
        String leftField,
        Multiplicity leftMultiplicity,
        String rightAsset,
        String rightField,
        Multiplicity rightMultiplicity) {
      this.name = name;
      this.leftAsset = leftAsset;
      this.leftField = leftField;
      this.leftMultiplicity = leftMultiplicity;
      this.rightAsset = rightAsset;
      this.rightField = rightField;
      this.rightMultiplicity = rightMultiplicity;
    }

    /**
     * Returns the name of this {@code Association.Builder} object.
     *
     * @return the name of this {@code Association.Builder} object
     */
    public String getName() {
      return name;
    }

    /**
     * Returns the meta info of this {@code Association.Builder} object.
     *
     * @return the meta info of this {@code Association.Builder} object
     */
    public Meta.Builder getMeta() {
      return meta;
    }

    /**
     * Sets the meta info of this {@code Association.Builder} object.
     *
     * @param meta the meta info to set
     * @return this {@code Association.Builder} object
     * @throws NullPointerException if {@code meta} is {@code null}
     */
    public Builder setMeta(Meta.Builder meta) {
      checkNotNull(meta);
      this.meta = meta;
      return this;
    }

    /**
     * Creates a new {@link Association} object.
     *
     * @param assets a map of all assets in the language
     * @return a new {@link Association} object
     * @throws NullPointerException if {@code assets} is {@code null}
     * @throws IllegalArgumentException if {@code assets} does not contain the left asset or the
     *     right asset of this {@code Association.Builder} object
     */
    public Association build(Map<String, Asset> assets) {
      checkNotNull(assets);
      if (!assets.containsKey(leftAsset)) {
        throw new IllegalArgumentException(String.format("Asset \"%s\" not found", leftAsset));
      }
      if (!assets.containsKey(rightAsset)) {
        throw new IllegalArgumentException(String.format("Asset \"%s\" not found", rightAsset));
      }
      var leftFieldObject = new Field(leftField, assets.get(leftAsset), leftMultiplicity);
      var rightFieldObject = new Field(rightField, assets.get(rightAsset), rightMultiplicity);
      return new Association(name, meta.build(), leftFieldObject, rightFieldObject);
    }

    static Builder fromJson(JsonObject jsonAssociation) {
      return Association.builder(
              jsonAssociation.getString("name"),
              jsonAssociation.getString("leftAsset"),
              jsonAssociation.getString("leftField"),
              Multiplicity.fromJson(jsonAssociation.getJsonObject("leftMultiplicity")),
              jsonAssociation.getString("rightAsset"),
              jsonAssociation.getString("rightField"),
              Multiplicity.fromJson(jsonAssociation.getJsonObject("rightMultiplicity")))
          .setMeta(Meta.Builder.fromJson(jsonAssociation.getJsonObject("meta")));
    }
  }

  /**
   * Creates a new {@link Builder} object.
   *
   * @param name the name of the category
   * @param leftAsset the left asset
   * @param leftField the left field
   * @param leftMultiplicity the left multiplicity
   * @param rightAsset the right asset
   * @param rightField the right field
   * @param rightMultiplicity the right multiplicity
   * @return a new {@link Builder} object
   * @throws NullPointerException if {@code name}, {@code leftAsset}, {@code leftField}, {@code
   *     leftMultiplicity}, {@code rightAsset}, {@code rightField}, or {@code rightMultiplicity} is
   *     {@code null}
   * @throws IllegalArgumentException if {@code name}, {@code leftAsset}, {@code leftField}, {@code
   *     rightAsset}, or {@code rightField} is not a valid identifier
   */
  public static Builder builder(
      String name,
      String leftAsset,
      String leftField,
      Multiplicity leftMultiplicity,
      String rightAsset,
      String rightField,
      Multiplicity rightMultiplicity) {
    checkNotNull(leftMultiplicity, rightMultiplicity);
    checkIdentifier(name, leftAsset, leftField, rightAsset, rightField);
    return new Builder(
        name, leftAsset, leftField, leftMultiplicity, rightAsset, rightField, rightMultiplicity);
  }
}
