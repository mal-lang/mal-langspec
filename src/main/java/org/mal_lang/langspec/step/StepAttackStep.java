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

package org.mal_lang.langspec.step;

import static java.util.Objects.requireNonNull;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import java.util.Map;
import org.mal_lang.langspec.Asset;
import org.mal_lang.langspec.AttackStep;
import org.mal_lang.langspec.Variable;
import org.mal_lang.langspec.builders.step.StepAttackStepBuilder;

/**
 * Immutable class representing an attack step step in a MAL language.
 *
 * @since 1.0.0
 */
public final class StepAttackStep extends StepReference {
  private final AttackStep attackStep;

  private StepAttackStep(Asset sourceAsset, Asset targetAsset, AttackStep attackStep) {
    super(sourceAsset, targetAsset);
    this.attackStep = requireNonNull(attackStep);
  }

  /**
   * Returns the attack step of this {@code StepAttackStep} object.
   *
   * @return the attack step of this {@code StepAttackStep} object
   * @since 1.0.0
   */
  public AttackStep getAttackStep() {
    return this.attackStep;
  }

  @Override
  public JsonObject toJson() {
    return Json.createObjectBuilder()
        .add("type", "attackStep")
        .add("name", this.attackStep.getName())
        .build();
  }

  static StepAttackStep fromBuilder(
      StepAttackStepBuilder builder,
      Asset sourceAsset,
      Map<String, Asset> assets,
      Map<Variable, Asset> variableTargets) {
    requireNonNull(builder);
    requireNonNull(sourceAsset);
    requireNonNull(assets);
    requireNonNull(variableTargets);
    if (!sourceAsset.hasAttackStep(builder.getName())) {
      throw new IllegalArgumentException(
          String.format("Attack step \"%s\" not found", builder.getName()));
    }
    var attackStep = sourceAsset.getAttackStep(builder.getName());
    return new StepAttackStep(sourceAsset, attackStep.getAsset(), attackStep);
  }
}
