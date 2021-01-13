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
import java.util.Map;

/** A builder for creating {@link Association} objects. */
public final class AssociationBuilder {
  private final String name;
  private MetaBuilder meta = Meta.builder();
  private final String leftAsset;
  private final String leftField;
  private final Multiplicity leftMultiplicity;
  private final String rightAsset;
  private final String rightField;
  private final Multiplicity rightMultiplicity;

  AssociationBuilder(
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
   * Returns the name of this {@code AssociationBuilder} object.
   *
   * @return the name of this {@code AssociationBuilder} object
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the meta info of this {@code AssociationBuilder} object.
   *
   * @return the meta info of this {@code AssociationBuilder} object
   */
  public MetaBuilder getMeta() {
    return meta;
  }

  /**
   * Sets the meta info of this {@code AssociationBuilder} object.
   *
   * @param meta the meta info to set
   * @return this {@code AssociationBuilder} object
   * @throws NullPointerException if {@code meta} is {@code null}
   */
  public AssociationBuilder setMeta(MetaBuilder meta) {
    checkNotNull(meta);
    this.meta = meta;
    return this;
  }

  /**
   * Returns the left asset of this {@code AssociationBuilder} object.
   *
   * @return the left asset of this {@code AssociationBuilder} object
   */
  public String getLeftAsset() {
    return leftAsset;
  }

  /**
   * Returns the left field of this {@code AssociationBuilder} object.
   *
   * @return the left field of this {@code AssociationBuilder} object
   */
  public String getLeftField() {
    return leftField;
  }

  /**
   * Returns the left multiplicity of this {@code AssociationBuilder} object.
   *
   * @return the left multiplicity of this {@code AssociationBuilder} object
   */
  public Multiplicity getLeftMultiplicity() {
    return leftMultiplicity;
  }

  /**
   * Returns the right asset of this {@code AssociationBuilder} object.
   *
   * @return the right asset of this {@code AssociationBuilder} object
   */
  public String getRightAsset() {
    return rightAsset;
  }

  /**
   * Returns the right field of this {@code AssociationBuilder} object.
   *
   * @return the right field of this {@code AssociationBuilder} object
   */
  public String getRightField() {
    return rightField;
  }

  /**
   * Returns the right multiplicity of this {@code AssociationBuilder} object.
   *
   * @return the right multiplicity of this {@code AssociationBuilder} object
   */
  public Multiplicity getRightMultiplicity() {
    return rightMultiplicity;
  }

  /**
   * Creates a new {@link Association} object.
   *
   * @param assets a map of all assets in the language
   * @return a new {@link Association} object
   * @throws NullPointerException if {@code assets} is {@code null}
   * @throws IllegalArgumentException if {@code assets} does not contain the left asset or the right
   *     asset of this {@code AssociationBuilder} object
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

  static AssociationBuilder fromJson(JsonObject jsonAssociation) {
    return Association.builder(
            jsonAssociation.getString("name"),
            jsonAssociation.getString("leftAsset"),
            jsonAssociation.getString("leftField"),
            Multiplicity.fromJson(jsonAssociation.getJsonObject("leftMultiplicity")),
            jsonAssociation.getString("rightAsset"),
            jsonAssociation.getString("rightField"),
            Multiplicity.fromJson(jsonAssociation.getJsonObject("rightMultiplicity")))
        .setMeta(MetaBuilder.fromJson(jsonAssociation.getJsonObject("meta")));
  }
}
