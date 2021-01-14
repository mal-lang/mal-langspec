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
import java.util.ArrayList;
import java.util.List;
import org.mal_lang.langspec.ttc.TtcExpression;

/** A builder for creating {@link AttackStep} objects. */
public final class AttackStepBuilder {
  private final String name;
  private MetaBuilder meta = Meta.builder();
  private final AttackStepType type;
  private final List<String> tags = new ArrayList<>();
  private Risk risk = null;
  private TtcExpression ttc = null;

  AttackStepBuilder(String name, AttackStepType type) {
    this.name = name;
    this.type = type;
  }

  /**
   * Returns the name of this {@code AttackStepBuilder} object.
   *
   * @return the name of this {@code AttackStepBuilder} object
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the meta info of this {@code AttackStepBuilder} object.
   *
   * @return the meta info of this {@code AttackStepBuilder} object
   */
  public MetaBuilder getMeta() {
    return meta;
  }

  /**
   * Sets the meta info of this {@code AttackStepBuilder} object.
   *
   * @param meta the meta info to set
   * @return this {@code AttackStepBuilder} object
   * @throws NullPointerException if {@code meta} is {@code null}
   */
  public AttackStepBuilder setMeta(MetaBuilder meta) {
    checkNotNull(meta);
    this.meta = meta;
    return this;
  }

  /**
   * Returns the type of this {@code AttackStepBuilder} object.
   *
   * @return the type of this {@code AttackStepBuilder} object
   */
  public AttackStepType getType() {
    return type;
  }

  /**
   * Adds a tag to this {@code AttackStepBuilder} object.
   *
   * @param tag the tag to add
   * @return this {@code AttackStepBuilder} object
   * @throws NullPointerException if {@code tag} is {@code null}
   */
  public AttackStepBuilder addTag(String tag) {
    checkNotNull(tag);
    tags.add(tag);
    return this;
  }

  /**
   * Returns the risk of this {@code AttackStepBuilder} object, or {@code null}.
   *
   * @return the risk of this {@code AttackStepBuilder} object, or {@code null}
   */
  public Risk getRisk() {
    return risk;
  }

  /**
   * Sets the risk of this {@code AttackStepBuilder} object.
   *
   * @param risk the risk to set, or {@code null}
   * @return this {@code AttackStepBuilder} object
   */
  public AttackStepBuilder setRisk(Risk risk) {
    this.risk = risk;
    return this;
  }

  /**
   * Returns the TTC of this {@code AttackStepBuilder} object, or {@code null}.
   *
   * @return the TTC of this {@code AttackStepBuilder} object, or {@code null}
   */
  public TtcExpression getTtc() {
    return ttc;
  }

  /**
   * Sets the TTC of this {@code AttackStepBuilder} object.
   *
   * @param ttc the TTC to set, or {@code null}
   * @return this {@code AttackStepBuilder} object
   */
  public AttackStepBuilder setTtc(TtcExpression ttc) {
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

  static AttackStepBuilder fromJson(JsonObject jsonAttackStep) {
    var attackStepBuilder =
        AttackStep.builder(
                jsonAttackStep.getString("name"),
                AttackStepType.fromString(jsonAttackStep.getString("type")))
            .setMeta(MetaBuilder.fromJson(jsonAttackStep.getJsonObject("meta")));

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
