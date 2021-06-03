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
import org.mal_lang.langspec.builders.VariableBuilder;
import org.mal_lang.langspec.step.StepExpression;

/**
 * Immutable class representing a variable of an asset in a MAL language.
 *
 * @since 1.0.0
 */
public final class Variable {
  private final String name;
  private final Asset asset;
  private StepExpression stepExpression;

  private Variable(String name, Asset asset) {
    this.name = requireNonNull(name);
    this.asset = requireNonNull(asset);
  }

  /**
   * Returns the name of this {@code Variable} object.
   *
   * @return the name of this {@code Variable} object
   * @since 1.0.0
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the asset of this {@code Variable} object.
   *
   * @return the asset of this {@code Variable} object
   * @since 1.0.0
   */
  public Asset getAsset() {
    return this.asset;
  }

  /**
   * Returns the step expression of this {@code Variable} object.
   *
   * @return the step expression of this {@code Variable} object.
   * @since 1.0.0
   */
  public StepExpression getStepExpression() {
    return this.stepExpression;
  }

  void setStepExpression(StepExpression stepExpression) {
    this.stepExpression = requireNonNull(stepExpression);
  }

  /**
   * Returns whether this {@code Variable} object has a super variable.
   *
   * @return whether this {@code Variable} object has a super variable
   * @since 1.*.0
   */
  public boolean hasSuperVariable() {
    return this.asset.hasSuperAsset() && this.asset.getSuperAsset().hasVariable(this.name);
  }

  /**
   * Returns the super variable of this {@code Variable} object.
   *
   * @return the super variable of this {@code Variable} object
   * @throws java.lang.UnsupportedOperationException if this {@code Variable} object does not have a
   *     super variable
   * @since 1.0.0
   */
  public Variable getSuperVariable() {
    if (!this.hasSuperVariable()) {
      throw new UnsupportedOperationException("Super variable not found");
    }
    return this.asset.getSuperAsset().getVariable(this.name);
  }

  JsonObject toJson() {
    return Json.createObjectBuilder()
        .add("name", this.name)
        .add("stepExpression", this.stepExpression.toJson())
        .build();
  }

  static Variable fromBuilder(VariableBuilder builder, Asset asset) {
    requireNonNull(builder);
    requireNonNull(asset);
    return new Variable(builder.getName(), asset);
  }
}
