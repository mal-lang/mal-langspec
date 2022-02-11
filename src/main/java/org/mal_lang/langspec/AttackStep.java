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
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.mal_lang.langspec.builders.AttackStepBuilder;
import org.mal_lang.langspec.step.StepExpression;
import org.mal_lang.langspec.ttc.TtcExpression;

/**
 * Immutable class representing an attack step of an asset in a MAL language.
 *
 * @since 1.0.0
 */
public final class AttackStep {
  private final String name;
  private final Meta meta;
  private final Asset asset;
  private final AttackStepType type;
  private final Set<String> tags;
  private final Risk risk;
  private final TtcExpression ttc;
  private final Steps requires;
  private final Steps reaches;

  private AttackStep(
      String name,
      Meta meta,
      Asset asset,
      AttackStepType type,
      List<String> tags,
      Risk risk,
      TtcExpression ttc,
      Steps requires,
      Steps reaches) {
    this.name = requireNonNull(name);
    this.meta = requireNonNull(meta);
    this.asset = requireNonNull(asset);
    this.type = requireNonNull(type);
    this.tags = new LinkedHashSet<>(requireNonNull(tags));
    this.risk = risk;
    this.ttc = ttc;
    this.requires = requires;
    this.reaches = reaches;
  }

  /**
   * Returns the name of this {@code AttackStep} object.
   *
   * @return the name of this {@code AttackStep} object
   * @since 1.0.0
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the meta info of this {@code AttackStep} object.
   *
   * @return the meta info of this {@code AttackStep} object
   * @since 1.0.0
   */
  public Meta getMeta() {
    return this.meta;
  }

  /**
   * Returns the asset of this {@code AttackStep} object.
   *
   * @return the asset of this {@code AttackStep} object
   * @since 1.0.0
   */
  public Asset getAsset() {
    return this.asset;
  }

  /**
   * Returns the type of this {@code AttackStep} object.
   *
   * @return the type of this {@code AttackStep} object
   * @since 1.0.0
   */
  public AttackStepType getType() {
    return this.type;
  }

  /**
   * Returns whether {@code name} is the name of a local tag in this {@code AttackStep} object.
   *
   * @param name the name of the local tag
   * @return whether {@code name} is the name of a local tag in this {@code AttackStep} object
   * @throws java.lang.NullPointerException if {@code name} is {@code null}
   * @since 1.0.0
   */
  public boolean hasLocalTag(String name) {
    return this.tags.contains(requireNonNull(name));
  }

  /**
   * Returns a list of all local tags in this {@code AttackStep} object.
   *
   * @return a list of all local tags in this {@code AttackStep} object
   * @since 1.0.0
   */
  public List<String> getLocalTags() {
    return List.copyOf(this.tags);
  }

  /**
   * Returns whether {@code name} is the name of a tag in this {@code AttackStep} object.
   *
   * @param name the name of the tag
   * @return whether {@code name} is the name of a tag in this {@code AttackStep} object
   * @throws java.lang.NullPointerException if {@code name} is {@code null}
   * @since 1.0.0
   */
  public boolean hasTag(String name) {
    return this.hasLocalTag(name)
        || this.hasSuperAttackStep() && this.getSuperAttackStep().hasTag(name);
  }

  /**
   * Returns a list of all tags in this {@code AttackStep} object.
   *
   * @return a list of all tags in this {@code AttackStep} object
   * @since 1.0.0
   */
  public List<String> getTags() {
    return List.copyOf(this.getTagsSet());
  }

  private Set<String> getTagsSet() {
    var tagsSet =
        this.hasSuperAttackStep()
            ? this.getSuperAttackStep().getTagsSet()
            : new LinkedHashSet<String>();
    tagsSet.addAll(this.tags);
    return tagsSet;
  }

  /**
   * Returns whether this {@code AttackStep} object has a local risk.
   *
   * @return whether this {@code AttackStep} object has a local risk
   * @since 1.0.0
   */
  public boolean hasLocalRisk() {
    return this.risk != null;
  }

  /**
   * Returns the local risk of this {@code AttackStep} object.
   *
   * @return the local risk of this {@code AttackStep} object
   * @throws java.lang.UnsupportedOperationException if this {@code AttackStep} object does not have
   *     a local risk
   * @since 1.0.0
   */
  public Risk getLocalRisk() {
    if (!this.hasLocalRisk()) {
      throw new UnsupportedOperationException("Local risk not found");
    }
    return this.risk;
  }

  /**
   * Returns whether this {@code AttackStep} object has a risk.
   *
   * @return whether this {@code AttackStep} object has a risk
   * @since 1.0.0
   */
  public boolean hasRisk() {
    return this.hasLocalRisk() || this.hasSuperAttackStep() && this.getSuperAttackStep().hasRisk();
  }

  /**
   * Returns the risk of this {@code AttackStep} object.
   *
   * @return the risk of this {@code AttackStep} object
   * @throws java.lang.UnsupportedOperationException if this {@code AttackStep} object does not have
   *     a risk
   * @since 1.0.0
   */
  public Risk getRisk() {
    if (!this.hasRisk()) {
      throw new UnsupportedOperationException("Risk not found");
    }
    return this.hasLocalRisk() ? this.getLocalRisk() : this.getSuperAttackStep().getRisk();
  }

  /**
   * Returns whether this {@code AttackStep} object has a local TTC.
   *
   * @return whether this {@code AttackStep} object has a local TTC
   * @since 1.0.0
   */
  public boolean hasLocalTtc() {
    return this.ttc != null;
  }

  /**
   * Returns the local TTC of this {@code AttackStep} object.
   *
   * @return the local TTC of this {@code AttackStep} object
   * @throws java.lang.UnsupportedOperationException if this {@code AttackStep} object does not have
   *     a local TTC
   * @since 1.0.0
   */
  public TtcExpression getLocalTtc() {
    if (!this.hasLocalTtc()) {
      throw new UnsupportedOperationException("Local TTC not found");
    }
    return this.ttc;
  }

  /**
   * Returns whether this {@code AttackStep} object has a TTC.
   *
   * @return whether this {@code AttackStep} object has a TTC
   * @since 1.0.0
   */
  public boolean hasTtc() {
    return this.hasLocalTtc() || this.hasSuperAttackStep() && this.getSuperAttackStep().hasTtc();
  }

  /**
   * Returns the TTC of this {@code AttackStep} object.
   *
   * @return the TTC of this {@code AttackStep} object
   * @throws java.lang.UnsupportedOperationException if this {@code AttackStep} object does not have
   *     a TTC
   * @since 1.0.0
   */
  public TtcExpression getTtc() {
    if (!this.hasTtc()) {
      throw new UnsupportedOperationException("TTC not found");
    }
    return this.hasLocalTtc() ? this.getLocalTtc() : this.getSuperAttackStep().getTtc();
  }

  /**
   * Returns whether this {@code AttackStep} object has local requires steps.
   *
   * @return whether this {@code AttackStep} object has local requires steps
   * @since 1.0.0
   */
  public boolean hasLocalRequires() {
    return this.requires != null;
  }

  /**
   * Returns the local requires steps of this {@code AttackStep} object.
   *
   * @return the local requires steps of this {@code AttackStep} object
   * @throws java.lang.UnsupportedOperationException if this {@code AttackStep} object does not have
   *     local requires steps
   * @since 1.0.0
   */
  public Steps getLocalRequires() {
    if (!this.hasLocalRequires()) {
      throw new UnsupportedOperationException("Local requires not found");
    }
    return this.requires;
  }

  /**
   * Returns the requires steps of this {@code AttackStep} object.
   *
   * @return the requires steps of this {@code AttackStep} object
   * @since 1.0.0
   */
  public List<StepExpression> getRequires() {
    return List.copyOf(this.getRequiresList());
  }

  private List<StepExpression> getRequiresList() {
    var overrides = this.hasLocalRequires() && this.requires.overrides();
    var requiresList =
        this.hasSuperAttackStep() && !overrides
            ? this.getSuperAttackStep().getRequiresList()
            : new ArrayList<StepExpression>();
    if (this.hasLocalRequires()) {
      requiresList.addAll(this.requires.getStepExpressions());
    }
    return requiresList;
  }

  /**
   * Returns whether this {@code AttackStep} object has local reaches steps.
   *
   * @return whether this {@code AttackStep} object has local reaches steps
   * @since 1.0.0
   */
  public boolean hasLocalReaches() {
    return this.reaches != null;
  }

  /**
   * Returns the local reaches steps of this {@code AttackStep} object.
   *
   * @return the local reaches steps of this {@code AttackStep} object
   * @throws java.lang.UnsupportedOperationException if this {@code AttackStep} object does not have
   *     local reaches steps
   * @since 1.0.0
   */
  public Steps getLocalReaches() {
    if (!this.hasLocalReaches()) {
      throw new UnsupportedOperationException("Local reaches not found");
    }
    return this.reaches;
  }

  /**
   * Returns the reaches steps of this {@code AttackStep} object.
   *
   * @return the reaches steps of this {@code AttackStep} object
   * @since 1.0.0
   */
  public List<StepExpression> getReaches() {
    return List.copyOf(this.getReachesList());
  }

  private List<StepExpression> getReachesList() {
    var overrides = this.hasLocalReaches() && this.reaches.overrides();
    var reachesList =
        this.hasSuperAttackStep() && !overrides
            ? this.getSuperAttackStep().getReachesList()
            : new ArrayList<StepExpression>();
    if (this.hasLocalReaches()) {
      reachesList.addAll(this.reaches.getStepExpressions());
    }
    return reachesList;
  }

  /**
   * Returns whether this {@code AttackStep} object has a super attack step.
   *
   * @return whether this {@code AttackStep} object has a super attack step
   * @since 1.0.0
   */
  public boolean hasSuperAttackStep() {
    return this.asset.hasSuperAsset() && this.asset.getSuperAsset().hasAttackStep(this.name);
  }

  /**
   * Returns the super attack step of this {@code AttackStep} object.
   *
   * @return the super attack step of this {@code AttackStep} object
   * @throws java.lang.UnsupportedOperationException if this {@code AttackStep} object does not have
   *     a super attack step
   * @since 1.0.0
   */
  public AttackStep getSuperAttackStep() {
    if (!this.hasSuperAttackStep()) {
      throw new UnsupportedOperationException("Super attack step not found");
    }
    return this.asset.getSuperAsset().getAttackStep(this.name);
  }

  JsonObject toJson() {
    var jsonTags = Json.createArrayBuilder();
    for (var tag : this.tags) {
      jsonTags.add(tag);
    }

    var jsonAttackStep =
        Json.createObjectBuilder()
            .add("name", this.name)
            .add("meta", this.meta.toJson())
            .add("type", this.type.toString())
            .add("tags", jsonTags);
    if (this.risk == null) {
      jsonAttackStep.addNull("risk");
    } else {
      jsonAttackStep.add("risk", this.risk.toJson());
    }
    if (this.ttc == null) {
      jsonAttackStep.addNull("ttc");
    } else {
      jsonAttackStep.add("ttc", this.ttc.toJson());
    }
    if (this.requires == null) {
      jsonAttackStep.addNull("requires");
    } else {
      jsonAttackStep.add("requires", this.requires.toJson());
    }
    if (this.reaches == null) {
      jsonAttackStep.addNull("reaches");
    } else {
      jsonAttackStep.add("reaches", this.reaches.toJson());
    }
    return jsonAttackStep.build();
  }

  static AttackStep fromBuilder(AttackStepBuilder builder, Asset asset) {
    requireNonNull(builder);
    requireNonNull(asset);
    var requires = builder.getRequires() == null ? null : Steps.fromBuilder(builder.getRequires());
    var reaches = builder.getReaches() == null ? null : Steps.fromBuilder(builder.getReaches());
    return new AttackStep(
        builder.getName(),
        Meta.fromBuilder(builder.getMeta()),
        asset,
        builder.getType(),
        builder.getTags(),
        builder.getRisk(),
        builder.getTtc(),
        requires,
        reaches);
  }
}
