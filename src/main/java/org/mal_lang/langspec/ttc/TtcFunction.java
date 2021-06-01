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

import static java.util.Objects.requireNonNull;

import jakarta.json.Json;
import jakarta.json.JsonObject;

/**
 * Immutable class representing a TTC function in a MAL language.
 *
 * @since 1.0.0
 */
public final class TtcFunction extends TtcExpression {
  private final TtcDistribution distribution;
  private final double[] arguments;

  /**
   * Constructs a new {@code TtcFunction} object.
   *
   * @param distribution the distribution of the function
   * @param arguments the arguments of the function
   * @throws java.lang.NullPointerException if {@code distribution} or {@code arguments} is {@code
   *     null}
   * @throws java.lang.IllegalArgumentException if {@code arguments} is not valid for {@code
   *     distribution}
   * @since 1.0.0
   */
  public TtcFunction(TtcDistribution distribution, double... arguments) {
    requireNonNull(distribution);
    requireNonNull(arguments);
    distribution.validateArguments(arguments);
    this.distribution = distribution;
    this.arguments = arguments.clone();
  }

  /**
   * Returns the distribution of this {@code TtcFunction} object.
   *
   * @return the distribution of this {@code TtcFunction} object
   * @since 1.0.0
   */
  public TtcDistribution getDistribution() {
    return this.distribution;
  }

  /**
   * Returns the arguments of this {@code TtcFunction} object.
   *
   * @return the arguments of this {@code TtcFunction} object
   * @since 1.0.0
   */
  public double[] getArguments() {
    return this.arguments.clone();
  }

  @Override
  public double getMeanTtc() {
    return this.distribution.getMeanTtc(this.arguments);
  }

  @Override
  public double getMeanProbability() {
    return this.distribution.getMeanProbability(this.arguments);
  }

  @Override
  public JsonObject toJson() {
    var jsonArguments = Json.createArrayBuilder();
    for (var argument : this.arguments) {
      jsonArguments.add(argument);
    }
    return Json.createObjectBuilder()
        .add("type", "function")
        .add("name", this.distribution.toString())
        .add("arguments", jsonArguments)
        .build();
  }

  /**
   * Creates a new {@code TtcFunction} from a {@link jakarta.json.JsonObject}.
   *
   * @param jsonTtcFunction the {@link jakarta.json.JsonObject}
   * @return a new {@code TtcFunction}
   * @throws java.lang.NullPointerException if {@code jsonTtcFunction} is {@code null}
   * @since 1.0.0
   */
  public static TtcFunction fromJson(JsonObject jsonTtcFunction) {
    requireNonNull(jsonTtcFunction);
    var distribution = TtcDistribution.fromString(jsonTtcFunction.getString("name"));
    var jsonArguments = jsonTtcFunction.getJsonArray("arguments");
    var arguments = new double[jsonArguments.size()];
    for (int i = 0; i < jsonArguments.size(); i++) {
      arguments[i] = jsonArguments.getJsonNumber(i).doubleValue();
    }
    return new TtcFunction(distribution, arguments);
  }
}
