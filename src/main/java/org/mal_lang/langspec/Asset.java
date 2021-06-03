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

import static java.util.Objects.requireNonNull;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.mal_lang.langspec.builders.AssetBuilder;

/**
 * Immutable class representing an asset in a MAL language.
 *
 * @since 1.0.0
 */
public final class Asset {
  private final String name;
  private final Meta meta;
  private final Category category;
  private final boolean isAbstract;
  private Asset superAsset;
  private final Map<String, Field> fields = new LinkedHashMap<>();
  private final Map<String, Variable> variables = new LinkedHashMap<>();
  private final Map<String, AttackStep> attackSteps = new LinkedHashMap<>();
  private final byte[] svgIcon;
  private final byte[] pngIcon;

  private Asset(
      String name,
      Meta meta,
      Category category,
      boolean isAbstract,
      byte[] svgIcon,
      byte[] pngIcon) {
    this.name = requireNonNull(name);
    this.meta = requireNonNull(meta);
    this.category = requireNonNull(category);
    this.isAbstract = isAbstract;
    this.svgIcon = svgIcon == null ? null : svgIcon.clone();
    this.pngIcon = pngIcon == null ? null : pngIcon.clone();
    category.addAsset(this);
  }

  /**
   * Returns the name of this {@code Asset} object.
   *
   * @return the name of this {@code Asset} object
   * @since 1.0.0
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the meta info of this {@code Asset} object.
   *
   * @return the meta info of this {@code Asset} object
   * @since 1.0.0
   */
  public Meta getMeta() {
    return this.meta;
  }

  /**
   * Returns the category of this {@code Asset} object.
   *
   * @return the category of this {@code Asset} object
   * @since 1.0.0
   */
  public Category getCategory() {
    return this.category;
  }

  /**
   * Returns whether this {@code Asset} object is abstract.
   *
   * @return whether this {@code Asset} object is abstract
   * @since 1.0.0
   */
  public boolean isAbstract() {
    return this.isAbstract;
  }

  /**
   * Returns whether this {@code Asset} object has a super asset.
   *
   * @return whether this {@code Asset} object has a super asset
   * @since 1.0.0
   */
  public boolean hasSuperAsset() {
    return this.superAsset != null;
  }

  /**
   * Returns the super asset of this {@code Asset} object.
   *
   * @return the super asset of this {@code Asset} object
   * @throws java.lang.UnsupportedOperationException if this {@code Asset} object does not have a
   *     super asset
   * @since 1.0.0
   */
  public Asset getSuperAsset() {
    if (!this.hasSuperAsset()) {
      throw new UnsupportedOperationException("Super asset not found");
    }
    return this.superAsset;
  }

  void setSuperAsset(Asset superAsset) {
    this.superAsset = requireNonNull(superAsset);
  }

  /**
   * Returns whether {@code name} is the name of a local field in this {@code Asset} object.
   *
   * @param name the name of the local field
   * @return whether {@code name} is the name of a local field in this {@code Asset} object
   * @throws java.lang.NullPointerException if {@code name} is {@code null}
   * @since 1.0.0
   */
  public boolean hasLocalField(String name) {
    return this.fields.containsKey(requireNonNull(name));
  }

  /**
   * Returns the local field with the name {@code name} in this {@code Asset} object.
   *
   * @param name the name of the local field
   * @return the local field with the name {@code name} in this {@code Asset} object
   * @throws java.lang.NullPointerException if {@code name} is {@code null}
   * @throws java.lang.IllegalArgumentException if {@code name} is not the name of a local field in
   *     this {@code Asset} object
   * @since 1.0.0
   */
  public Field getLocalField(String name) {
    if (!this.hasLocalField(name)) {
      throw new IllegalArgumentException(String.format("Local field \"%s\" not found", name));
    }
    return this.fields.get(name);
  }

  /**
   * Returns a list of all local fields in this {@code Asset} object.
   *
   * @return a list of all local fields in this {@code Asset} object
   * @since 1.0.0
   */
  public List<Field> getLocalFields() {
    return List.copyOf(this.fields.values());
  }

  /**
   * Returns whether {@code name} is the name of a field in this {@code Asset} object.
   *
   * @param name the name of the field
   * @return whether {@code name} is the name of a field in this {@code Asset} object
   * @throws java.lang.NullPointerException if {@code name} is {@code null}
   * @since 1.0.0
   */
  public boolean hasField(String name) {
    return this.hasLocalField(name) || this.hasSuperAsset() && this.getSuperAsset().hasField(name);
  }

  /**
   * Returns the field with the name {@code name} in this {@code Asset} object.
   *
   * @param name the name of the field
   * @return the field with the name {@code name} in this {@code Asset} object
   * @throws java.lang.NullPointerException if {@code name} is {@code null}
   * @throws java.lang.IllegalArgumentException if {@code name} is not the name of a field in this
   *     {@code Asset} object
   * @since 1.0.0
   */
  public Field getField(String name) {
    if (!this.hasField(name)) {
      throw new IllegalArgumentException(String.format("Field \"%s\" not found", name));
    }
    return this.hasLocalField(name)
        ? this.getLocalField(name)
        : this.getSuperAsset().getField(name);
  }

  /**
   * Returns a list of all fields in this {@code Asset} object.
   *
   * @return a list of all fields in this {@code Asset} object
   * @since 1.0.0
   */
  public List<Field> getFields() {
    return List.copyOf(this.getFieldsMap().values());
  }

  void addField(Field field) {
    requireNonNull(field);
    this.fields.put(field.getName(), field);
  }

  private Map<String, Field> getFieldsMap() {
    var fieldsMap =
        this.hasSuperAsset()
            ? this.getSuperAsset().getFieldsMap()
            : new LinkedHashMap<String, Field>();
    fieldsMap.putAll(this.fields);
    return fieldsMap;
  }

  /**
   * Returns whether {@code name} is the name of a local variable in this {@code Asset} object.
   *
   * @param name the name of the local variable
   * @return whether {@code name} is the name of a local variable in this {@code Asset} object
   * @throws java.lang.NullPointerException if {@code name} is {@code null}
   * @since 1.0.0
   */
  public boolean hasLocalVariable(String name) {
    return this.variables.containsKey(requireNonNull(name));
  }

  /**
   * Returns the local variable with the name {@code name} in this {@code Asset} object.
   *
   * @param name the name of the local variable
   * @return the local variable with the name {@code name} in this {@code Asset} object
   * @throws java.lang.NullPointerException if {@code name} is {@code null}
   * @throws java.lang.IllegalArgumentException if {@code name} is not the name of a local variable
   *     in this {@code Asset} object
   * @since 1.0.0
   */
  public Variable getLocalVariable(String name) {
    if (!this.hasLocalVariable(name)) {
      throw new IllegalArgumentException(String.format("Local variable \"%s\" not found", name));
    }
    return this.variables.get(name);
  }

  /**
   * Returns a list of all local variables in this {@code Asset} object.
   *
   * @return a list of all local variables in this {@code Asset} object
   * @since 1.0.0
   */
  public List<Variable> getLocalVariables() {
    return List.copyOf(this.variables.values());
  }

  /**
   * Returns whether {@code name} is the name of a variable in this {@code Asset} object.
   *
   * @param name the name of the variable
   * @return whether {@code name} is the name of a variable in this {@code Asset} object
   * @throws java.lang.NullPointerException if {@code name} is {@code null}
   * @since 1.0.0
   */
  public boolean hasVariable(String name) {
    return this.hasLocalVariable(name)
        || this.hasSuperAsset() && this.getSuperAsset().hasVariable(name);
  }

  /**
   * Returns the variable with the name {@code name} in this {@code Asset} object.
   *
   * @param name the name of the variable
   * @return the variable with the name {@code name} in this {@code Asset} object
   * @throws java.lang.NullPointerException if {@code name} is {@code null}
   * @throws java.lang.IllegalArgumentException if {@code name} is not the name of a variable in
   *     this {@code Asset} object
   * @since 1.0.0
   */
  public Variable getVariable(String name) {
    if (!this.hasVariable(name)) {
      throw new IllegalArgumentException(String.format("Variable \"%s\" not found", name));
    }
    return this.hasLocalVariable(name)
        ? this.getLocalVariable(name)
        : this.getSuperAsset().getVariable(name);
  }

  /**
   * Returns a list of all variables in this {@code Asset} object.
   *
   * @return a list of all variables in this {@code Asset} object
   * @since 1.0.0
   */
  public List<Variable> getVariables() {
    return List.copyOf(this.getVariablesMap().values());
  }

  private void addVariable(Variable variable) {
    requireNonNull(variable);
    this.variables.put(variable.getName(), variable);
  }

  private Map<String, Variable> getVariablesMap() {
    var variablesMap =
        this.hasSuperAsset()
            ? this.getSuperAsset().getVariablesMap()
            : new LinkedHashMap<String, Variable>();
    variablesMap.putAll(this.variables);
    return variablesMap;
  }

  /**
   * Returns whether {@code name} is the name of a local attack step in this {@code Asset} object.
   *
   * @param name the name of the local attack step
   * @return whether {@code name} is the name of a local attack step in this {@code Asset} object
   * @throws java.lang.NullPointerException if {@code name} is {@code null}
   * @since 1.0.0
   */
  public boolean hasLocalAttackStep(String name) {
    return this.attackSteps.containsKey(requireNonNull(name));
  }

  /**
   * Returns the local attack step with the name {@code name} in this {@code Asset} object.
   *
   * @param name the name of the local attack step
   * @return the local attack step with the name {@code name} in this {@code Asset} object
   * @throws java.lang.NullPointerException if {@code name} is {@code null}
   * @throws java.lang.IllegalArgumentException if {@code name} is not the name of a local attack
   *     step in this {@code Asset} object
   * @since 1.0.0
   */
  public AttackStep getLocalAttackStep(String name) {
    if (!this.hasLocalAttackStep(name)) {
      throw new IllegalArgumentException(String.format("Local attack step \"%s\" not found", name));
    }
    return this.attackSteps.get(name);
  }

  /**
   * Returns a list of all local attack steps in this {@code Asset} object.
   *
   * @return a list of all local attack steps in this {@code Asset} object
   * @since 1.0.0
   */
  public List<AttackStep> getLocalAttackSteps() {
    return List.copyOf(this.attackSteps.values());
  }

  /**
   * Returns whether {@code name} is the name of an attack step in this {@code Asset} object.
   *
   * @param name the name of the attack step
   * @return whether {@code name} is the name of an attack step in this {@code Asset} object
   * @throws java.lang.NullPointerException if {@code name} is {@code null}
   * @since 1.0.0
   */
  public boolean hasAttackStep(String name) {
    return this.hasLocalAttackStep(name)
        || this.hasSuperAsset() && this.getSuperAsset().hasAttackStep(name);
  }

  /**
   * Returns the attack step with the name {@code name} in this {@code Asset} object.
   *
   * @param name the name of the attack step
   * @return the attack step with the name {@code name} in this {@code Asset} object
   * @throws java.lang.NullPointerException if {@code name} is {@code null}
   * @throws java.lang.IllegalArgumentException if {@code name} is not the name of an attack step in
   *     this {@code Asset} object
   * @since 1.0.0
   */
  public AttackStep getAttackStep(String name) {
    if (!this.hasAttackStep(name)) {
      throw new IllegalArgumentException(String.format("Attack step \"%s\" not found", name));
    }
    return this.hasLocalAttackStep(name)
        ? this.getLocalAttackStep(name)
        : this.getSuperAsset().getAttackStep(name);
  }

  /**
   * Returns a list of all attack steps in this {@code Asset} object.
   *
   * @return a list of all attack steps in this {@code Asset} object
   * @since 1.0.0
   */
  public List<AttackStep> getAttackSteps() {
    return List.copyOf(this.getAttackStepsMap().values());
  }

  private void addAttackStep(AttackStep attackStep) {
    requireNonNull(attackStep);
    this.attackSteps.put(attackStep.getName(), attackStep);
  }

  private Map<String, AttackStep> getAttackStepsMap() {
    var attackStepsMap =
        this.hasSuperAsset()
            ? this.getSuperAsset().getAttackStepsMap()
            : new LinkedHashMap<String, AttackStep>();
    attackStepsMap.putAll(this.attackSteps);
    return attackStepsMap;
  }

  /**
   * Returns whether this {@code Asset} object has a local SVG icon.
   *
   * @return whether this {@code Asset} object has a local SVG icon
   * @since 1.0.0
   */
  public boolean hasLocalSvgIcon() {
    return this.svgIcon != null;
  }

  /**
   * Returns the local SVG icon of this {@code Asset} object.
   *
   * @return the local SVG icon of this {@code Asset} object
   * @throws java.lang.UnsupportedOperationException if this {@code Asset} object does not have a
   *     local SVG icon
   * @since 1.0.0
   */
  public byte[] getLocalSvgIcon() {
    if (!this.hasLocalSvgIcon()) {
      throw new UnsupportedOperationException("Local SVG icon not found");
    }
    return this.svgIcon.clone();
  }

  /**
   * Returns whether this {@code Asset} object has an SVG icon.
   *
   * @return whether this {@code Asset} object has an SVG icon
   * @since 1.0.0
   */
  public boolean hasSvgIcon() {
    return this.hasLocalSvgIcon() || this.hasSuperAsset() && this.getSuperAsset().hasSvgIcon();
  }

  /**
   * Returns the SVG icon of this {@code Asset} object.
   *
   * @return the SVG icon of this {@code Asset} object
   * @throws java.lang.UnsupportedOperationException if this {@code Asset} object does not have an
   *     SVG icon
   * @since 1.0.0
   */
  public byte[] getSvgIcon() {
    if (!this.hasSvgIcon()) {
      throw new UnsupportedOperationException("SVG icon not found");
    }
    return this.hasLocalSvgIcon() ? this.getLocalSvgIcon() : this.getSuperAsset().getSvgIcon();
  }

  /**
   * Returns whether this {@code Asset} object has a local PNG icon.
   *
   * @return whether this {@code Asset} object has a local PNG icon
   * @since 1.0.0
   */
  public boolean hasLocalPngIcon() {
    return this.pngIcon != null;
  }

  /**
   * Returns the local PNG icon of this {@code Asset} object.
   *
   * @return the local PNG icon of this {@code Asset} object
   * @throws java.lang.UnsupportedOperationException if this {@code Asset} object does not have a
   *     local PNG icon
   * @since 1.0.0
   */
  public byte[] getLocalPngIcon() {
    if (!this.hasLocalPngIcon()) {
      throw new UnsupportedOperationException("Local PNG icon not found");
    }
    return this.pngIcon.clone();
  }

  /**
   * Returns whether this {@code Asset} object has an PNG icon.
   *
   * @return whether this {@code Asset} object has an PNG icon
   * @since 1.0.0
   */
  public boolean hasPngIcon() {
    return this.hasLocalPngIcon() || this.hasSuperAsset() && this.getSuperAsset().hasPngIcon();
  }

  /**
   * Returns the PNG icon of this {@code Asset} object.
   *
   * @return the PNG icon of this {@code Asset} object
   * @throws java.lang.UnsupportedOperationException if this {@code Asset} object does not have an
   *     PNG icon
   * @since 1.0.0
   */
  public byte[] getPngIcon() {
    if (!this.hasPngIcon()) {
      throw new UnsupportedOperationException("PNG icon not found");
    }
    return this.hasLocalPngIcon() ? this.getLocalPngIcon() : this.getSuperAsset().getPngIcon();
  }

  /**
   * Returns whether this {@code Asset} object is a sub type of {@code other}.
   *
   * @param other another {@code Asset} object
   * @return whether this {@code Asset} object is a sub type of {@code other}
   * @throws java.lang.NullPointerException if {@code other} is {@code null}
   * @since 1.0.0
   */
  public boolean isSubTypeOf(Asset other) {
    requireNonNull(other);
    if (this == other) {
      return true;
    }
    if (!this.hasSuperAsset()) {
      return false;
    }
    return this.getSuperAsset().isSubTypeOf(other);
  }

  /**
   * Returns the least upper bound of {@code asset1} and {@code asset2}, or {@code null} if {@code
   * asset1} and {@code asset2} have no upper bound.
   *
   * @param asset1 an {@code Asset} object
   * @param asset2 an {@code Asset} object
   * @return the least upper bound of {@code asset1} and {@code asset2}, or {@code null} if {@code
   *     asset1} and {@code asset2} have no upper bound
   * @throws java.lang.NullPointerException if {@code asset1} or {@code asset2} is {@code null}
   * @since 1.0.0
   */
  public static Asset leastUpperBound(Asset asset1, Asset asset2) {
    requireNonNull(asset1);
    requireNonNull(asset2);
    if (asset1.isSubTypeOf(asset2)) {
      return asset2;
    }
    if (asset2.isSubTypeOf(asset1)) {
      return asset1;
    }
    if (!asset1.hasSuperAsset() || !asset2.hasSuperAsset()) {
      return null;
    }
    return Asset.leastUpperBound(asset1.getSuperAsset(), asset2.getSuperAsset());
  }

  JsonObject toJson() {
    var jsonVariables = Json.createArrayBuilder();
    for (var variable : this.variables.values()) {
      jsonVariables.add(variable.toJson());
    }

    var jsonAttackSteps = Json.createArrayBuilder();
    for (var attackStep : this.attackSteps.values()) {
      jsonAttackSteps.add(attackStep.toJson());
    }

    var jsonAsset =
        Json.createObjectBuilder()
            .add("name", this.name)
            .add("meta", this.meta.toJson())
            .add("category", this.category.getName())
            .add("isAbstract", this.isAbstract);
    if (this.superAsset == null) {
      jsonAsset.addNull("superAsset");
    } else {
      jsonAsset.add("superAsset", this.superAsset.getName());
    }
    return jsonAsset.add("variables", jsonVariables).add("attackSteps", jsonAttackSteps).build();
  }

  static Asset fromBuilder(AssetBuilder builder, Map<String, Category> categories) {
    requireNonNull(builder);
    requireNonNull(categories);
    if (!categories.containsKey(builder.getCategory())) {
      throw new IllegalArgumentException(
          String.format("Category \"%s\" not found", builder.getCategory()));
    }
    var asset =
        new Asset(
            builder.getName(),
            Meta.fromBuilder(builder.getMeta()),
            categories.get(builder.getCategory()),
            builder.isAbstract(),
            builder.getSvgIcon(),
            builder.getPngIcon());
    for (var variableBuilder : builder.getVariables()) {
      asset.addVariable(Variable.fromBuilder(variableBuilder, asset));
    }
    for (var attackStepBuilder : builder.getAttackSteps()) {
      asset.addAttackStep(AttackStep.fromBuilder(attackStepBuilder, asset));
    }
    return asset;
  }
}
