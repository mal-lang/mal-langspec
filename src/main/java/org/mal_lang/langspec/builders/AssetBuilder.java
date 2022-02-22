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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A builder for creating {@link org.mal_lang.langspec.Asset} objects.
 *
 * @since 1.0.0
 */
public final class AssetBuilder {
  private final String name;
  private final MetaBuilder meta = new MetaBuilder();
  private final String category;
  private final boolean isAbstract;
  private final String superAsset;
  private final Map<String, VariableBuilder> variables = new LinkedHashMap<>();
  private final Map<String, AttackStepBuilder> attackSteps = new LinkedHashMap<>();
  private byte[] svgIcon = null;
  private byte[] pngIcon = null;

  /**
   * Constructs a new {@code AssetBuilder} object.
   *
   * @param name the name of the asset
   * @param category the category of the asset
   * @param isAbstract whether the asset is abstract
   * @param superAsset the super asset of the asset, or {@code null}
   * @throws java.lang.NullPointerException if {@code name} or {@code category} is {@code null}
   * @throws java.lang.IllegalArgumentException if {@code name} or {@code category} is not a valid
   *     identifier
   * @since 1.0.0
   */
  public AssetBuilder(String name, String category, boolean isAbstract, String superAsset) {
    this.name = requireIdentifier(name);
    this.category = requireIdentifier(category);
    this.isAbstract = isAbstract;
    this.superAsset = superAsset;
  }

  /**
   * Returns the name of this {@code AssetBuilder} object.
   *
   * @return the name of this {@code AssetBuilder} object
   * @since 1.0.0
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the meta info of this {@code AssetBuilder} object.
   *
   * @return the meta info of this {@code AssetBuilder} object
   * @since 1.0.0
   */
  public MetaBuilder getMeta() {
    return this.meta;
  }

  /**
   * Returns the category of this {@code AssetBuilder} object.
   *
   * @return the category of this {@code AssetBuilder} object
   * @since 1.0.0
   */
  public String getCategory() {
    return this.category;
  }

  /**
   * Returns whether this {@code AssetBuilder} object is abstract.
   *
   * @return whether this {@code AssetBuilder} object is abstract
   * @since 1.0.0
   */
  public boolean isAbstract() {
    return this.isAbstract;
  }

  /**
   * Returns the super asset of this {@code AssetBuilder} object, or {@code null} if no super asset
   * has been set.
   *
   * @return the super asset of this {@code AssetBuilder} object, or {@code null} if no super asset
   *     has been set
   * @since 1.0.0
   */
  public String getSuperAsset() {
    return this.superAsset;
  }

  /**
   * Returns a list of all variables in this {@code AssetBuilder} object.
   *
   * @return a list of all variables in this {@code AssetBuilder} object
   * @since 1.0.0
   */
  public List<VariableBuilder> getVariables() {
    return List.copyOf(this.variables.values());
  }

  /**
   * Adds a variable to this {@code AssetBuilder} object.
   *
   * @param variable the variable to add
   * @return this {@code AssetBuilder} object
   * @throws java.lang.NullPointerException if {@code variable} is {@code null}
   * @since 1.0.0
   */
  public AssetBuilder addVariable(VariableBuilder variable) {
    requireNonNull(variable);
    this.variables.put(variable.getName(), variable);
    return this;
  }

  /**
   * Returns a list of all attack steps in this {@code AssetBuilder} object.
   *
   * @return a list of all attack steps in this {@code AssetBuilder} object
   * @since 1.0.0
   */
  public List<AttackStepBuilder> getAttackSteps() {
    return List.copyOf(this.attackSteps.values());
  }

  /**
   * Adds an attack step to this {@code AssetBuilder} object.
   *
   * @param attackStep the attack step to add
   * @return this {@code AssetBuilder} object
   * @throws java.lang.NullPointerException if {@code attackStep} is {@code null}
   * @since 1.0.0
   */
  public AssetBuilder addAttackStep(AttackStepBuilder attackStep) {
    requireNonNull(attackStep);
    this.attackSteps.put(attackStep.getName(), attackStep);
    return this;
  }

  /**
   * Returns the SVG icon of this {@code AssetBuilder} object, or {@code null} if no SVG icon has
   * been set.
   *
   * @return the SVG icon of this {@code AssetBuilder} object, or {@code null} if no SVG icon has
   *     been set
   * @since 1.0.0
   */
  public byte[] getSvgIcon() {
    return this.svgIcon == null ? null : this.svgIcon.clone();
  }

  /**
   * Sets the SVG icon of this {@code AssetBuilder} object.
   *
   * @param svgIcon the SVG icon to set
   * @return this {@code AssetBuilder} object
   * @throws java.lang.NullPointerException if {@code svgIcon} is {@code null}
   * @since 1.0.0
   */
  public AssetBuilder setSvgIcon(byte[] svgIcon) {
    this.svgIcon = requireNonNull(svgIcon).clone();
    return this;
  }

  /**
   * Returns the PNG icon of this {@code AssetBuilder} object, or {@code null} if no PNG icon has
   * been set.
   *
   * @return the PNG icon of this {@code AssetBuilder} object, or {@code null} if no PNG icon has
   *     been set
   * @since 1.0.0
   */
  public byte[] getPngIcon() {
    return this.pngIcon == null ? null : this.pngIcon.clone();
  }

  /**
   * Sets the PNG icon of this {@code AssetBuilder} object.
   *
   * @param pngIcon the PNG icon to set
   * @return this {@code AssetBuilder} object
   * @throws java.lang.NullPointerException if {@code pngIcon} is {@code null}
   * @since 1.0.0
   */
  public AssetBuilder setPngIcon(byte[] pngIcon) {
    this.pngIcon = requireNonNull(pngIcon).clone();
    return this;
  }

  /**
   * Creates a new {@code AssetBuilder} from a {@link jakarta.json.JsonObject}.
   *
   * @param jsonAsset the {@link jakarta.json.JsonObject}
   * @param svgIcons the SVG icons of the language
   * @param pngIcons the PNG icons of the language
   * @return a new {@code AssetBuilder} from a {@link jakarta.json.JsonObject}
   * @throws java.lang.NullPointerException if {@code jsonAsset}, {@code svgIcons}, or {@code
   *     pngIcons} is {@code null}
   * @since 1.0.0
   */
  public static AssetBuilder fromJson(
      JsonObject jsonAsset, Map<String, byte[]> svgIcons, Map<String, byte[]> pngIcons) {
    requireNonNull(jsonAsset);
    requireNonNull(svgIcons);
    requireNonNull(pngIcons);
    var builder =
        new AssetBuilder(
            jsonAsset.getString("name"),
            jsonAsset.getString("category"),
            jsonAsset.getBoolean("isAbstract"),
            jsonAsset.isNull("superAsset") ? null : jsonAsset.getString("superAsset"));
    builder.getMeta().fromJson(jsonAsset.getJsonObject("meta"));
    for (var jsonVariable : jsonAsset.getJsonArray("variables")) {
      builder.addVariable(VariableBuilder.fromJson(jsonVariable.asJsonObject()));
    }
    for (var jsonAttackStep : jsonAsset.getJsonArray("attackSteps")) {
      builder.addAttackStep(AttackStepBuilder.fromJson(jsonAttackStep.asJsonObject()));
    }
    if (svgIcons.containsKey(builder.getName())) {
      builder.setSvgIcon(svgIcons.get(builder.getName()));
    }
    if (pngIcons.containsKey(builder.getName())) {
      builder.setPngIcon(pngIcons.get(builder.getName()));
    }
    return builder;
  }
}
