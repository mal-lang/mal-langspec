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

import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import java.util.ArrayList;

/**
 * Immutable class representing a TTC expression of an attack step or a defense in a MAL language.
 */
public abstract class TtcExpression {
  static final String ADDITION = "addition";
  static final String SUBTRACTION = "subtraction";
  static final String MULTIPLICATION = "multiplication";
  static final String DIVISION = "division";
  static final String EXPONENTIATION = "exponentiation";
  static final String FUNCTION = "function";
  static final String NUMBER = "number";

  private final String type;

  /** Singleton object representing the empty TTC expression {@code "[]"}. */
  public static final TtcExpression EMPTY =
      new TtcExpression(null) {
        @Override
        public String getType() {
          throw new UnsupportedOperationException();
        }

        @Override
        public double getMeanTtc() {
          return 0.0;
        }

        @Override
        public double getMeanProbability() {
          throw new UnsupportedOperationException();
        }

        @Override
        public JsonValue toJson() {
          return JsonValue.NULL;
        }
      };

  TtcExpression(String type) {
    this.type = type;
  }

  /**
   * Returns the type of this {@code TtcExpression} object.
   *
   * @return the type of this {@code TtcExpression} object
   * @throws UnsupportedOperationException if this {@code TtcExpression} object is {@link EMPTY}
   */
  public String getType() {
    return type;
  }

  /**
   * Returns the mean TTC of this {@code TtcExpression} object.
   *
   * @return the mean TTC of this {@code TtcExpression} object
   * @throws UnsupportedOperationException if this {@code TtcExpression} does not support mean TTC
   */
  public abstract double getMeanTtc();

  /**
   * Returns the mean probability of this {@code TtcExpression} object.
   *
   * @return the mean probability of this {@code TtcExpression} object
   * @throws UnsupportedOperationException if this {@code TtcExpression} does not support mean
   *     probability
   */
  public abstract double getMeanProbability();

  /**
   * Returns the JSON representation of this {@code TtcExpression} object.
   *
   * @return the JSON representation of this {@code TtcExpression} object
   */
  public abstract JsonValue toJson();

  /**
   * Creates a new {@code TtcExpression} object from the JSON representation of a {@code
   * TtcExpression} object.
   *
   * @param jsonTtc the JSON representation of a {@code TtcExpression} object
   * @return a new {@code TtcExpression} object from the JSON representation of a {@code
   *     TtcExpression} object
   * @throws NullPointerException if {@code jsonTtc} is {@code null}
   */
  public static TtcExpression fromJson(JsonObject jsonTtc) {
    checkNotNull(jsonTtc);
    var type = jsonTtc.getString("type");
    switch (type) {
      case TtcExpression.ADDITION:
        return new TtcAddition(
            fromJson(jsonTtc.getJsonObject("lhs")), fromJson(jsonTtc.getJsonObject("rhs")));
      case TtcExpression.SUBTRACTION:
        return new TtcSubtraction(
            fromJson(jsonTtc.getJsonObject("lhs")), fromJson(jsonTtc.getJsonObject("rhs")));
      case TtcExpression.MULTIPLICATION:
        return new TtcMultiplication(
            fromJson(jsonTtc.getJsonObject("lhs")), fromJson(jsonTtc.getJsonObject("rhs")));
      case TtcExpression.DIVISION:
        return new TtcDivision(
            fromJson(jsonTtc.getJsonObject("lhs")), fromJson(jsonTtc.getJsonObject("rhs")));
      case TtcExpression.EXPONENTIATION:
        return new TtcExponentiation(
            fromJson(jsonTtc.getJsonObject("lhs")), fromJson(jsonTtc.getJsonObject("rhs")));
      case TtcExpression.FUNCTION:
        var arguments = new ArrayList<Double>();
        var jsonArguments = jsonTtc.getJsonArray("arguments");
        for (int i = 0; i < jsonArguments.size(); i++) {
          arguments.add(jsonArguments.getJsonNumber(i).doubleValue());
        }
        return new TtcFunction(TtcDistribution.get(jsonTtc.getString("name")), arguments);
      case TtcExpression.NUMBER:
        return new TtcNumber(jsonTtc.getJsonNumber("value").doubleValue());
      default:
        throw new IllegalArgumentException(
            String.format("Invalid TtcExpression type \"%s\"", type));
    }
  }
}
