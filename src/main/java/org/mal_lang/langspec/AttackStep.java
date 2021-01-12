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
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.mal_lang.langspec.ttc.TtcExpression;

/** Immutable class representing an attack step of an asset in a MAL language. */
public final class AttackStep {
  private final String name;
  private final Meta meta;
  private final Asset asset;
  private final AttackStepType type;
  private final Set<String> tags;
  private final Risk risk;
  private final TtcExpression ttc;

  private AttackStep(
      String name,
      Meta meta,
      Asset asset,
      AttackStepType type,
      List<String> tags,
      Risk risk,
      TtcExpression ttc) {
    this.name = name;
    this.meta = meta;
    this.asset = asset;
    this.type = type;
    this.tags = Set.copyOf(tags);
    this.risk = risk;
    this.ttc = ttc;
    asset.addAttackStep(this);
  }

  /**
   * Returns the name of this {@code AttackStep} object.
   *
   * @return the name of this {@code AttackStep} object
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the meta info of this {@code AttackStep} object.
   *
   * @return the meta info of this {@code AttackStep} object
   */
  public Meta getMeta() {
    return meta;
  }

  /**
   * Returns the asset of this {@code AttackStep} object.
   *
   * @return the asset of this {@code AttackStep} object
   */
  public Asset getAsset() {
    return asset;
  }

  /**
   * Returns whether this {@code AttackStep} object has a super attack step.
   *
   * @return whether this {@code AttackStep} object has a super attack step
   */
  public boolean hasSuperAttackStep() {
    return asset.hasSuperAsset() && asset.getSuperAsset().hasAttackStep(name);
  }

  /**
   * Returns the super attack step of this {@code AttackStep} object.
   *
   * @return the super attack step of this {@code AttackStep} object
   * @throws UnsupportedOperationException if this {@code AttackStep} object does not have a super
   *     attack step
   */
  public AttackStep getSuperAttackStep() {
    if (!hasSuperAttackStep()) {
      throw new UnsupportedOperationException(
          String.format(
              "Attack step \"%s.%s\" does not have a super attack step", asset.getName(), name));
    }
    return asset.getSuperAsset().getAttackStep(name);
  }

  /**
   * Returns the type of this {@code AttackStep} object.
   *
   * @return the type of this {@code AttackStep} object
   */
  public AttackStepType getType() {
    return type;
  }

  /**
   * Returns whether {@code name} is the name of a local tag in this {@code AttackStep} object.
   *
   * @param name the name of the local tag
   * @return whether {@code name} is the name of a local tag in this {@code AttackStep} object
   * @throws NullPointerException if {@code name} is {@code null}
   */
  public boolean hasLocalTag(String name) {
    checkNotNull(name);
    return tags.contains(name);
  }

  /**
   * Returns a list of all local tags in this {@code AttackStep} object.
   *
   * @return a list of all local tags in this {@code AttackStep} object
   */
  public List<String> getLocalTags() {
    return List.copyOf(tags);
  }

  /**
   * Returns whether {@code name} is the name of a tag in this {@code AttackStep} object.
   *
   * @param name the name of the tag
   * @return whether {@code name} is the name of a tag in this {@code AttackStep} object
   * @throws NullPointerException if {@code name} is {@code null}
   */
  public boolean hasTag(String name) {
    checkNotNull(name);
    return hasLocalTag(name) || hasSuperAttackStep() && getSuperAttackStep().hasTag(name);
  }

  private Set<String> getTagsSet() {
    var tagsSet =
        hasSuperAttackStep() ? getSuperAttackStep().getTagsSet() : new LinkedHashSet<String>();
    for (var tag : tags) {
      tagsSet.add(tag);
    }
    return tagsSet;
  }

  /**
   * Returns a list of all tags in this {@code AttackStep} object.
   *
   * @return a list of all tags in this {@code AttackStep} object
   */
  public List<String> getTags() {
    return List.copyOf(getTagsSet());
  }

  /**
   * Returns whether this {@code AttackStep} object has a local risk.
   *
   * @return whether this {@code AttackStep} object has a local risk
   */
  public boolean hasLocalRisk() {
    return risk != null;
  }

  /**
   * Returns the local risk of this {@code AttackStep} object.
   *
   * @return the local risk of this {@code AttackStep} object
   * @throws UnsupportedOperationException if this {@code AttackStep} object does not have a local
   *     risk
   */
  public Risk getLocalRisk() {
    if (!hasLocalRisk()) {
      throw new UnsupportedOperationException(
          String.format("Attack step \"%s.%s\" does not have a local risk", asset.getName(), name));
    }
    return risk;
  }

  /**
   * Returns whether this {@code AttackStep} object has a risk.
   *
   * @return whether this {@code AttackStep} object has a risk
   */
  public boolean hasRisk() {
    return hasLocalRisk() || hasSuperAttackStep() && getSuperAttackStep().hasRisk();
  }

  /**
   * Returns the risk of this {@code AttackStep} object.
   *
   * @return the risk of this {@code AttackStep} object
   * @throws UnsupportedOperationException if this {@code AttackStep} object does not have a risk
   */
  public Risk getRisk() {
    if (!hasRisk()) {
      throw new UnsupportedOperationException(
          String.format("Attack step \"%s.%s\" does not have a risk", asset.getName(), name));
    }
    return hasLocalRisk() ? getLocalRisk() : getSuperAttackStep().getRisk();
  }

  /**
   * Returns whether this {@code AttackStep} object has a local TTC.
   *
   * @return whether this {@code AttackStep} object has a local TTC
   */
  public boolean hasLocalTtc() {
    return ttc != null;
  }

  /**
   * Returns the local TTC of this {@code AttackStep} object.
   *
   * @return the local TTC of this {@code AttackStep} object
   * @throws UnsupportedOperationException if this {@code AttackStep} object does not have a local
   *     TTC
   */
  public TtcExpression getLocalTtc() {
    if (!hasLocalTtc()) {
      throw new UnsupportedOperationException(
          String.format("Attack step \"%s.%s\" does not have a local TTC", asset.getName(), name));
    }
    return ttc;
  }

  /**
   * Returns whether this {@code AttackStep} object has a TTC.
   *
   * @return whether this {@code AttackStep} object has a TTC
   */
  public boolean hasTtc() {
    return hasLocalTtc() || hasSuperAttackStep() && getSuperAttackStep().hasTtc();
  }

  /**
   * Returns the TTC of this {@code AttackStep} object.
   *
   * @return the TTC of this {@code AttackStep} object
   * @throws UnsupportedOperationException if this {@code AttackStep} object does not have a TTC
   */
  public TtcExpression getTtc() {
    if (!hasTtc()) {
      throw new UnsupportedOperationException(
          String.format("Attack step \"%s.%s\" does not have a TTC", asset.getName(), name));
    }
    return hasLocalTtc() ? getLocalTtc() : getSuperAttackStep().getTtc();
  }

  /**
   * Returns the JSON representation of this {@code AttackStep} object.
   *
   * @return the JSON representation of this {@code AttackStep} object
   */
  public JsonObject toJson() {
    var jsonTags = Json.createArrayBuilder();
    for (var tag : tags) {
      jsonTags.add(tag);
    }

    var jsonAttackStep =
        Json.createObjectBuilder()
            .add("name", name)
            .add("meta", meta.toJson())
            .add("type", type.toString())
            .add("tags", jsonTags);
    if (risk != null) {
      jsonAttackStep.add("risk", risk.toJson());
    }
    if (ttc != null) {
      jsonAttackStep.add("ttc", ttc.toJson());
    }
    return jsonAttackStep.build();
  }

  /** A builder for creating {@link AttackStep} objects. */
  public static final class Builder {
    private final String name;
    private Meta.Builder meta = Meta.builder();
    private final AttackStepType type;
    private final List<String> tags = new ArrayList<>();
    private Risk risk = null;
    private TtcExpression ttc = null;

    private Builder(String name, AttackStepType type) {
      this.name = name;
      this.type = type;
    }

    /**
     * Returns the name of this {@code AttackStep.Builder} object.
     *
     * @return the name of this {@code AttackStep.Builder} object
     */
    public String getName() {
      return name;
    }

    /**
     * Returns the meta info of this {@code AttackStep.Builder} object.
     *
     * @return the meta info of this {@code AttackStep.Builder} object
     */
    public Meta.Builder getMeta() {
      return meta;
    }

    /**
     * Sets the meta info of this {@code AttackStep.Builder} object.
     *
     * @param meta the meta info to set
     * @return this {@code AttackStep.Builder} object
     * @throws NullPointerException if {@code meta} is {@code null}
     */
    public Builder setMeta(Meta.Builder meta) {
      checkNotNull(meta);
      this.meta = meta;
      return this;
    }

    /**
     * Adds a tag to this {@code AttackStep.Builder} object.
     *
     * @param tag the tag to add
     * @return this {@code AttackStep.Builder} object
     * @throws NullPointerException if {@code tag} is {@code null}
     */
    public Builder addTag(String tag) {
      checkNotNull(tag);
      tags.add(tag);
      return this;
    }

    /**
     * Returns the risk of this {@code AttackStep.Builder} object, or {@code null}.
     *
     * @return the risk of this {@code AttackStep.Builder} object, or {@code null}
     */
    public Risk getRisk() {
      return risk;
    }

    /**
     * Sets the risk of this {@code AttackStep.Builder} object.
     *
     * @param risk the risk to set, or {@code null}
     * @return this {@code AttackStep.Builder} object
     */
    public Builder setRisk(Risk risk) {
      this.risk = risk;
      return this;
    }

    /**
     * Returns the TTC of this {@code AttackStep.Builder} object, or {@code null}.
     *
     * @return the TTC of this {@code AttackStep.Builder} object, or {@code null}
     */
    public TtcExpression getTtc() {
      return ttc;
    }

    /**
     * Sets the TTC of this {@code AttackStep.Builder} object.
     *
     * @param ttc the TTC to set, or {@code null}
     * @return this {@code AttackStep.Builder} object
     */
    public Builder setTtc(TtcExpression ttc) {
      this.ttc = ttc;
      return this;
    }

    /**
     * Creates a new {@link AttackStep} object.
     *
     * @param asset the built asset
     * @return a new {@link AttackStep} object
     * @throws NullPointerException if {@code asset} is {@code null}
     */
    public AttackStep build(Asset asset) {
      checkNotNull(asset);
      return new AttackStep(name, meta.build(), asset, type, tags, risk, ttc);
    }

    static Builder fromJson(JsonObject jsonAttackStep) {
      var attackStepBuilder =
          AttackStep.builder(
                  jsonAttackStep.getString("name"),
                  AttackStepType.fromString(jsonAttackStep.getString("type")))
              .setMeta(Meta.Builder.fromJson(jsonAttackStep.getJsonObject("meta")));

      var jsonTags = jsonAttackStep.getJsonArray("tags");
      for (int i = 0; i < jsonTags.size(); i++) {
        var tag = jsonTags.getString(i);
        attackStepBuilder.addTag(tag);
      }

      if (jsonAttackStep.containsKey("risk")) {
        var jsonRisk = jsonAttackStep.getJsonArray("risk");
        var risk = Risk.fromJson(jsonRisk);
        attackStepBuilder.setRisk(risk);
      }

      if (jsonAttackStep.containsKey("ttc")) {
        if (jsonAttackStep.isNull("ttc")) {
          attackStepBuilder.setTtc(TtcExpression.EMPTY);
        } else {
          var jsonTtc = jsonAttackStep.getJsonObject("ttc");
          var ttc = TtcExpression.fromJson(jsonTtc);
          attackStepBuilder.setTtc(ttc);
        }
      }

      return attackStepBuilder;
    }
  }

  /**
   * Creates a new {@link Builder} object.
   *
   * @param name the name of the attack step
   * @param type the type of the attack step
   * @return a new {@link Builder} object
   * @throws NullPointerException if {@code name} or {@code type} is {@code null}
   * @throws IllegalArgumentException if {@code name} is not a valid identifier
   */
  public static Builder builder(String name, AttackStepType type) {
    checkIdentifier(name);
    checkNotNull(type);
    return new Builder(name, type);
  }
}
