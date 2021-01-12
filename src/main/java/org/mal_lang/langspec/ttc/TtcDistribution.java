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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Immutable class representing a TTC distribution. */
public abstract class TtcDistribution {
  private static final Map<String, TtcDistribution> DISTRIBUTION_MAP = new LinkedHashMap<>();

  /** Bernoulli distribution. */
  public static final TtcDistribution BERNOULLI =
      new TtcDistribution("Bernoulli") {
        @Override
        public void checkArguments(List<Double> arguments) {
          checkArgumentList(arguments, 1);
          double probability = arguments.get(0);
          checkProbability(probability);
        }

        @Override
        public double getMeanTtc(List<Double> arguments) {
          checkArguments(arguments);
          double probability = arguments.get(0);
          return probability < 0.5 ? 0.0 : Double.MAX_VALUE;
        }

        @Override
        public double getMeanProbability(List<Double> arguments) {
          checkArguments(arguments);
          double probability = arguments.get(0);
          return probability;
        }
      };

  /** Binomial distribution. */
  public static final TtcDistribution BINOMIAL =
      new TtcDistribution("Binomial") {
        @Override
        public void checkArguments(List<Double> arguments) {
          checkArgumentList(arguments, 2);
          double numberOfTrials = arguments.get(0);
          double probabilityOfSuccess = arguments.get(1);
          checkNonNegativeInteger(numberOfTrials);
          checkProbability(probabilityOfSuccess);
        }

        @Override
        public double getMeanTtc(List<Double> arguments) {
          checkArguments(arguments);
          double numberOfTrials = arguments.get(0);
          double probabilityOfSuccess = arguments.get(1);
          return numberOfTrials * probabilityOfSuccess;
        }

        @Override
        public double getMeanProbability(List<Double> arguments) {
          throw new UnsupportedOperationException();
        }
      };

  /** Exponential distribution. */
  public static final TtcDistribution EXPONENTIAL =
      new TtcDistribution("Exponential") {
        @Override
        public void checkArguments(List<Double> arguments) {
          checkArgumentList(arguments, 1);
          double rate = arguments.get(0);
          checkPositive(rate);
        }

        @Override
        public double getMeanTtc(List<Double> arguments) {
          checkArguments(arguments);
          double rate = arguments.get(0);
          return 1.0 / rate;
        }

        @Override
        public double getMeanProbability(List<Double> arguments) {
          throw new UnsupportedOperationException();
        }
      };

  /** Gamma distribution. */
  public static final TtcDistribution GAMMA =
      new TtcDistribution("Gamma") {
        @Override
        public void checkArguments(List<Double> arguments) {
          checkArgumentList(arguments, 2);
          double shape = arguments.get(0);
          double scale = arguments.get(1);
          checkPositive(shape);
          checkPositive(scale);
        }

        @Override
        public double getMeanTtc(List<Double> arguments) {
          checkArguments(arguments);
          double shape = arguments.get(0);
          double scale = arguments.get(1);
          return shape * scale;
        }

        @Override
        public double getMeanProbability(List<Double> arguments) {
          throw new UnsupportedOperationException();
        }
      };

  /** Log-normal distribution. */
  public static final TtcDistribution LOG_NORMAL =
      new TtcDistribution("LogNormal") {
        @Override
        public void checkArguments(List<Double> arguments) {
          checkArgumentList(arguments, 2);
          // double normalMean = arguments.get(0);
          double normalStandardDeviation = arguments.get(1);
          checkPositive(normalStandardDeviation);
        }

        @Override
        public double getMeanTtc(List<Double> arguments) {
          checkArguments(arguments);
          double normalMean = arguments.get(0);
          double normalStandardDeviation = arguments.get(1);
          return Math.exp(normalMean + normalStandardDeviation * normalStandardDeviation / 2.0);
        }

        @Override
        public double getMeanProbability(List<Double> arguments) {
          throw new UnsupportedOperationException();
        }
      };

  /** Pareto distribution. */
  public static final TtcDistribution PARETO =
      new TtcDistribution("Pareto") {
        @Override
        public void checkArguments(List<Double> arguments) {
          checkArgumentList(arguments, 2);
          double minimumValue = arguments.get(0);
          double shape = arguments.get(1);
          checkPositive(minimumValue);
          checkPositive(shape);
        }

        @Override
        public double getMeanTtc(List<Double> arguments) {
          checkArguments(arguments);
          double minimumValue = arguments.get(0);
          double shape = arguments.get(1);
          return shape > 1 ? (shape * minimumValue) / (shape - 1.0) : Double.MAX_VALUE;
        }

        @Override
        public double getMeanProbability(List<Double> arguments) {
          throw new UnsupportedOperationException();
        }
      };

  /** Truncated normal distribution. */
  public static final TtcDistribution TRUNCATED_NORMAL =
      new TtcDistribution("TruncatedNormal") {
        @Override
        public void checkArguments(List<Double> arguments) {
          checkArgumentList(arguments, 2);
          // double mean = arguments.get(0);
          double standardDeviation = arguments.get(1);
          checkPositive(standardDeviation);
        }

        @Override
        public double getMeanTtc(List<Double> arguments) {
          checkArguments(arguments);
          double mean = arguments.get(0);
          // double standardDeviation = arguments.get(1);
          return mean;
        }

        @Override
        public double getMeanProbability(List<Double> arguments) {
          throw new UnsupportedOperationException();
        }
      };

  /** Uniform distribution. */
  public static final TtcDistribution UNIFORM =
      new TtcDistribution("Uniform") {
        @Override
        public void checkArguments(List<Double> arguments) {
          checkArgumentList(arguments, 2);
          double minimum = arguments.get(0);
          double maximum = arguments.get(1);
          if (minimum > maximum) {
            throw new IllegalArgumentException("Invalid arguments for distribution");
          }
        }

        @Override
        public double getMeanTtc(List<Double> arguments) {
          checkArguments(arguments);
          double minimum = arguments.get(0);
          double maximum = arguments.get(1);
          return (maximum + minimum) / 2.0;
        }

        @Override
        public double getMeanProbability(List<Double> arguments) {
          throw new UnsupportedOperationException();
        }
      };

  /** Easy and certain - {@code Exponential(1.0)}. */
  public static final TtcDistribution EASY_AND_CERTAIN =
      new TtcDistribution("EasyAndCertain") {
        private final TtcExpression ttcExpression = new TtcFunction(EXPONENTIAL, List.of(1.0));

        @Override
        public void checkArguments(List<Double> arguments) {
          checkArgumentList(arguments, 0);
        }

        @Override
        public double getMeanTtc(List<Double> arguments) {
          checkArguments(arguments);
          return ttcExpression.getMeanTtc();
        }

        @Override
        public double getMeanProbability(List<Double> arguments) {
          throw new UnsupportedOperationException();
        }
      };

  /** Easy and uncertain - {@code Bernoulli(0.5) + Exponential(1.0)}. */
  public static final TtcDistribution EASY_AND_UNCERTAIN =
      new TtcDistribution("EasyAndUncertain") {
        private final TtcExpression ttcExpression =
            new TtcAddition(
                new TtcFunction(BERNOULLI, List.of(0.5)),
                new TtcFunction(EXPONENTIAL, List.of(1.0)));

        @Override
        public void checkArguments(List<Double> arguments) {
          checkArgumentList(arguments, 0);
        }

        @Override
        public double getMeanTtc(List<Double> arguments) {
          checkArguments(arguments);
          return ttcExpression.getMeanTtc();
        }

        @Override
        public double getMeanProbability(List<Double> arguments) {
          throw new UnsupportedOperationException();
        }
      };

  /** Hard and certain - {@code Exponential(0.1)}. */
  public static final TtcDistribution HARD_AND_CERTAIN =
      new TtcDistribution("HardAndCertain") {
        private final TtcExpression ttcExpression = new TtcFunction(EXPONENTIAL, List.of(0.1));

        @Override
        public void checkArguments(List<Double> arguments) {
          checkArgumentList(arguments, 0);
        }

        @Override
        public double getMeanTtc(List<Double> arguments) {
          checkArguments(arguments);
          return ttcExpression.getMeanTtc();
        }

        @Override
        public double getMeanProbability(List<Double> arguments) {
          throw new UnsupportedOperationException();
        }
      };

  /** Hard and uncertain - {@code Bernoulli(0.5) + Exponential(0.1)}. */
  public static final TtcDistribution HARD_AND_UNCERTAIN =
      new TtcDistribution("HardAndUncertain") {
        private final TtcExpression ttcExpression =
            new TtcAddition(
                new TtcFunction(BERNOULLI, List.of(0.5)),
                new TtcFunction(EXPONENTIAL, List.of(0.1)));

        @Override
        public void checkArguments(List<Double> arguments) {
          checkArgumentList(arguments, 0);
        }

        @Override
        public double getMeanTtc(List<Double> arguments) {
          checkArguments(arguments);
          return ttcExpression.getMeanTtc();
        }

        @Override
        public double getMeanProbability(List<Double> arguments) {
          throw new UnsupportedOperationException();
        }
      };

  /** Very hard and certain - {@code Exponential(0.01)}. */
  public static final TtcDistribution VERY_HARD_AND_CERTAIN =
      new TtcDistribution("VeryHardAndCertain") {
        private final TtcExpression ttcExpression = new TtcFunction(EXPONENTIAL, List.of(0.01));

        @Override
        public void checkArguments(List<Double> arguments) {
          checkArgumentList(arguments, 0);
        }

        @Override
        public double getMeanTtc(List<Double> arguments) {
          checkArguments(arguments);
          return ttcExpression.getMeanTtc();
        }

        @Override
        public double getMeanProbability(List<Double> arguments) {
          throw new UnsupportedOperationException();
        }
      };

  /** Very hard and uncertain - {@code Bernoulli(0.5) + Exponential(0.01)}. */
  public static final TtcDistribution VERY_HARD_AND_UNCERTAIN =
      new TtcDistribution("VeryHardAndUncertain") {
        private final TtcExpression ttcExpression =
            new TtcAddition(
                new TtcFunction(BERNOULLI, List.of(0.5)),
                new TtcFunction(EXPONENTIAL, List.of(0.01)));

        @Override
        public void checkArguments(List<Double> arguments) {
          checkArgumentList(arguments, 0);
        }

        @Override
        public double getMeanTtc(List<Double> arguments) {
          checkArguments(arguments);
          return ttcExpression.getMeanTtc();
        }

        @Override
        public double getMeanProbability(List<Double> arguments) {
          throw new UnsupportedOperationException();
        }
      };

  /** Infinity constant. */
  public static final TtcDistribution INFINITY =
      new TtcDistribution("Infinity") {
        @Override
        public void checkArguments(List<Double> arguments) {
          checkArgumentList(arguments, 0);
        }

        @Override
        public double getMeanTtc(List<Double> arguments) {
          checkArguments(arguments);
          return Double.MAX_VALUE;
        }

        @Override
        public double getMeanProbability(List<Double> arguments) {
          throw new UnsupportedOperationException();
        }
      };

  /** Zero constant. */
  public static final TtcDistribution ZERO =
      new TtcDistribution("Zero") {
        @Override
        public void checkArguments(List<Double> arguments) {
          checkArgumentList(arguments, 0);
        }

        @Override
        public double getMeanTtc(List<Double> arguments) {
          checkArguments(arguments);
          return 0.0;
        }

        @Override
        public double getMeanProbability(List<Double> arguments) {
          throw new UnsupportedOperationException();
        }
      };

  /** Enabled constant. */
  public static final TtcDistribution ENABLED =
      new TtcDistribution("Enabled") {
        @Override
        public void checkArguments(List<Double> arguments) {
          checkArgumentList(arguments, 0);
        }

        @Override
        public double getMeanTtc(List<Double> arguments) {
          throw new UnsupportedOperationException();
        }

        @Override
        public double getMeanProbability(List<Double> arguments) {
          return 1.0;
        }
      };

  /** Disabled constant. */
  public static final TtcDistribution DISABLED =
      new TtcDistribution("Disabled") {
        @Override
        public void checkArguments(List<Double> arguments) {
          checkArgumentList(arguments, 0);
        }

        @Override
        public double getMeanTtc(List<Double> arguments) {
          throw new UnsupportedOperationException();
        }

        @Override
        public double getMeanProbability(List<Double> arguments) {
          return 0.0;
        }
      };

  static {
    var distributions =
        List.of(
            BERNOULLI,
            BINOMIAL,
            EXPONENTIAL,
            GAMMA,
            LOG_NORMAL,
            PARETO,
            TRUNCATED_NORMAL,
            UNIFORM,
            EASY_AND_CERTAIN,
            EASY_AND_UNCERTAIN,
            HARD_AND_CERTAIN,
            HARD_AND_UNCERTAIN,
            VERY_HARD_AND_CERTAIN,
            VERY_HARD_AND_UNCERTAIN,
            INFINITY,
            ZERO,
            ENABLED,
            DISABLED);
    for (var distribution : distributions) {
      DISTRIBUTION_MAP.put(distribution.getName(), distribution);
    }
  }

  private final String name;

  private TtcDistribution(String name) {
    this.name = name;
  }

  private static void checkArgumentList(List<Double> arguments, int size) {
    checkNotNullList(arguments);
    if (arguments.size() != size) {
      throw new IllegalArgumentException("Invalid arguments for distribution");
    }
    for (double argument : arguments) {
      if (!Double.isFinite(argument)) {
        throw new IllegalArgumentException("Invalid arguments for distribution");
      }
    }
  }

  private static void checkProbability(double probability) {
    if (probability < 0.0 || probability > 1.0) {
      throw new IllegalArgumentException("Invalid arguments for distribution");
    }
  }

  private static void checkPositive(double value) {
    if (value <= 0.0) {
      throw new IllegalArgumentException("Invalid arguments for distribution");
    }
  }

  private static void checkNonNegativeInteger(double value) {
    if (value < 0.0) {
      throw new IllegalArgumentException("Invalid arguments for distribution");
    }
    if (Math.floor(value) != value) {
      throw new IllegalArgumentException("Invalid arguments for distribution");
    }
  }

  /**
   * Returns the name of this {@code TtcDistribution} object.
   *
   * @return the name of this {@code TtcDistribution} object
   */
  public String getName() {
    return name;
  }

  /**
   * Checks the given arguments against this {@code TtcDistribution} object.
   *
   * @param arguments the arguments of the distribution
   * @throws NullPointerException if {@code arguments} or any object in {@code arguments} is {@code
   *     null}
   * @throws IllegalArgumentException if {@code arguments} is not valid for this {@code
   *     TtcDistribution} object
   */
  public abstract void checkArguments(List<Double> arguments);

  /**
   * Returns the mean TTC of this {@code TtcDistribution} object given the arguments in {@code
   * arguments}.
   *
   * @param arguments the arguments of the distribution
   * @return the mean TTC of this {@code TtcDistribution} object given the arguments in {@code
   *     arguments}
   * @throws UnsupportedOperationException if this {@code TtcDistribution} does not support mean TTC
   * @throws NullPointerException if {@code arguments} or any object in {@code arguments} is {@code
   *     null}
   * @throws IllegalArgumentException if {@code arguments} is not valid for this {@code
   *     TtcDistribution} object
   */
  public abstract double getMeanTtc(List<Double> arguments);

  /**
   * Returns the mean probability of this {@code TtcDistribution} given the arguments in {@code
   * arguments}.
   *
   * @param arguments the arguments of the distribution
   * @return the mean probability of this {@code TtcDistribution} object given the arguments in
   *     {@code arguments}
   * @throws UnsupportedOperationException if this {@code TtcDistribution} does not support mean
   *     probability
   * @throws NullPointerException if {@code arguments} or any object in {@code arguments} is {@code
   *     null}
   * @throws IllegalArgumentException if {@code arguments} is not valid for this {@code
   *     TtcDistribution} object
   */
  public abstract double getMeanProbability(List<Double> arguments);

  /**
   * Returns the distribution with the name {@code name}.
   *
   * @param name the name of the distribution
   * @return the distribution with the name {@code name}
   * @throws NullPointerException if {@code name} is {@code null}
   * @throws IllegalArgumentException if {@code name} is not the name of a distribution
   */
  public static TtcDistribution get(String name) {
    checkNotNull(name);
    if (!DISTRIBUTION_MAP.containsKey(name)) {
      throw new IllegalArgumentException(String.format("Invalid distribution \"%s\"", name));
    }
    return DISTRIBUTION_MAP.get(name);
  }
}
