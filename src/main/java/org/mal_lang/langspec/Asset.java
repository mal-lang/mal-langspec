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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Immutable class representing an asset in a MAL language. */
public final class Asset {
  private final String name;
  private final Meta meta;
  private final Category category;
  private final boolean isAbstract;
  private Asset superAsset;
  private final Map<String, Variable> variables = new LinkedHashMap<>();
  private final Map<String, AttackStep> attackSteps = new LinkedHashMap<>();
  private final Map<String, Field> fields = new LinkedHashMap<>();
  private final byte[] svgIcon;
  private final byte[] pngIcon;

  Asset(
      String name,
      Meta meta,
      Category category,
      boolean isAbstract,
      byte[] svgIcon,
      byte[] pngIcon) {
    this.name = name;
    this.meta = meta;
    this.category = category;
    this.isAbstract = isAbstract;
    this.svgIcon = svgIcon == null ? null : svgIcon.clone();
    this.pngIcon = pngIcon == null ? null : pngIcon.clone();
    category.addAsset(this);
  }

  /**
   * Returns the name of this {@code Asset} object.
   *
   * @return the name of this {@code Asset} object
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the meta info of this {@code Asset} object.
   *
   * @return the meta info of this {@code Asset} object
   */
  public Meta getMeta() {
    return meta;
  }

  /**
   * Returns the category of this {@code Asset} object.
   *
   * @return the category of this {@code Asset} object
   */
  public Category getCategory() {
    return category;
  }

  /**
   * Returns whether this {@code Asset} object is abstract.
   *
   * @return whether this {@code Asset} object is abstract
   */
  public boolean isAbstract() {
    return isAbstract;
  }

  void setSuperAsset(Asset superAsset) {
    this.superAsset = superAsset;
  }

  /**
   * Returns whether this {@code Asset} object has a super asset.
   *
   * @return whether this {@code Asset} object has a super asset
   */
  public boolean hasSuperAsset() {
    return superAsset != null;
  }

  /**
   * Returns the super asset of this {@code Asset} object.
   *
   * @return the super asset of this {@code Asset} object
   * @throws UnsupportedOperationException if this {@code Asset} object does not have a super asset
   */
  public Asset getSuperAsset() {
    if (!hasSuperAsset()) {
      throw new UnsupportedOperationException(
          String.format("Asset \"%s\" does not have a super asset", name));
    }
    return superAsset;
  }

  void addVariable(Variable variable) {
    variables.put(variable.getName(), variable);
  }

  /**
   * Returns whether {@code name} is the name of a local variable in this {@code Asset} object.
   *
   * @param name the name of the local variable
   * @return whether {@code name} is the name of a local variable in this {@code Asset} object
   * @throws NullPointerException if {@code name} is {@code null}
   */
  public boolean hasLocalVariable(String name) {
    checkNotNull(name);
    return variables.containsKey(name);
  }

  /**
   * Returns the local variable in this {@code Asset} object with the name {@code name}.
   *
   * @param name the name of the local variable
   * @return the local variable in this {@code Asset} object with the name {@code name}
   * @throws NullPointerException if {@code name} is {@code null}
   * @throws IllegalArgumentException if {@code name} is not the name of a local variable in this
   *     {@code Asset} object
   */
  public Variable getLocalVariable(String name) {
    if (!hasLocalVariable(name)) {
      throw new IllegalArgumentException(String.format("Local variable \"%s\" not found", name));
    }
    return variables.get(name);
  }

  /**
   * Returns a list of all local variables in this {@code Asset} object.
   *
   * @return a list of all local variables in this {@code Asset} object
   */
  public List<Variable> getLocalVariables() {
    return List.copyOf(variables.values());
  }

  /**
   * Returns whether {@code name} is the name of a variable in this {@code Asset} object.
   *
   * @param name the name of the variable
   * @return whether {@code name} is the name of a variable in this {@code Asset} object
   * @throws NullPointerException if {@code name} is {@code null}
   */
  public boolean hasVariable(String name) {
    checkNotNull(name);
    return hasLocalVariable(name) || hasSuperAsset() && getSuperAsset().hasVariable(name);
  }

  /**
   * Returns the variable in this {@code Asset} object with the name {@code name}.
   *
   * @param name the name of the variable
   * @return the variable in this {@code Asset} object with the name {@code name}
   * @throws NullPointerException if {@code name} is {@code null}
   * @throws IllegalArgumentException if {@code name} is not the name of a variable in this {@code
   *     Asset} object
   */
  public Variable getVariable(String name) {
    if (!hasVariable(name)) {
      throw new IllegalArgumentException(String.format("Variable \"%s\" not found", name));
    }
    return hasLocalVariable(name) ? getLocalVariable(name) : getSuperAsset().getVariable(name);
  }

  private Map<String, Variable> getVariablesMap() {
    var variablesMap =
        hasSuperAsset() ? getSuperAsset().getVariablesMap() : new LinkedHashMap<String, Variable>();
    for (var entry : variables.entrySet()) {
      variablesMap.put(entry.getKey(), entry.getValue());
    }
    return variablesMap;
  }

  /**
   * Returns a list of all variables in this {@code Asset} object.
   *
   * @return a list of all variables in this {@code Asset} object
   */
  public List<Variable> getVariables() {
    return List.copyOf(getVariablesMap().values());
  }

  void addAttackStep(AttackStep attackStep) {
    attackSteps.put(attackStep.getName(), attackStep);
  }

  /**
   * Returns whether {@code name} is the name of a local attack step in this {@code Asset} object.
   *
   * @param name the name of the local attack step
   * @return whether {@code name} is the name of a local attack step in this {@code Asset} object
   * @throws NullPointerException if {@code name} is {@code null}
   */
  public boolean hasLocalAttackStep(String name) {
    checkNotNull(name);
    return attackSteps.containsKey(name);
  }

  /**
   * Returns the local attack step in this {@code Asset} object with the name {@code name}.
   *
   * @param name the name of the local attack step
   * @return the local attack step in this {@code Asset} object with the name {@code name}
   * @throws NullPointerException if {@code name} is {@code null}
   * @throws IllegalArgumentException if {@code name} is not the name of a local attack step in this
   *     {@code Asset} object
   */
  public AttackStep getLocalAttackStep(String name) {
    if (!hasLocalAttackStep(name)) {
      throw new IllegalArgumentException(String.format("Local attack step \"%s\" not found", name));
    }
    return attackSteps.get(name);
  }

  /**
   * Returns a list of all local attack steps in this {@code Asset} object.
   *
   * @return a list of all local attack steps in this {@code Asset} object
   */
  public List<AttackStep> getLocalAttackSteps() {
    return List.copyOf(attackSteps.values());
  }

  /**
   * Returns whether {@code name} is the name of an attack step in this {@code Asset} object.
   *
   * @param name the name of the attack step
   * @return whether {@code name} is the name of an attack step in this {@code Asset} object
   * @throws NullPointerException if {@code name} is {@code null}
   */
  public boolean hasAttackStep(String name) {
    checkNotNull(name);
    return hasLocalAttackStep(name) || hasSuperAsset() && getSuperAsset().hasAttackStep(name);
  }

  /**
   * Returns the attack step in this {@code Asset} object with the name {@code name}.
   *
   * @param name the name of the attack step
   * @return the attack step in this {@code Asset} object with the name {@code name}
   * @throws NullPointerException if {@code name} is {@code null}
   * @throws IllegalArgumentException if {@code name} is not the name of an attack step in this
   *     {@code Asset} object
   */
  public AttackStep getAttackStep(String name) {
    if (!hasAttackStep(name)) {
      throw new IllegalArgumentException(String.format("Attack step \"%s\" not found", name));
    }
    return hasLocalAttackStep(name)
        ? getLocalAttackStep(name)
        : getSuperAsset().getAttackStep(name);
  }

  private Map<String, AttackStep> getAttackStepsMap() {
    var attackStepsMap =
        hasSuperAsset()
            ? getSuperAsset().getAttackStepsMap()
            : new LinkedHashMap<String, AttackStep>();
    for (var entry : attackSteps.entrySet()) {
      attackStepsMap.put(entry.getKey(), entry.getValue());
    }
    return attackStepsMap;
  }

  /**
   * Returns a list of all attack steps in this {@code Asset} object.
   *
   * @return a list of all attack steps in this {@code Asset} object
   */
  public List<AttackStep> getAttackSteps() {
    return List.copyOf(getAttackStepsMap().values());
  }

  void addField(Field field) {
    fields.put(field.getName(), field);
  }

  /**
   * Returns whether {@code name} is the name of a local field in this {@code Asset} object.
   *
   * @param name the name of the local field
   * @return whether {@code name} is the name of a local field in this {@code Asset} object
   * @throws NullPointerException if {@code name} is {@code null}
   */
  public boolean hasLocalField(String name) {
    checkNotNull(name);
    return fields.containsKey(name);
  }

  /**
   * Returns the local field in this {@code Asset} object with the name {@code name}.
   *
   * @param name the name of the local field
   * @return the local field in this {@code Asset} object with the name {@code name}
   * @throws NullPointerException if {@code name} is {@code null}
   * @throws IllegalArgumentException if {@code name} is not the name of a local field in this
   *     {@code Asset} object
   */
  public Field getLocalField(String name) {
    if (!hasLocalField(name)) {
      throw new IllegalArgumentException(String.format("Local field \"%s\" not found", name));
    }
    return fields.get(name);
  }

  /**
   * Returns a list of all local fields in this {@code Asset} object.
   *
   * @return a list of all local fields in this {@code Asset} object
   */
  public List<Field> getLocalFields() {
    return List.copyOf(fields.values());
  }

  /**
   * Returns whether {@code name} is the name of a field in this {@code Asset} object.
   *
   * @param name the name of the field
   * @return whether {@code name} is the name of a field in this {@code Asset} object
   * @throws NullPointerException if {@code name} is {@code null}
   */
  public boolean hasField(String name) {
    checkNotNull(name);
    return hasLocalField(name) || hasSuperAsset() && getSuperAsset().hasField(name);
  }

  /**
   * Returns the field in this {@code Asset} object with the name {@code name}.
   *
   * @param name the name of the field
   * @return the field in this {@code Asset} object with the name {@code name}
   * @throws NullPointerException if {@code name} is {@code null}
   * @throws IllegalArgumentException if {@code name} is not the name of a field in this {@code
   *     Asset} object
   */
  public Field getField(String name) {
    if (!hasField(name)) {
      throw new IllegalArgumentException(String.format("Field \"%s\" not found", name));
    }
    return hasLocalField(name) ? getLocalField(name) : getSuperAsset().getField(name);
  }

  private Map<String, Field> getFieldsMap() {
    var fieldsMap =
        hasSuperAsset() ? getSuperAsset().getFieldsMap() : new LinkedHashMap<String, Field>();
    for (var entry : fields.entrySet()) {
      fieldsMap.put(entry.getKey(), entry.getValue());
    }
    return fieldsMap;
  }

  /**
   * Returns a list of all fields in this {@code Asset} object.
   *
   * @return a list of all fields in this {@code Asset} object
   */
  public List<Field> getFields() {
    return List.copyOf(getFieldsMap().values());
  }

  /**
   * Returns whether this {@code Asset} object has a local SVG icon.
   *
   * @return whether this {@code Asset} object has a local SVG icon
   */
  public boolean hasLocalSvgIcon() {
    return svgIcon != null;
  }

  /**
   * Returns the local SVG icon of this {@code Asset} object.
   *
   * @return the local SVG icon of this {@code Asset} object
   * @throws UnsupportedOperationException if this {@code Asset} object does not have a local SVG
   *     icon
   */
  public byte[] getLocalSvgIcon() {
    if (!hasLocalSvgIcon()) {
      throw new UnsupportedOperationException(
          String.format("Asset \"%s\" does not have a local SVG icon", name));
    }
    return svgIcon.clone();
  }

  /**
   * Returns whether this {@code Asset} object has an SVG icon.
   *
   * @return whether this {@code Asset} object has an SVG icon
   */
  public boolean hasSvgIcon() {
    return hasLocalSvgIcon() || hasSuperAsset() && getSuperAsset().hasSvgIcon();
  }

  /**
   * Returns the SVG icon of this {@code Asset} object.
   *
   * @return the SVG icon of this {@code Asset} object
   * @throws UnsupportedOperationException if this {@code Asset} object does not have an SVG icon
   */
  public byte[] getSvgIcon() {
    if (!hasSvgIcon()) {
      throw new UnsupportedOperationException(
          String.format("Asset \"%s\" does not have an SVG icon", name));
    }
    return hasLocalSvgIcon() ? getLocalSvgIcon() : getSuperAsset().getSvgIcon();
  }

  /**
   * Returns whether this {@code Asset} object has a local PNG icon.
   *
   * @return whether this {@code Asset} object has a local PNG icon
   */
  public boolean hasLocalPngIcon() {
    return pngIcon != null;
  }

  /**
   * Returns the local PNG icon of this {@code Asset} object.
   *
   * @return the local PNG icon of this {@code Asset} object
   * @throws UnsupportedOperationException if this {@code Asset} object does not have a local PNG
   *     icon
   */
  public byte[] getLocalPngIcon() {
    if (!hasLocalPngIcon()) {
      throw new UnsupportedOperationException(
          String.format("Asset \"%s\" does not have a local PNG icon", name));
    }
    return pngIcon.clone();
  }

  /**
   * Returns whether this {@code Asset} object has a PNG icon.
   *
   * @return whether this {@code Asset} object has a PNG icon
   */
  public boolean hasPngIcon() {
    return hasLocalPngIcon() || hasSuperAsset() && getSuperAsset().hasPngIcon();
  }

  /**
   * Returns the PNG icon of this {@code Asset} object.
   *
   * @return the PNG icon of this {@code Asset} object
   * @throws UnsupportedOperationException if this {@code Asset} object does not have a PNG icon
   */
  public byte[] getPngIcon() {
    if (!hasPngIcon()) {
      throw new UnsupportedOperationException(
          String.format("Asset \"%s\" does not have an PNG icon", name));
    }
    return hasLocalPngIcon() ? getLocalPngIcon() : getSuperAsset().getPngIcon();
  }

  /**
   * Returns the JSON representation of this {@code Asset} object.
   *
   * @return the JSON representation of this {@code Asset} object
   */
  public JsonObject toJson() {
    // Variables
    var jsonVariables = Json.createArrayBuilder();
    for (var variable : variables.values()) {
      jsonVariables.add(variable.toJson());
    }

    // Attack steps
    var jsonAttackSteps = Json.createArrayBuilder();
    for (var attackStep : attackSteps.values()) {
      jsonAttackSteps.add(attackStep.toJson());
    }

    var jsonAsset =
        Json.createObjectBuilder()
            .add("name", name)
            .add("meta", meta.toJson())
            .add("category", category.getName())
            .add("isAbstract", isAbstract);
    if (superAsset == null) {
      jsonAsset.addNull("superAsset");
    } else {
      jsonAsset.add("superAsset", superAsset.getName());
    }
    return jsonAsset.add("variables", jsonVariables).add("attackSteps", jsonAttackSteps).build();
  }

  /**
   * Creates a new {@link AssetBuilder} object.
   *
   * @param name the name of the asset
   * @param category the category of the asset
   * @return a new {@link AssetBuilder} object
   * @throws NullPointerException if {@code name} or {@code category} is {@code null}
   * @throws IllegalArgumentException if {@code name} or {@code category} is not a valid identifier
   */
  public static AssetBuilder builder(String name, String category) {
    checkIdentifier(name, category);
    return new AssetBuilder(name, category);
  }
}
