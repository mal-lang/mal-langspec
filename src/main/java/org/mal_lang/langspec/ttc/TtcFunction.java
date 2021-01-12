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

package org.mal_lang.langspec.ttc;

import static org.mal_lang.langspec.Utils.checkNotNull;
import static org.mal_lang.langspec.Utils.checkNotNullList;

import jakarta.json.Json;
import jakarta.json.JsonValue;
import java.util.List;

/** Immutable class representing a TTC function of an attack step or a defense in a MAL language. */
public final class TtcFunction extends TtcExpression {
  private final TtcDistribution distribution;
  private final List<Double> arguments;

  /**
   * Constructs a new {@code TtcFunction} object.
   *
   * @param distribution the distribution of the new {@code TtcFunction} object
   * @param arguments the arguments of the new {@code TtcFunction} object
   * @throws NullPointerException if {@code distribution}, {@code arguments}, or any object in
   *     {@code arguments} is {@code null}
   * @throws IllegalArgumentException if {@code arguments} is not valid for {@code distribution}
   */
  public TtcFunction(TtcDistribution distribution, List<Double> arguments) {
    super(TtcExpression.FUNCTION);
    checkNotNull(distribution);
    checkNotNullList(arguments);
    distribution.checkArguments(arguments);
    this.distribution = distribution;
    this.arguments = List.copyOf(arguments);
  }

  /**
   * Returns the distribution of this {@code TtcFunction} object.
   *
   * @return the distribution of this {@code TtcFunction} object
   */
  public TtcDistribution getDistribution() {
    return distribution;
  }

  /**
   * Returns the arguments of this {@code TtcFunction} object.
   *
   * @return the arguments of this {@code TtcFunction} object
   */
  public List<Double> getArguments() {
    return arguments;
  }

  @Override
  public double getMeanTtc() {
    return distribution.getMeanTtc(arguments);
  }

  @Override
  public double getMeanProbability() {
    return distribution.getMeanProbability(arguments);
  }

  @Override
  public JsonValue toJson() {
    var jsonArguments = Json.createArrayBuilder();
    for (double argument : arguments) {
      jsonArguments.add(argument);
    }
    return Json.createObjectBuilder()
        .add("type", getType())
        .add("name", distribution.getName())
        .add("arguments", jsonArguments)
        .build();
  }
}
