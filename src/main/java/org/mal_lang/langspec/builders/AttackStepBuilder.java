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
import jakarta.json.JsonString;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.mal_lang.langspec.AttackStepType;
import org.mal_lang.langspec.Risk;
import org.mal_lang.langspec.ttc.TtcExpression;

/**
 * A builder for creating {@link org.mal_lang.langspec.AttackStep} objects.
 *
 * @since 1.0.0
 */
public final class AttackStepBuilder {
  private final String name;
  private final MetaBuilder meta = new MetaBuilder();
  private final AttackStepType type;
  private final Set<String> tags = new LinkedHashSet<>();
  private final Risk risk;
  private final TtcExpression ttc;
  private final StepsBuilder requires;
  private final StepsBuilder reaches;

  /**
   * Constructs a new {@code AttackStepBuilder} object.
   *
   * @param name the name of the attack step
   * @param type the type of the attack step
   * @param risk the risk of the attack step, or {@code null}
   * @param ttc the TTC of the attack step, or {@code null}
   * @param requires the requires steps of the attack step, or {@code null}
   * @param reaches the reaches steps of the attack step, or {@code null}
   * @throws java.lang.NullPointerException if {@code name} or {@code type} is {@code null}
   * @throws java.lang.IllegalArgumentException if {@code name} is not a valid identifier
   * @since 1.0.0
   */
  public AttackStepBuilder(
      String name,
      AttackStepType type,
      Risk risk,
      TtcExpression ttc,
      StepsBuilder requires,
      StepsBuilder reaches) {
    this.name = requireIdentifier(name);
    this.type = requireNonNull(type);
    this.risk = risk;
    this.ttc = ttc;
    this.requires = requires;
    this.reaches = reaches;
  }

  /**
   * Returns the name of this {@code AttackStepBuilder} object.
   *
   * @return the name of this {@code AttackStepBuilder} object
   * @since 1.0.0
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the meta info of this {@code AttackStepBuilder} object.
   *
   * @return the meta info of this {@code AttackStepBuilder} object
   * @since 1.0.0
   */
  public MetaBuilder getMeta() {
    return this.meta;
  }

  /**
   * Returns the type of this {@code AttackStepBuilder} object.
   *
   * @return the type of this {@code AttackStepBuilder} object
   * @since 1.0.0
   */
  public AttackStepType getType() {
    return this.type;
  }

  /**
   * Returns a list of all tags in this {@code AttackStepBuilder} object.
   *
   * @return a list of all tags in this {@code AttackStepBuilder} object
   * @since 1.0.0
   */
  public List<String> getTags() {
    return List.copyOf(this.tags);
  }

  /**
   * Adds a tag to this {@code AttackStepBuilder} object.
   *
   * @param tag the tag to add
   * @return this {@code AttackStepBuilder} object
   * @throws java.lang.NullPointerException if {@code tag} is {@code null}
   * @throws java.lang.IllegalArgumentException if {@code tag} is not a valid identifier
   * @since 1.0.0
   */
  public AttackStepBuilder addTag(String tag) {
    this.tags.add(requireIdentifier(tag));
    return this;
  }

  /**
   * Returns the risk of this {@code AttackStepBuilder} object, or {@code null} if no risk has been
   * set.
   *
   * @return the risk of this {@code AttackStepBuilder} object, or {@code null} if no risk has been
   *     set
   * @since 1.0.0
   */
  public Risk getRisk() {
    return this.risk;
  }

  /**
   * Returns the TTC of this {@code AttackStepBuilder} object, or {@code null} if no TTC has been
   * set.
   *
   * @return the TTC of this {@code AttackStepBuilder} object, or {@code null} if no TTC has been
   *     set
   * @since 1.0.0
   */
  public TtcExpression getTtc() {
    return this.ttc;
  }

  /**
   * Returns the requires steps of this {@code AttackStepBuilder} object, or {@code null} if no
   * requires steps have been set.
   *
   * @return the requires steps of this {@code AttackStepBuilder} object, or {@code null} if no
   *     requires steps have been set
   * @since 1.0.0
   */
  public StepsBuilder getRequires() {
    return this.requires;
  }

  /**
   * Returns the reaches steps of this {@code AttackStepBuilder} object, or {@code null} if no
   * reaches steps has been set.
   *
   * @return the reaches steps of this {@code AttackStepBuilder} object, or {@code null} if no
   *     reaches steps has been set
   * @since 1.0.0
   */
  public StepsBuilder getReaches() {
    return this.reaches;
  }

  /**
   * Creates a new {@code AttackStepBuilder} from a {@link jakarta.json.JsonObject}.
   *
   * @param jsonAttackStep the {@link jakarta.json.JsonObject}
   * @return a new {@code AttackStepBuilder}
   * @throws java.lang.NullPointerException if {@code jsonAttackStep} is {@code null}
   * @since 1.0.0
   */
  public static AttackStepBuilder fromJson(JsonObject jsonAttackStep) {
    requireNonNull(jsonAttackStep);
    var name = jsonAttackStep.getString("name");
    var type = AttackStepType.fromString(jsonAttackStep.getString("type"));
    var risk =
        jsonAttackStep.isNull("risk") ? null : Risk.fromJson(jsonAttackStep.getJsonObject("risk"));
    var ttc =
        jsonAttackStep.isNull("ttc")
            ? null
            : TtcExpression.fromJson(jsonAttackStep.getJsonObject("ttc"));
    var requires =
        jsonAttackStep.isNull("requires")
            ? null
            : StepsBuilder.fromJson(jsonAttackStep.getJsonObject("requires"));
    var reaches =
        jsonAttackStep.isNull("reaches")
            ? null
            : StepsBuilder.fromJson(jsonAttackStep.getJsonObject("reaches"));
    var builder = new AttackStepBuilder(name, type, risk, ttc, requires, reaches);
    builder.getMeta().fromJson(jsonAttackStep.getJsonObject("meta"));
    for (var jsonTag : jsonAttackStep.getJsonArray("tags")) {
      builder.addTag(((JsonString) jsonTag).getString());
    }
    return builder;
  }
}
