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

package org.mal_lang.langspec.builders;

import static java.util.Objects.requireNonNull;
import static org.mal_lang.langspec.Utils.requireIdentifier;

import jakarta.json.JsonObject;
import org.mal_lang.langspec.Multiplicity;

/**
 * A builder for creating {@link org.mal_lang.langspec.Association} objects.
 *
 * @since 1.0.0
 */
public final class AssociationBuilder {
  private final String name;
  private final MetaBuilder meta = new MetaBuilder();
  private final String leftAsset;
  private final String leftField;
  private final Multiplicity leftMultiplicity;
  private final String rightAsset;
  private final String rightField;
  private final Multiplicity rightMultiplicity;

  /**
   * Constructs a new {@code AssociationBuilder} object.
   *
   * @param name the name of the association
   * @param leftAsset the left asset of the association
   * @param leftField the left field of the association
   * @param leftMultiplicity the left multiplicity of the association
   * @param rightAsset the right asset of the association
   * @param rightField the right field of the association
   * @param rightMultiplicity the right multiplicity of the association
   * @throws java.lang.NullPointerException if {@code name}, {@code leftAsset}, {@code leftField},
   *     {@code leftMultiplicity}, {@code rightAsset}, {@code rightField}, or {@code
   *     rightMultiplicity} is {@code null}
   * @throws java.lang.IllegalArgumentException if {@code name}, {@code leftAsset}, {@code
   *     leftField}, {@code rightAsset}, or {@code rightField} is not a valid identifier
   * @since 1.0.0
   */
  public AssociationBuilder(
      String name,
      String leftAsset,
      String leftField,
      Multiplicity leftMultiplicity,
      String rightAsset,
      String rightField,
      Multiplicity rightMultiplicity) {
    this.name = requireIdentifier(name);
    this.leftAsset = requireIdentifier(leftAsset);
    this.leftField = requireIdentifier(leftField);
    this.leftMultiplicity = requireNonNull(leftMultiplicity);
    this.rightAsset = requireIdentifier(rightAsset);
    this.rightField = requireIdentifier(rightField);
    this.rightMultiplicity = requireNonNull(rightMultiplicity);
  }

  /**
   * Returns the name of this {@code AssociationBuilder} object.
   *
   * @return the name of this {@code AssociationBuilder} object
   * @since 1.0.0
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the meta info of this {@code AssociationBuilder} object.
   *
   * @return the meta info of this {@code AssociationBuilder} object
   * @since 1.0.0
   */
  public MetaBuilder getMeta() {
    return this.meta;
  }

  /**
   * Returns the left asset of this {@code AssociationBuilder} object.
   *
   * @return the left asset of this {@code AssociationBuilder} object
   * @since 1.0.0
   */
  public String getLeftAsset() {
    return this.leftAsset;
  }

  /**
   * Returns the left field of this {@code AssociationBuilder} object.
   *
   * @return the left field of this {@code AssociationBuilder} object
   * @since 1.0.0
   */
  public String getLeftField() {
    return this.leftField;
  }

  /**
   * Returns the left multiplicity of this {@code AssociationBuilder} object.
   *
   * @return the left multiplicity of this {@code AssociationBuilder} object
   * @since 1.0.0
   */
  public Multiplicity getLeftMultiplicity() {
    return this.leftMultiplicity;
  }

  /**
   * Returns the right asset of this {@code AssociationBuilder} object.
   *
   * @return the right asset of this {@code AssociationBuilder} object
   * @since 1.0.0
   */
  public String getRightAsset() {
    return this.rightAsset;
  }

  /**
   * Returns the right field of this {@code AssociationBuilder} object.
   *
   * @return the right field of this {@code AssociationBuilder} object
   * @since 1.0.0
   */
  public String getRightField() {
    return this.rightField;
  }

  /**
   * Returns the right multiplicity of this {@code AssociationBuilder} object.
   *
   * @return the right multiplicity of this {@code AssociationBuilder} object
   * @since 1.0.0
   */
  public Multiplicity getRightMultiplicity() {
    return this.rightMultiplicity;
  }

  /**
   * Creates a new {@code AssociationBuilder} from a {@link jakarta.json.JsonObject}.
   *
   * @param jsonAssociation the {@link jakarta.json.JsonObject}
   * @return a new {@code AssociationBuilder}
   * @throws java.lang.NullPointerException if {@code jsonAssociation} is {@code null}
   * @since 1.0.0
   */
  public static AssociationBuilder fromJson(JsonObject jsonAssociation) {
    requireNonNull(jsonAssociation);
    var builder =
        new AssociationBuilder(
            jsonAssociation.getString("name"),
            jsonAssociation.getString("leftAsset"),
            jsonAssociation.getString("leftField"),
            Multiplicity.fromJson(jsonAssociation.getJsonObject("leftMultiplicity")),
            jsonAssociation.getString("rightAsset"),
            jsonAssociation.getString("rightField"),
            Multiplicity.fromJson(jsonAssociation.getJsonObject("rightMultiplicity")));
    builder.getMeta().fromJson(jsonAssociation.getJsonObject("meta"));
    return builder;
  }
}
